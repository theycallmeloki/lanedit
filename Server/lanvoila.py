#!/usr/bin/env python3
import os
import re
import io
import numpy as np
from flask import Flask, request, send_file, jsonify

import torch
from transformers import AutoModelForCausalLM, AutoTokenizer
from snac import SNAC
import soundfile as sf  # libsndfile must be installed (apt: libsndfile1)

# --------- Config ----------
MODEL_NAME = "theycallmeloki/volady"
SNAC_NAME = "hubertsiuzdak/snac_24khz"

SAMPLE_RATE = 24000
MAX_NEW_TOKENS = 1600          # per chunk
MAX_CHARS_PER_CHUNK = 400      # sentence-bucket cap
SILENCE_PAD_SEC = 0.30         # between chunks
SILENCE_PAD = np.zeros(int(SAMPLE_RATE * SILENCE_PAD_SEC), dtype=np.float32)

# --------- Flask ----------
app = Flask(__name__)

# --------- Model Load ----------
print("Loading LLM...")
use_half = torch.cuda.is_available()
model_dtype = torch.float16 if use_half else torch.float32
model = AutoModelForCausalLM.from_pretrained(
    MODEL_NAME,
    torch_dtype=model_dtype,
    device_map="auto"  # CUDA if available, else CPU
)
tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME)

print("Loading SNAC...")
snac_model = SNAC.from_pretrained(SNAC_NAME).to("cpu")

# --------- Helpers ----------
def chunk_text_sentences(text: str, max_chars: int) -> list[str]:
    """Greedy sentence bucketing to avoid mid-sentence cuts."""
    # split on sentence enders, keep delimiters
    parts = re.split(r'([.!?]\s+)', text)
    # recombine pieces (sentence + delimiter)
    sentences = []
    i = 0
    while i < len(parts):
        if i + 1 < len(parts) and re.match(r'[.!?]\s+', parts[i+1] or ''):
            sentences.append((parts[i] or '') + (parts[i+1] or ''))
            i += 2
        else:
            sentences.append(parts[i] or '')
            i += 1
    # bucket
    chunks, buf = [], ""
    for s in sentences:
        s = s.strip()
        if not s:
            continue
        if len(buf) + len(s) <= max_chars:
            buf = (buf + " " + s).strip()
        else:
            if buf:
                chunks.append(buf)
            if len(s) <= max_chars:
                buf = s
            else:
                # very long single sentence: hard-split
                for j in range(0, len(s), max_chars):
                    piece = s[j:j+max_chars].strip()
                    if piece:
                        if buf:
                            chunks.append(buf)
                        buf = piece
    if buf:
        chunks.append(buf)
    return chunks

@torch.inference_mode()
def generate_speech_tokens(text: str) -> torch.Tensor:
    """Run LLM to get token stream that includes audio codes."""
    input_ids = tokenizer(text, return_tensors="pt").input_ids
    start_token = torch.tensor([[128259]], dtype=torch.int64)              # <|human|>
    end_tokens = torch.tensor([[128009, 128260]], dtype=torch.int64)       # <|end_of_text|><|end_of_human|>
    modified_input_ids = torch.cat([start_token, input_ids, end_tokens], dim=1)

    device = model.device
    modified_input_ids = modified_input_ids.to(device)
    attention_mask = torch.ones_like(modified_input_ids).to(device)

    out = model.generate(
        input_ids=modified_input_ids,
        attention_mask=attention_mask,
        max_new_tokens=MAX_NEW_TOKENS,
        do_sample=True,
        temperature=0.6,
        top_p=0.95,
        repetition_penalty=1.1,
        num_return_sequences=1,
        eos_token_id=128258,  # <|end_of_speech|>
        use_cache=True
    )
    return out

def extract_audio_codes(generated_ids: torch.Tensor) -> list[int]:
    """Crop to speech section and translate to codec code indices."""
    token_start_speech = 128257  # <|start_of_speech|>
    token_end_speech = 128258    # <|end_of_speech|>

    idx = (generated_ids == token_start_speech).nonzero(as_tuple=True)
    if len(idx[1]) > 0:
        last = idx[1][-1].item()
        cropped = generated_ids[:, last + 1:]
    else:
        cropped = generated_ids  # fallback

    row = cropped[0]
    masked = row[row != token_end_speech]

    length = masked.size(0)
    new_len = (length // 7) * 7
    trimmed = masked[:new_len]

    # Offset into proper code space
    return [t.item() - 128266 for t in trimmed]

def decode_snac(code_list: list[int]) -> np.ndarray | None:
    """Reshape codes into layers and decode via SNAC -> float32 mono numpy [-1,1]."""
    if not code_list:
        return None

    L1, L2, L3 = [], [], []
    for i in range(len(code_list) // 7):
        L1.append(code_list[7*i])
        L2.append(code_list[7*i+1] - 4096)
        L3.append(code_list[7*i+2] - (2*4096))
        L3.append(code_list[7*i+3] - (3*4096))
        L2.append(code_list[7*i+4] - (4*4096))
        L3.append(code_list[7*i+5] - (5*4096))
        L3.append(code_list[7*i+6] - (6*4096))

    codes = [
        torch.tensor(L1).unsqueeze(0),
        torch.tensor(L2).unsqueeze(0),
        torch.tensor(L3).unsqueeze(0)
    ]
    audio = snac_model.decode(codes)                 # Tensor [1, T] float
    audio_np = audio.detach().squeeze().cpu().numpy().astype(np.float32)
    # normalize to safe headroom
    peak = np.max(np.abs(audio_np)) if audio_np.size else 0.0
    if peak > 0:
        audio_np = (0.97 * audio_np) / peak
    return audio_np

def text_to_audio_np(text: str) -> np.ndarray:
    """Full pipeline: chunk -> generate -> decode -> concat with silence pads."""
    chunks = chunk_text_sentences(text, MAX_CHARS_PER_CHUNK)
    segments: list[np.ndarray] = []
    for chunk in chunks:
        gen = generate_speech_tokens(chunk)
        codes = extract_audio_codes(gen)
        audio = decode_snac(codes)
        if audio is not None and audio.size > 0:
            segments.append(audio)
            segments.append(SILENCE_PAD)

    if not segments:
        return np.zeros(1, dtype=np.float32)
    return np.concatenate(segments).astype(np.float32)

def wav_bytes_from_audio(audio: np.ndarray) -> io.BytesIO:
    """Write WAV (PCM_16) into BytesIO with proper header."""
    buf = io.BytesIO()
    # shape must be (frames,) or (frames, channels)
    sf.write(buf, audio, SAMPLE_RATE, format="WAV", subtype="PCM_16")
    buf.seek(0)
    return buf

# --------- Routes ----------
@app.route("/health", methods=["GET"])
def health():
    return jsonify({"ok": True})

@app.route("/tts", methods=["POST"])
def tts_route():
    try:
        payload = request.get_json(silent=True) or {}
        text = (payload.get("text") or "").strip()
        if not text:
            return jsonify({"error": "text required"}), 400

        audio = text_to_audio_np(text)
        wav_buf = wav_bytes_from_audio(audio)
        # send_file will set proper Content-Length; aplay can read from stdin with -t wav
        return send_file(wav_buf, mimetype="audio/wav", as_attachment=False, download_name="speech.wav")
    except Exception as e:
        # bubble minimal info to caller
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    # small runtime hygiene
    torch.set_num_threads(max(1, os.cpu_count() // 2))
    app.run(host="0.0.0.0", port=5000)
