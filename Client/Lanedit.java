import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.awt.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.*;
import javax.swing.undo.*;
import javax.swing.plaf.basic.*;
import java.nio.charset.StandardCharsets;


public class Lanedit{
    JFrame frame;
    JPanel panel;
    JTabbedPane pane;
    JTextArea textArea;
	UndoManager undoManager;
    JScrollPane scrollPane;
    ArrayList<JTextArea> arrayTextArea;
	ArrayList<LanFile> arrayFiles;
    String[] keywords;
    String origWord;
	LanCookie cookie;
	LanFile lanfile;
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem newMenuItem;
	JMenuItem openMenuItem;
	JMenuItem closeMenuItem;
	JMenuItem saveMenuItem;
	JMenuItem saveasMenuItem;
	JMenuItem addLerminalMenuItem;
	JMenuItem addLanvoilaMenuItem;
	Lanvoila lanvoila;
	JMenuItem exitMenuItem;
	JMenu editMenu;
	JMenuItem undoMenuItem;
	JMenuItem redoMenuItem;
	JMenuItem moveLeftMenuItem;
	JMenuItem moveRightMenuItem;
	JMenu lancloudMenu;
	JMenuItem loginMenuItem;
	JMenuItem uploadMenuItem;
	JMenuItem downloadMenuItem;
	MenuHandler menuHandler;
	InputStream inputStream;
	Font matrix;
	GraphicsEnvironment graphicsEnvironment;
	
