import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
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
			fileMenu.add(addLanvoilaMenuItem);
			fileMenu.add(exitMenuItem);
			newMenuItem.addActionListener(menuHandler);
			openMenuItem.addActionListener(menuHandler);
			closeMenuItem.addActionListener(menuHandler);
			saveMenuItem.addActionListener(menuHandler);
			saveasMenuItem.addActionListener(menuHandler);
			addLerminalMenuItem.addActionListener(menuHandler);
			addLanvoilaMenuItem.addActionListener(menuHandler);
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
			addLanvoilaMenuItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)
			);
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
			frame.setSize(600,600);
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

	public void AddLanvoila(){
		Lanvoila lv = new Lanvoila();
		lv.setStyle(matrix, Color.BLACK, Color.GREEN);
		JPanel lvPanel = lv.getPanel();
		pane.addTab("Lanvoila", lvPanel);
	}
	
	public void RemoveTab(){
		int length = pane.getTabCount();
		if(length > 0){
			int paneIndex = pane.getSelectedIndex();
			Component comp = pane.getComponentAt(paneIndex); 
			String paneName = pane.getTitleAt(paneIndex);

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
        Lanedit lanedit = new Lanedit();
		//lanedit.prompt();
		lanedit.AddTab("Lanedit","");
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
			else if(menuOption.equalsIgnoreCase("add lanvoila")){
				AddLanvoila();
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
    private Process serverProcess;
    private JPanel panel;
    private JTextArea inputArea;
    private JPanel chatPanel;
    private JScrollPane chatScroll;

	private static class ScriptLine {
		String text;
		File audio;
		ScriptLine(String text, File audio) {
			this.text = text; this.audio = audio;
		}
	}

	private final java.util.List<ScriptLine> script = new ArrayList<>();


    public Lanvoila() {
        startServerIfNeeded();
        buildUI();
    }

    // ---------- server boot ----------
    private boolean isServerListening() {
        try (java.net.Socket s = new java.net.Socket("localhost", 5000)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void waitUntilUp(int attempts, int sleepMs) {
        for (int i = 0; i < attempts; i++) {
            if (isServerListening()) return;
            try { Thread.sleep(sleepMs); } catch (InterruptedException ignored) {}
        }
    }

    private void startServerIfNeeded() {
        if (isServerListening()) {
            System.out.println("[Lanvoila] Python server already running on :5000");
            return;
        }
        try {
            ProcessBuilder builder = new ProcessBuilder(
                "python3", "../Server/lanvoila.py"
            );
            builder.redirectErrorStream(true);
            serverProcess = builder.start();

            // pipe server logs to stdout
            new Thread(() -> {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(serverProcess.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println("[Lanvoila] " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, "LanvoilaServerLog").start();

            // give it a moment to bind the port
            waitUntilUp(80, 100); // up to ~8s
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- UI ----------
    private void buildUI() {
        panel = new JPanel(new BorderLayout());

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatScroll = new JScrollPane(chatPanel);
        chatScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        inputArea = new JTextArea(4, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);

        // SHIFT+ENTER = send TTS (non-blocking)
        InputMap im = inputArea.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = inputArea.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK), "sendTTS");
        am.put("sendTTS", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputArea.getText().trim();
                if (!text.isEmpty()) {
                    // addChatBubble("You: " + escapeHtml(text));
                    inputArea.setText("");
                    new Thread(() -> sendTTS(text), "LanvoilaTTS").start();
                }
            }
        });

        panel.add(chatScroll, BorderLayout.CENTER);
        panel.add(new JScrollPane(inputArea), BorderLayout.SOUTH);
    }

    public void setStyle(Font font, Color bg, Color fg){
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

    private static String escapeHtml(String s){
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    private void addChatBubble(String message) {
        JLabel label = new JLabel("<html><body style='width: 360px'>" + message + "</body></html>");
        label.setOpaque(true);
        // matrix-y look: dark bubble with green text
        label.setBackground(new Color(0, 32, 0));
        label.setForeground(Color.GREEN);
        label.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        chatPanel.add(label);
        chatPanel.revalidate();
        SwingUtilities.invokeLater(() ->
            chatScroll.getVerticalScrollBar().setValue(chatScroll.getVerticalScrollBar().getMaximum())
        );
    }

	private void playAudio(File wav){
		try { new ProcessBuilder("aplay", wav.getAbsolutePath()).start(); } catch (Exception ignore) {}
	}

    // ---------- HTTP to /tts ----------
    private void sendTTS(String text) {
		try {
			if (!isServerListening()) {
				addChatBubble("Lanvoila server not reachable on :5000");
				return;
			}
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
				script.add(new ScriptLine(text, out)); // save line + file
				addChatBubble("milady ▶ " + escapeHtml(text));
				playAudio(out);
			} else {
				addChatBubble("HTTP " + conn.getResponseCode() + " from /tts");
			}
		} catch (Exception e) {
			addChatBubble("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void replayUntil(int idx){
		for(int i=0;i<=idx && i<script.size();i++){
			ScriptLine line = script.get(i);
			addChatBubble("Replay ▶ " + line.text);
			playAudio(line.audio);
			try { Thread.sleep(500); } catch(Exception ignored){} // pacing
		}
	}

    public JPanel getPanel() { return panel; }

    public void stop() {
        if (serverProcess != null) {
            serverProcess.destroy();
            serverProcess = null;
        }
    }
}