    Lanedit(){
		try{
			frame = new JFrame("Lanedit");
			//The UIManager lines are compulsory and must be put, BEFORE THE MAKING OF THE TABBED PANE.
			UIManager.put("TabbedPane.selected", Color.BLACK);
			UIManager.put("TabbedPane.tabAreaBackground", Color.BLACK);
			UIManager.put("TabbedPane.borderColor", Color.BLACK);
			UIManager.put("TabbedPane.darkShadow", Color.BLACK);
			UIManager.put("TabbedPane.light", Color.BLACK);
			UIManager.put("TabbedPane.highlight", Color.BLACK);
			UIManager.put("TabbedPane.focus", Color.BLACK);
			UIManager.put("TabbedPane.unselectedBackground", Color.BLACK);
			UIManager.put("TabbedPane.selectHighlight", Color.BLACK);
			UIManager.put("TabbedPane.borderHightlightColor", Color.GREEN);
			UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
			UIManager.put("TextArea.selectionBackground", Color.GREEN);
			UIManager.put("TextArea.selectionForeground", Color.BLACK);
			UIManager.put("TextField.selectionBackground", Color.GREEN);
			UIManager.put("TextField.selectionForeground", Color.BLACK);
			UIManager.put("Menu.selectionBackground", Color.GREEN);
			UIManager.put("Menu.selectionForeground", Color.BLACK);
			UIManager.put("MenuItem.selectionBackground", Color.GREEN);
			UIManager.put("MenuItem.selectionForeground", Color.BLACK);
			UIManager.put("MenuItem.acceleratorForeground", Color.GREEN);
			pane = new JTabbedPane(JTabbedPane.BOTTOM);
			menuBar = new JMenuBar();
			menuHandler = new MenuHandler();
			fileMenu = new JMenu("File");
			newMenuItem = new JMenuItem("New");
			openMenuItem = new JMenuItem("Open");
			closeMenuItem = new JMenuItem("Close");
			saveMenuItem = new JMenuItem("Save");
			saveasMenuItem = new JMenuItem("Save as");
			addLerminalMenuItem = new JMenuItem("Add Lerminal");
			addLanvoilaMenuItem = new JMenuItem("Add Lanvoila");
			exitMenuItem = new JMenuItem("Exit");
			fileMenu.add(newMenuItem);
			fileMenu.add(openMenuItem);
			fileMenu.add(closeMenuItem);
			fileMenu.add(saveMenuItem);
			fileMenu.add(saveasMenuItem);
			fileMenu.add(addLerminalMenuItem);
			// fileMenu.add(addLanvoilaMenuItem);
			fileMenu.add(exitMenuItem);
			newMenuItem.addActionListener(menuHandler);
			openMenuItem.addActionListener(menuHandler);
			closeMenuItem.addActionListener(menuHandler);
			saveMenuItem.addActionListener(menuHandler);
			saveasMenuItem.addActionListener(menuHandler);
			addLerminalMenuItem.addActionListener(menuHandler);
			// addLanvoilaMenuItem.addActionListener(menuHandler);
			exitMenuItem.addActionListener(menuHandler);
			editMenu = new JMenu("Edit");
			undoMenuItem = new JMenuItem("Undo");
			redoMenuItem = new JMenuItem("Redo");
			moveLeftMenuItem = new JMenuItem("Move Left");
			moveRightMenuItem = new JMenuItem("Move Right");
			undoMenuItem.addActionListener(menuHandler);
			redoMenuItem.addActionListener(menuHandler);
			moveLeftMenuItem.addActionListener(menuHandler);
			moveRightMenuItem.addActionListener(menuHandler);
			editMenu.add(undoMenuItem);
			editMenu.add(redoMenuItem);
			editMenu.add(moveLeftMenuItem);
			editMenu.add(moveRightMenuItem);
			lancloudMenu = new JMenu("Lancloud");
			loginMenuItem = new JMenuItem("Login");
			uploadMenuItem = new JMenuItem("Upload");
			downloadMenuItem = new JMenuItem("Download");
			loginMenuItem.addActionListener(menuHandler);
			uploadMenuItem.addActionListener(menuHandler);
			downloadMenuItem.addActionListener(menuHandler);
			lancloudMenu.add(loginMenuItem);
			lancloudMenu.add(uploadMenuItem);
			lancloudMenu.add(downloadMenuItem);
			//Start of Keyboard Accelerators:
			newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
			openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
			closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
			saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			addLerminalMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
			// addLanvoilaMenuItem.setAccelerator(
			// 	KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)
			// );
			undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
			redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
			moveLeftMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.ALT_MASK));
			moveRightMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.ALT_MASK));
			uploadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
			//Lanedit Resources:
			arrayTextArea = new ArrayList<JTextArea>();
			arrayFiles = new ArrayList<LanFile>();
			undoManager = new UndoManager();
			inputStream = Lanedit.class.getResourceAsStream("Matrix.ttf");
			matrix = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(20f);
			graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			graphicsEnvironment.registerFont(matrix);
			origWord = "";
			lanvoila = new Lanvoila();
			lanvoila.setStyle(matrix, Color.BLACK, Color.GREEN);
			pane.addTab("Lanvoila", lanvoila.getPanel());
			//Changing colors for everything: 
			pane.setForeground(Color.GREEN);
			pane.setBackground(Color.BLACK);
			menuBar.setForeground(Color.GREEN);
			menuBar.setBackground(Color.BLACK);
			fileMenu.setForeground(Color.GREEN);
			fileMenu.setBackground(Color.BLACK);
			newMenuItem.setForeground(Color.GREEN);
			newMenuItem.setBackground(Color.BLACK);
            		openMenuItem.setForeground(Color.GREEN);
			openMenuItem.setBackground(Color.BLACK);
            		closeMenuItem.setForeground(Color.GREEN);
			closeMenuItem.setBackground(Color.BLACK);
			saveMenuItem.setForeground(Color.GREEN);
			saveMenuItem.setBackground(Color.BLACK);
			saveasMenuItem.setForeground(Color.GREEN);
			saveasMenuItem.setBackground(Color.BLACK);
			addLerminalMenuItem.setForeground(Color.GREEN);
			addLerminalMenuItem.setBackground(Color.BLACK);
			addLanvoilaMenuItem.setForeground(Color.GREEN);
			addLanvoilaMenuItem.setBackground(Color.BLACK);
			exitMenuItem.setForeground(Color.GREEN);
			exitMenuItem.setBackground(Color.BLACK);
            		editMenu.setForeground(Color.GREEN);
			editMenu.setBackground(Color.BLACK);
			undoMenuItem.setForeground(Color.GREEN);
			undoMenuItem.setBackground(Color.BLACK);
			redoMenuItem.setForeground(Color.GREEN);
			redoMenuItem.setBackground(Color.BLACK);
			moveLeftMenuItem.setForeground(Color.GREEN);
			moveLeftMenuItem.setBackground(Color.BLACK);
			moveRightMenuItem.setForeground(Color.GREEN);
			moveRightMenuItem.setBackground(Color.BLACK);
			lancloudMenu.setForeground(Color.GREEN);
			lancloudMenu.setBackground(Color.BLACK);
			loginMenuItem.setForeground(Color.GREEN);
			loginMenuItem.setBackground(Color.BLACK);
			uploadMenuItem.setForeground(Color.GREEN);
			uploadMenuItem.setBackground(Color.BLACK);
			downloadMenuItem.setForeground(Color.GREEN);
			downloadMenuItem.setBackground(Color.BLACK);
			//End of changing colors
			frame.add(pane);
			frame.setJMenuBar(menuBar);
			menuBar.add(fileMenu);
			menuBar.add(editMenu);
			menuBar.add(lancloudMenu);
			frame.setSize(800,800);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setBackground(Color.BLACK);
			frame.setVisible(true);
			readKeywords();
			
		}
		catch(Exception e){
			System.out.println(e);
		}
    }

    public void AddTab(String filename,String content){
        textArea = new JTextArea();
		lanfile = new LanFile();
        textArea.setTabSize(4);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.GREEN);
		textArea.append(content);
		textArea.setFont(matrix);
        textArea.addKeyListener(new EnterListener());
        textArea.addKeyListener(new TabSpaceListener());
		textArea.getDocument().addUndoableEditListener(undoManager);
        arrayTextArea.add(textArea);
		arrayFiles.add(lanfile);
        scrollPane = new JScrollPane(textArea);
        panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.BLACK);
		panel.setForeground(Color.GREEN);
        panel.add(scrollPane);
        pane.addTab(filename,panel);
    }
	
	public void AddLerminal(){
		Lerminal lerminal = new Lerminal();
		lerminal.setStyle(matrix, Color.BLACK, Color.GREEN);
		JPanel lermPanel = lerminal.getLerminal();
		pane.addTab("Lerminal",lermPanel);
	}
	
	public void RemoveTab(){
		int length = pane.getTabCount();
		if(length > 0){
			int paneIndex = pane.getSelectedIndex();
			Component comp = pane.getComponentAt(paneIndex); 
			String paneName = pane.getTitleAt(paneIndex);
			if(paneName.equalsIgnoreCase("lanvoila")){
				JOptionPane.showMessageDialog(frame,"Can't remove Lanvoila.","Lanedit says,",JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			pane.remove(paneIndex);

			if(!(paneName.equalsIgnoreCase("lerminal"))){
				arrayTextArea.remove(Math.min(paneIndex, arrayTextArea.size()-1));
			}
		}
		else{
			JOptionPane.showMessageDialog(frame,"No tab to remove!","Lanedit says,",JOptionPane.INFORMATION_MESSAGE);
		}
	}


    public void readKeywords(){
        try{
            FileReader fileReader = new FileReader("keywords.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> lines = new ArrayList<String>();
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                lines.add(line);
            }
            bufferedReader.close();
            keywords = lines.toArray(new String[lines.size()]);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
	
	public void moveLeft(){
		int paneIndex = pane.getSelectedIndex();
		if(paneIndex > 0){
			pane.setSelectedIndex(paneIndex - 1);
		}
	}
	
	public void moveRight(){
		int paneIndex = pane.getSelectedIndex();
		int length = pane.getTabCount();
		if(paneIndex < (length - 1)){
			pane.setSelectedIndex(paneIndex + 1);
		}
	}
	
	public void undo(){
		undoManager.undo();
	}
	
	public void redo(){
		undoManager.redo();
	}
	
	public void exit(){
		System.exit(0);
	}
	
	public void newTab(){
		AddTab("Untitled","");
	}
	
	public void prompt(){
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        JTextField username = new JTextField(15);
        JPasswordField password = new JPasswordField(15);		
        password.setEchoChar('x');
        loginPanel.add(new JLabel("Username: "));
        loginPanel.add(username);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(new JLabel("Password: "));
        loginPanel.add(password);
        int result = JOptionPane.showConfirmDialog(frame,loginPanel,"Enter Credentials to Login",JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION){
			String usernameString = username.getText();
			String passwordString = new String(password.getPassword());
			LanCloudFramework login = new LanCloudFramework();
			cookie = new LanCookie();
			cookie.authed = login.login(usernameString,passwordString);
			if(cookie.isAuthed()){
				String response = login.getResponse();
				cookie.uid = Integer.parseInt(response.substring(4));
			}
			else{
				JOptionPane.showMessageDialog(frame,login.getResponse(),"LanCloud Server says,",JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else{
			//Empty cookie, simply or else it'll bring up a NullPointerException.
			cookie = new LanCookie();
		}
    }
	
	public void upload(){
		if(cookie.isAuthed()){
			String content = arrayTextArea.get(pane.getSelectedIndex()).getText();
			if(content.isEmpty()){
				JOptionPane.showMessageDialog(frame,"Write something before uploading it!","Lanedit says,",JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				String filename = JOptionPane.showInputDialog(frame,"Filename: ","Enter Filename to Upload",JOptionPane.QUESTION_MESSAGE);
				if(!(filename.isEmpty())){
					//^ If filename is not empty
					LanCloudFramework uploader = new LanCloudFramework();
					uploader.upload(cookie,filename,content);
					JOptionPane.showMessageDialog(frame,uploader.getResponse(),"Lancloud Server says,",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		else{
			JOptionPane.showMessageDialog(frame,"Please Log in before continuing.","Lanedit says,",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void download(){
		if(cookie.isAuthed()){
			JPanel downloadPanel = new JPanel();
			downloadPanel.setLayout(new BoxLayout(downloadPanel, BoxLayout.Y_AXIS));
			LanCloudFramework lister = new LanCloudFramework();
			String[] options = lister.filenameLister(cookie);
			JComboBox dropdown = new JComboBox<String>(options);
			dropdown.setEditable(false);
			downloadPanel.add(new JLabel("Filename: "));
			downloadPanel.add(dropdown);
			if(options.length == 0){
				JOptionPane.showMessageDialog(frame,"You probably dont have any files online.","LanCloud Server says,",JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				int result = JOptionPane.showConfirmDialog(frame,downloadPanel,"Enter Filename to Download",JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION){
					String file = "";
					try{
						file = dropdown.getSelectedItem().toString();
					}
					catch(NullPointerException npe){
						//Do nothing cuz it'll be taken care of by the control thingy down.
					}
					if (!(file.isEmpty())){
						LanCloudFramework downloader = new LanCloudFramework();
						AddTab("Untitled",downloader.download(cookie,file));
					}
				}
			}
		}
		else{
			JOptionPane.showMessageDialog(frame,"Please Log in before continuing.","Lanedit says,",JOptionPane.INFORMATION_MESSAGE);
			
		}
	}
	
	public void open(){
		JFileChooser chooser = new JFileChooser();
		int returnValue = chooser.showOpenDialog(frame);
		if(returnValue == JFileChooser.APPROVE_OPTION){
			File tempFile = chooser.getSelectedFile();
			lanfile.setFile(tempFile);
			arrayTextArea.get(pane.getSelectedIndex()).setText(read());
			pane.setTitleAt(pane.getSelectedIndex(),tempFile.getName());
		}
	}
	
	public String read(){
	String content = "";
		try{
			BufferedReader reader = new BufferedReader(new FileReader(lanfile.file));
			
			String line;
			StringBuilder builder = new StringBuilder();
			while((line = reader.readLine()) != null){
				builder.append(line);
				builder.append("\n");
			}
			content = builder.toString();
		}
		catch(Exception e){
			System.out.println(e);
		}
		return content;
	}
	
	public void save(){
		if(lanfile.isSavedBefore()){
			String content = arrayTextArea.get(pane.getSelectedIndex()).getText();
			write(content);
		}
		else{
			saveas();
		}
	}
	
	public void saveas(){
		JFileChooser chooser = new JFileChooser();
		int returnValue = chooser.showSaveDialog(frame);
		if(returnValue == JFileChooser.APPROVE_OPTION){
			File tempFile = chooser.getSelectedFile();
			lanfile.setFile(tempFile);
			lanfile.setSaved(true);
			save();
		}
	}
	
	public void write(String content){
		try{
			File tempFile = lanfile.getFile();
			pane.setTitleAt(pane.getSelectedIndex(),tempFile.getName());
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));
			writer.print(content);
			writer.flush();
			writer.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	
    public static void main(String[] args){
		LanvoilaServer.start();
        Lanedit lanedit = new Lanedit();
		//lanedit.prompt();
		lanedit.AddTab("Lanedit","");
		Runtime.getRuntime().addShutdownHook(new Thread(LanvoilaServer::stop));
    }
	
	public class MenuHandler implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			String menuOption = ae.getActionCommand();
			if(menuOption.equalsIgnoreCase("new")){
				newTab();
			}
			else if(menuOption.equalsIgnoreCase("open")){
				open();
			}
			else if(menuOption.equalsIgnoreCase("close")){
				RemoveTab();
			}
			else if(menuOption.equalsIgnoreCase("save")){
				save();
			}
			else if(menuOption.equalsIgnoreCase("save as")){
				saveas();
			}
			else if(menuOption.equalsIgnoreCase("add lerminal")){
				AddLerminal();
			}
			else if(menuOption.equalsIgnoreCase("exit")){
				exit();
			}
			else if(menuOption.equalsIgnoreCase("undo")){
				undo();
			}
			else if(menuOption.equalsIgnoreCase("redo")){
				redo();
			}
			else if(menuOption.equalsIgnoreCase("move left")){
				moveLeft();
			}
			else if(menuOption.equalsIgnoreCase("move right")){
				moveRight();
			}
			else if(menuOption.equalsIgnoreCase("login")){
				prompt();
			}
			else if(menuOption.equalsIgnoreCase("upload")){
				upload();
			}
			else if(menuOption.equalsIgnoreCase("download")){
				download();
			}
		}
	}

    public class EnterListener implements KeyListener{
        public void keyReleased(KeyEvent key){/*Nothing*/}
        public void keyPressed(KeyEvent key){/*Nothing*/}

        public void keyTyped(KeyEvent key){
            char keyCode = key.getKeyChar();
            if(keyCode == 125){
                removeTabCloseBracket();
            }
            if(keyCode == 10){
              indent();
            }
        }

        public void removeTabCloseBracket(){
            int paneIndex = pane.getSelectedIndex();
            int currentPosition = arrayTextArea.get(paneIndex).getCaretPosition();
			int prevLetter = arrayTextArea.get(paneIndex).getText().charAt(currentPosition - 1);
			if(prevLetter == ' '){
				arrayTextArea.get(paneIndex).replaceRange(null,currentPosition - 1, currentPosition);
			}
        }
        
        public void indent(){
            int paneIndex = pane.getSelectedIndex();
            int currentPosition = arrayTextArea.get(paneIndex).getCaretPosition();
            int indentCount = 0;
            for(int i = currentPosition; i>=0; i--){
                int prevLetter;
                if(i==0){
                    prevLetter = '\t';
                }
                else{
                    prevLetter = arrayTextArea.get(paneIndex).getText().charAt(i - 1);
                }
                if(prevLetter == '{'){
                    indentCount = indentCount + 1;
                }
                if(prevLetter == '}'){
                    indentCount = indentCount - 1;
                }
            }
            String tabLen = "";
            for(int i = 0; i < indentCount; i++){
                tabLen = tabLen + "\t";
            }
            currentPosition = arrayTextArea.get(paneIndex).getCaretPosition();
            arrayTextArea.get(paneIndex).insert(tabLen,currentPosition);
        }
    }
    
    public class TabSpaceListener implements KeyListener{
        public int opNumber = 0;
        public int paneIndex;
        public void keyReleased(KeyEvent key){/*Nothing*/}
        public void keyPressed(KeyEvent key){/*Nothing*/}

        public void keyTyped(KeyEvent key){
            char keyCode = key.getKeyChar();
            if(keyCode == 9){
              tabCompletion();
            }
            else{
                origWord = "";
            }
        }

        public void tabCompletion(){
            paneIndex = pane.getSelectedIndex();
            boolean completion = removeTabOrNot();
            String word = "";

            if(completion == true){
                if(origWord.isEmpty())
                {
                    word = "";
                    int i = 1;
                    while(true){
                        int Position = (int)(arrayTextArea.get(paneIndex).getCaretPosition() - i);
                        if (Position < 0){
                            break;
                        }
                        char Letter = arrayTextArea.get(paneIndex).getText().charAt(Position);
                        if(Character.isLetter(Letter)){
                            word = "" + Letter + "" + word;
                            i = i + 1;
                            continue;
                        }
                        else{
                            break;
                        }
                    }
                origWord = word;
                }
            wordChooser(origWord);
            }
        }

        public boolean removeTabOrNot(){
            int currentPosition;
            char prevLetter;
            currentPosition = arrayTextArea.get(paneIndex).getCaretPosition();
            arrayTextArea.get(paneIndex).replaceRange(null,currentPosition - 1, currentPosition);
            currentPosition = arrayTextArea.get(paneIndex).getCaretPosition();
            if (currentPosition == 0){
            prevLetter = '\t';
            }
            else{
            prevLetter = arrayTextArea.get(paneIndex).getText().charAt(currentPosition - 1);
            }
            if(!(Character.isLetter(prevLetter))){
                arrayTextArea.get(paneIndex).insert("\t",currentPosition);
                return false;
            }
            else{
                return true;
            }
        }

        public void wordChooser(String word){
            if((!(word.equalsIgnoreCase("")))){
                java.util.List<String> options = new ArrayList<String>();
                for(int x=0;x<keywords.length;x++){
                    String thisKeyword = keywords[x];
                    if((thisKeyword.startsWith(word)) && (thisKeyword.contains(word))){
                        options.add(thisKeyword);
                    }
                }
            decideToFill(options);
            }
        }
        

        public void decideToFill(java.util.List<String> options){
            String[] optionsArray = options.toArray(new String[options.size()]);
            if(optionsArray.length > 0){
                if(opNumber < optionsArray.length - 1){
                    opNumber = opNumber + 1;
                }
                else{
                    opNumber = 0;
                }
                fillWithWord(optionsArray[opNumber]);
            }
        }

        public void fillWithWord(String wordToFill){
            int i = 1;
            while(true){
                int Position = (int)(arrayTextArea.get(paneIndex).getCaretPosition() - i);
                if (Position < 0){
                        break;
                }
                char Letter = arrayTextArea.get(paneIndex).getText().charAt(Position);
                if(Character.isLetter(Letter)){
                    i = i + 1;
                    continue;
                }
                else{
                    break;
                }
            }
            int startRange = ((arrayTextArea.get(paneIndex).getCaretPosition() - i) + 1);
            int endRange = arrayTextArea.get(paneIndex).getCaretPosition();
            arrayTextArea.get(paneIndex).replaceRange(null,startRange,endRange);
            int currentPosition = arrayTextArea.get(paneIndex).getCaretPosition();
            arrayTextArea.get(paneIndex).insert(wordToFill,currentPosition);
        }
    }
}

class LanCloudFramework{
	public static final String site = "http://lanclouder.appspot.com/";
	//Site has to have trailing slash
	BufferedReader reader;
	StringBuilder builder;
	OutputStreamWriter writer;
	URL url;
	String response;
	URLConnection connector;
	LanCookie cookie;
	String[] files;
	
	public boolean login(String username, String password){
		if(username != "" && password != ""){
			try{
			
				url = new URL(site + "login?username=" + username + "&" + "password=" + password);
				connector = url.openConnection();
				reader = new BufferedReader(new InputStreamReader(connector.getInputStream()));
				response = reader.readLine();
			}
			catch(Exception e){
				System.out.println(e);
			}
		}
		if(response.startsWith("uid")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void upload(LanCookie cookie,String filename,String content){
		try{
			url = new URL(site + "upload");
			connector = url.openConnection();
			connector.setDoOutput(true);
			writer = new OutputStreamWriter(connector.getOutputStream());
			writer.write("uid=" + URLEncoder.encode(""+cookie.getUID(),"UTF-8") + "&");
			writer.write("filename=" + URLEncoder.encode(filename,"UTF-8") + "&");
			writer.write("content=" + URLEncoder.encode(content,"UTF-8"));
			writer.close();
			reader = new BufferedReader(new InputStreamReader(connector.getInputStream()));
			response = reader.readLine();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String[] filenameLister(LanCookie cookie){
		try{
			url = new URL(site + "lister?uid=" + cookie.getUID());
			connector = url.openConnection();
			reader = new BufferedReader(new InputStreamReader(connector.getInputStream()));
			ArrayList<String> list = new ArrayList<String>();
			String line;
			while((line = reader.readLine()) != null){
				list.add(line);
			}
			files = list.toArray(new String[list.size()]);
		}
		catch(Exception e){
			System.out.println(e);
		}
		return files;
	}
	
	public String download(LanCookie cookie,String filename){
		try{
			url = new URL(site + "download?uid=" + cookie.getUID() + "&filename=" + URLEncoder.encode(filename,"UTF-8"));
			connector = url.openConnection();
			reader = new BufferedReader(new InputStreamReader(connector.getInputStream()));
			builder = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null){
				builder.append(line);
				builder.append("\n");
			}
			response = builder.toString();
		}
		catch(Exception e){
			System.out.println(e);
		}
		return response;
	}
	
	public String getResponse(){
		return this.response;
	}
}

class LanCookie{
	boolean authed = false;
	int uid = 0;
	
	public int getUID(){
		return this.uid;
	}
	
	public boolean isAuthed(){
		return this.authed;
	}
}

class LanFile{
	File file;
	boolean savedBefore = false;
	
	public boolean isSavedBefore(){
		return this.savedBefore;
	}
	
	public File getFile(){
		return this.file;
	}
	
	public void setSaved(boolean savedBefore){
		this.savedBefore = savedBefore;
	}
	
	public void setFile(File file){
		this.file = file;
	}
}

class Lerminal{
	JPanel panel;
	JTextField input;
	JTextArea output;
	JScrollPane scroller;
	ProcessBuilder builder;
	Process process;
	OutputStream stdin;
	InputStream stdout;
	InputStream stderr;
	StdoutStreamThread ost;
	Thread ostThread;
	StderrStreamThread est;
	Thread estThread;
	
	public Lerminal(){
		try{
			panel = new JPanel(new BorderLayout());
			input = new JTextField();
			output = new JTextArea();
			input.addActionListener(new Actioner());
			output.setEditable(false);
			output.setLineWrap(true);
			output.setWrapStyleWord(true);
			output.setTabSize(4);
			builder = new ProcessBuilder("bash");
			process = builder.start();
			stdin = process.getOutputStream();
			stdout = process.getInputStream();
			stderr = process.getErrorStream();
			scroller = new JScrollPane(output);
			ost = new StdoutStreamThread();
			ostThread = new Thread(ost);
			ostThread.start();
			est = new StderrStreamThread();
			estThread = new Thread(est);
			estThread.start();
			panel.add(scroller);
			panel.add(input,BorderLayout.SOUTH);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public JPanel getLerminal(){
		return panel;
	}
	
	public void setStyle(Font matrix, Color black, Color green){
		output.setFont(matrix);
		output.setBackground(black);
		output.setForeground(green);
		input.setFont(matrix);
		input.setBackground(black);
		input.setForeground(green);
	}
	
	public void outputAppend(String outLine){
		try{
			int length = outLine.length();
			for(int i=0; i<length;i++){
				char ch =(char) outLine.charAt(i);
				output.append(""+ch);
				Thread.sleep(3);
				output.setCaretPosition(output.getText().length());
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void sendIn(String inLine){
		try{
			inLine = inLine + "\n";
			stdin.write(inLine.getBytes());
			stdin.flush();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public class Actioner implements ActionListener{
		public void actionPerformed(ActionEvent e){
			outputAppend("\n");
			String command = input.getText();
			sendIn(command);
			input.setText("");
		}
	}
	
	public class StdoutStreamThread implements Runnable{
		public void run(){
			Scanner stdoutScanner = new Scanner(stdout);
			while(stdoutScanner.hasNextLine()){
				String outLine = stdoutScanner.nextLine() + "\n";
				outputAppend(outLine);
			}
		}
	}
	
	public class StderrStreamThread implements Runnable{
		public void run(){
			Scanner stderrScanner = new Scanner(stderr);
			while(stderrScanner.hasNextLine()){
				String errLine = stderrScanner.nextLine() + "\n";
				outputAppend(errLine);
			}
		}
	}
}


class Lanvoila {
    private JPanel panel;
    private JTextArea inputArea;
    private JPanel chatPanel;
    private JScrollPane chatScroll;
	private Font matrix;

	private ImageIcon miladyIcon;

	private final Queue<Bubble> playbackQueue = new LinkedList<>();
	private boolean isPlaying = false;

	private class Bubble {
		JPanel row;
		JTextArea msgArea;
		File audio;
		String text;
		Bubble(JPanel row, JTextArea msgArea, File audio, String text) {
			this.row = row;
			this.msgArea = msgArea;
			this.audio = audio;
			this.text = text;
		}
	}

	private void enqueueBubble(String text, File audioFile) {
		Bubble bubble = addChatBubble(text, audioFile); // just builds UI
		synchronized (playbackQueue) {
			playbackQueue.add(bubble);
			if (!isPlaying) {
				playNextBubble();
			}
		}
	}

	private static class ScriptLine {
		String text;
		File audio;
		ScriptLine(String text, File audio) {
			this.text = text; this.audio = audio;
		}
	}

	private final java.util.List<ScriptLine> script = new ArrayList<>();


    public Lanvoila() {
		loadProfileImage();
        buildUI();
    }

	private void loadProfileImage() {
		try {
			File f = new File("milady.png");
			System.out.println("Loading avatar from: " + f.getAbsolutePath());

			BufferedImage raw = javax.imageio.ImageIO.read(f);
			if (raw == null) {
				System.out.println("ImageIO.read() returned null!");
				miladyIcon = null;
				return;
			}

			int size = 40; // avatar size
			BufferedImage circle = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = circle.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
			g2.drawImage(raw, 0, 0, size, size, null);
			g2.dispose();

			miladyIcon = new ImageIcon(circle);
		} catch (Exception e) {
			e.printStackTrace();
			miladyIcon = null;
		}
	}


    // ---------- UI ----------
    private void buildUI() {
		panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.BLACK);

		// the actual vertical list of message rows
		chatPanel = new JPanel();
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatPanel.setBackground(Color.BLACK);
		chatPanel.setAlignmentY(Component.TOP_ALIGNMENT);

		// wrapper pins chatPanel to the top of the viewport (prevents stretching)
		JPanel chatWrapper = new JPanel(new BorderLayout());
		chatWrapper.setBackground(Color.BLACK);
		chatWrapper.add(chatPanel, BorderLayout.NORTH);

		// place wrapper in the scrollpane (viewport shows wrapper; chatPanel sits at NORTH)
		chatScroll = new JScrollPane(chatWrapper);
		chatScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatScroll.getViewport().setBackground(Color.BLACK);
		chatScroll.setBorder(null);
		chatScroll.getVerticalScrollBar().setUnitIncrement(16); // smoother scrolling
		themeScrollBar(chatScroll);

		// input area (keeps its own scrollpane)
		inputArea = new JTextArea(4, 40);
		inputArea.setLineWrap(true);
		inputArea.setWrapStyleWord(true);
		inputArea.setBackground(Color.BLACK);
		inputArea.setForeground(Color.GREEN);
		inputArea.setCaretColor(Color.GREEN);
		if (matrix != null) inputArea.setFont(matrix);

		JScrollPane inputScroll = new JScrollPane(inputArea);
		inputScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		inputScroll.setBorder(null);

		// SHIFT+ENTER = send TTS (non-blocking)
		InputMap im = inputArea.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am = inputArea.getActionMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK), "sendTTS");
		am.put("sendTTS", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = inputArea.getText().trim();
				if (!text.isEmpty()) {
					inputArea.setText("");
					new Thread(() -> sendTTS(text), "LanvoilaTTS").start();
				}
			}
		});

		panel.add(chatScroll, BorderLayout.CENTER);
		panel.add(inputScroll, BorderLayout.SOUTH);
	}


    public void setStyle(Font font, Color bg, Color fg){
		// set font
		this.matrix = font;
        // container colors
        panel.setBackground(bg);
        chatPanel.setBackground(bg);
        chatScroll.getViewport().setBackground(bg);

        // input styling
        inputArea.setFont(font);
        inputArea.setBackground(bg);
        inputArea.setForeground(fg);
        inputArea.setCaretColor(fg);

        // scrollbar colors (simple)
        chatScroll.setBackground(bg);
    }

    // ---------- Chat ----------
    private Bubble addChatBubble(String text, File audioFile) {
		final Bubble[] result = new Bubble[1];

		Runnable r = () -> {
			String timestamp = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());

			// choose play glyph
			char playChar = '\u25B6';
			String play = (matrix != null && matrix.canDisplay(playChar)) ? " \u25B6 " : " > ";

			// avatar (scaled to 72x72)
			JLabel avatarLabel = new JLabel();
			if (miladyIcon != null) {
				Image scaledImg = miladyIcon.getImage().getScaledInstance(72, 72, Image.SCALE_SMOOTH);
				avatarLabel.setIcon(new ImageIcon(scaledImg));
			}
			avatarLabel.setOpaque(false);
			avatarLabel.setAlignmentY(Component.TOP_ALIGNMENT);

			// message area with wrapping
			JTextArea msgArea = new JTextArea("milady" + play + text);
			Font msgFont = (matrix != null) ? matrix.deriveFont(22f) : new Font("Monospaced", Font.PLAIN, 22);
			msgArea.setFont(msgFont);
			msgArea.setForeground(Color.GREEN);
			msgArea.setBackground(Color.BLACK);
			msgArea.setOpaque(true);
			msgArea.setEditable(false);
			msgArea.setLineWrap(true);
			msgArea.setWrapStyleWord(true);
			msgArea.setBorder(null);

			// compute preferred size so height matches text lines
			FontMetrics fm = msgArea.getFontMetrics(msgFont);
			int lineHeight = fm.getHeight();
			int charWidth = fm.charWidth('M');
			int panelWidth = chatPanel.getWidth() > 0 ? chatPanel.getWidth() : 600;
			int maxTextWidth = (int)(panelWidth * 0.6); // message take ~60% space
			msgArea.setColumns(Math.max(10, maxTextWidth / Math.max(1, charWidth)));
			int textLines = (int)Math.ceil((double)msgArea.getText().length() / Math.max(1, msgArea.getColumns()));
			msgArea.setRows(Math.max(1, textLines));
			msgArea.setSize(new Dimension(maxTextWidth, lineHeight * msgArea.getRows() + 4));
			msgArea.setPreferredSize(msgArea.getSize());

			// timestamp label
			JLabel timeLabel = new JLabel("[" + timestamp + "]");
			Font timeFont = (matrix != null) ? matrix.deriveFont(14f) : new Font("Monospaced", Font.PLAIN, 14);
			timeLabel.setFont(timeFont);
			timeLabel.setForeground(Color.GREEN);
			timeLabel.setBackground(Color.BLACK);
			timeLabel.setOpaque(false);
			timeLabel.setAlignmentY(Component.TOP_ALIGNMENT);

			// row layout
			JPanel row = new JPanel(new GridBagLayout());
			row.setBackground(Color.BLACK);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(2, 8, 2, 8);

			// avatar col
			gbc.gridx = 0;
			gbc.weightx = 0.0;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			row.add(avatarLabel, gbc);

			// message col (wrap, fill width)
			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.weightx = 1.0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.WEST;
			row.add(msgArea, gbc);

			// time col
			gbc = new GridBagConstraints();
			gbc.gridx = 2;
			gbc.weightx = 0.0;
			gbc.anchor = GridBagConstraints.NORTHEAST;
			gbc.insets = new Insets(2, 8, 2, 8);
			row.add(timeLabel, gbc);

			// ensure row won't stretch to fill entire viewport when there are few messages
			row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
			row.setAlignmentX(Component.LEFT_ALIGNMENT);

			// clickable for audio replay - use the bubble instance below
			// We'll create bubble first, then attach mouse listener referencing it.

			Bubble bubble = new Bubble(row, msgArea, audioFile, text);

			msgArea.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			msgArea.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (bubble.audio != null && bubble.audio.exists()) {
						// manual replay shouldn't enqueue duplicate UI bubble, so just play the file directly
						new Thread(() -> playAudio(bubble), "LanvoilaPlay").start();
					} else {
						// if no audio present, synthesize and enqueue normally
						new Thread(() -> sendTTS(text), "LanvoilaReplay").start();
					}
				}
			});

			// add row to chat panel
			chatPanel.add(row);
			chatPanel.add(Box.createVerticalStrut(6));
			chatPanel.revalidate();

			// scroll to bottom
			chatScroll.getVerticalScrollBar().setValue(chatScroll.getVerticalScrollBar().getMaximum());

			result[0] = bubble;
		};

		try {
			SwingUtilities.invokeAndWait(r);
		} catch (Exception e) {
			// fallback: run on EDT asynchronously and block briefly (shouldn't happen normally)
			e.printStackTrace();
			SwingUtilities.invokeLater(r);
			try { Thread.sleep(50); } catch (InterruptedException ignored) {}
		}

		return result[0];
	}

	private void playNextBubble() {
		Bubble bubble;
		synchronized (playbackQueue) {
			bubble = playbackQueue.poll();
			if (bubble == null) {
				isPlaying = false;
				return;
			}
			isPlaying = true;
		}

		// invert colors for active bubble
		SwingUtilities.invokeLater(() -> {
			bubble.msgArea.setBackground(Color.GREEN);
			bubble.msgArea.setForeground(Color.BLACK);
		});

		if (bubble.audio != null && bubble.audio.exists()) {
			playAudio(bubble);
		} else {
			// no audio yet, synthesize
			new Thread(() -> sendTTS(bubble.text), "LanvoilaTTSWorker").start();
			// when TTS finishes, it enqueues a new bubble — playback resumes automatically
			isPlaying = false;
		}
	}

	private void themeScrollBar(JScrollPane scroll) {
		scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = new Color(0, 255, 70); // neon green
				this.trackColor = Color.BLACK;
			}

			@Override
			protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(0, 255, 70, 180)); // translucent green
				g2.fillRoundRect(r.x, r.y, r.width, r.height, 12, 12);
				g2.setColor(new Color(0, 255, 70));
				g2.drawRoundRect(r.x, r.y, r.width-1, r.height-1, 12, 12);
				g2.dispose();
			}

			@Override
			protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setColor(Color.BLACK);
				g2.fillRect(r.x, r.y, r.width, r.height);
				g2.dispose();
			}
		});

		scroll.getVerticalScrollBar().setPreferredSize(new Dimension(12, Integer.MAX_VALUE));
	}

	private void playAudio(File file) {
		Bubble fake = new Bubble(null, null, file, "<manual replay>");
		playAudio(fake);
	}

	// ---------- Playback ----------
    private void playAudio(Bubble bubble) {
		// run streaming playback in worker thread; ensure we block that thread until audio finishes
		new Thread(() -> {
			AudioInputStream in = null;
			SourceDataLine line = null;
			try {
				System.out.println("[Lanvoila] open audio: " + (bubble.audio != null ? bubble.audio.getAbsolutePath() : "null"));
				in = AudioSystem.getAudioInputStream(bubble.audio);
				AudioFormat baseFormat = in.getFormat();

				// Convert to PCM_SIGNED if needed
				AudioFormat decodedFormat = baseFormat;
				if (baseFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
					decodedFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(),
						16,
						baseFormat.getChannels(),
						baseFormat.getChannels() * 2,
						baseFormat.getSampleRate(),
						false
					);
					in = AudioSystem.getAudioInputStream(decodedFormat, in);
				}

				AudioFormat playFormat = in.getFormat();
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, playFormat);
				if (!AudioSystem.isLineSupported(info)) {
					// try fallback by converting to a common format (16bit PCM, stereo)
					AudioFormat fallback = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						playFormat.getSampleRate(),
						16,
						Math.max(1, playFormat.getChannels()),
						Math.max(1, playFormat.getChannels()) * 2,
						playFormat.getSampleRate(),
						false
					);
					in = AudioSystem.getAudioInputStream(fallback, in);
					playFormat = in.getFormat();
					info = new DataLine.Info(SourceDataLine.class, playFormat);
				}

				line = (SourceDataLine) AudioSystem.getLine(info);
				line.open(playFormat);
				line.start();

				System.out.println("[Lanvoila] Playing: " + bubble.audio.getName());

				// invert colors (UI)
				if (bubble.msgArea != null) {
					SwingUtilities.invokeLater(() -> {
						bubble.msgArea.setBackground(Color.GREEN);
						bubble.msgArea.setForeground(Color.BLACK);
					});
				}

				byte[] buffer = new byte[4096];
				int n;
				while ((n = in.read(buffer, 0, buffer.length)) != -1) {
					line.write(buffer, 0, n);
				}

				// drain and stop
				line.drain();
				line.stop();
				line.close();
				in.close();

				System.out.println("[Lanvoila] Finished: " + bubble.audio.getName());

				// restore colors
				if (bubble.msgArea != null) {
					SwingUtilities.invokeLater(() -> {
						bubble.msgArea.setBackground(Color.BLACK);
						bubble.msgArea.setForeground(Color.GREEN);
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (bubble.msgArea != null) {
					SwingUtilities.invokeLater(() -> {
						bubble.msgArea.setBackground(Color.RED);
						bubble.msgArea.setForeground(Color.BLACK);
					});
				}
			} finally {
				try { if (in != null) in.close(); } catch (Exception ignored) {}
				if (line != null && line.isOpen()) {
					try { line.stop(); line.close(); } catch (Exception ignored) {}
				}

				// continue queue
				synchronized (playbackQueue) {
					isPlaying = false;
				}
				// small yield to avoid re-entrancy weirdness
				SwingUtilities.invokeLater(this::playNextBubble);
			}
		}, "LanvoilaPlayAudio").start();
	}


    // ---------- HTTP to /tts ----------
    private void sendTTS(String text) {
		try {
			URL url = new URL("http://localhost:5000/tts");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");

			String payload = "{\"text\":\"" + text.replace("\"", "\\\"") + "\"}";
			try (OutputStream os = conn.getOutputStream()) {
				os.write(payload.getBytes(StandardCharsets.UTF_8));
			}

			if (conn.getResponseCode() == 200) {
				File out = File.createTempFile("lanvoila_", ".wav");
				try (InputStream is = conn.getInputStream();
					FileOutputStream fos = new FileOutputStream(out)) {
					byte[] buf = new byte[4096];
					int r;
					while ((r = is.read(buf)) != -1) fos.write(buf, 0, r);
				}
				script.add(new ScriptLine(text, out));
				enqueueBubble(text, out);  // <-- enqueue instead of play immediately
			} else {
				enqueueBubble("HTTP " + conn.getResponseCode() + " from /tts", null);
			}
		} catch (Exception e) {
			enqueueBubble("Error: " + e.getMessage(), null);
			e.printStackTrace();
		}
	}


    public JPanel getPanel() { return panel; }
}

class LanvoilaServer {
    private static Process proc;
    private static final int PORT = 5000;

    public static void start() {
        if (proc != null && proc.isAlive()) return;

        try {
            File script = findScript();
            String python = findPython();

            ProcessBuilder pb = new ProcessBuilder(python, script.getName());
            pb.directory(script.getParentFile());                 // <— run in the script’s folder
            pb.environment().put("PYTHONUNBUFFERED", "1");        // <— flush logs
            proc = pb.start();

            // Drain logs so the process can’t block and so you can SEE errors
            new Thread(() -> drain(proc.getInputStream(),  "[py] " ), "lanvoila-stdout").start();
            new Thread(() -> drain(proc.getErrorStream(),  "[pyE] "), "lanvoila-stderr").start();

            waitUntilPortOpens("127.0.0.1", PORT, 8000);
            System.out.println("[Lanvoila] HTTP server ready on 127.0.0.1:" + PORT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to start Lanvoila server", e);
        }
    }

    public static void stop() {
        if (proc != null && proc.isAlive()) {
            proc.destroy();
            System.out.println("[Lanvoila] server stopped");
        }
    }

    // ---------- helpers ----------
    private static String findPython() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win") ? "python" : "python3";
    }

    private static File findScript() {
        String[] candidates = {
            "Server/lanvoila.py",
            "./Server/lanvoila.py",
            "../Server/lanvoila.py",
            "lanvoila.py"
        };
        for (String c : candidates) {
            File f = new File(c).getAbsoluteFile();
            if (f.exists()) return f;
        }
        throw new IllegalStateException(
            "lanvoila.py not found. Working dir: " + new File(".").getAbsolutePath());
    }

    private static void drain(InputStream is, String prefix) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) System.out.println(prefix + line);
        } catch (IOException ignored) {}
    }

    private static void waitUntilPortOpens(String host, int port, int timeoutMs) throws InterruptedException {
        long end = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < end) {
            try (Socket s = new Socket()) {
                s.connect(new InetSocketAddress(host, port), 200);
                return; // success
            } catch (IOException ignored) {}
            Thread.sleep(100);
        }
        System.err.println("[Lanvoila] server did not open port " + port + " in time.");
    }
}

