import javax.swing.JFrame;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;

public class GameScreen {

	private Font displayFont = new Font("Bodoni MT", Font.ITALIC, 20);
	private Font cardFont = new Font("Book Antiqua", Font.PLAIN, 11);
	private Font groupNameFont = new Font("Bodoni MT", Font.BOLD, 25);
	public static JFrame frmCherryPicking;
	public static JFrame helpFrame;
	static DefaultListModel<String> defaultList = new DefaultListModel<>();
	private static boolean active = true;
	static String msg;
	private static int cardSelected=-1;
	static boolean gameActivated = false;

	private static WindowListener windowListener = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			exit();
		}
	};

	/**
	 * GameScreen
	 * Default constructor for GameScreen, initializes GUI
	 */
	public GameScreen() {
		initialize();
	}

	/**
	 * startGameScreen
	 * Launch GUI
	 */
	public static void startGameScreen(){
		GameScreen window = new GameScreen();
		frmCherryPicking.setVisible(true);
	}

	/**
	 * updateScoresList
	 */
	public static void updateScoresList(){
		defaultList = new DefaultListModel<>();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//set JFrame
		frmCherryPicking = new JFrame();
		frmCherryPicking.setTitle("Cherry Picking");
		frmCherryPicking.setSize(new Dimension(792, 486));
		frmCherryPicking.setLocationRelativeTo(null);
		frmCherryPicking.setResizable(false);
		frmCherryPicking.addWindowListener(windowListener);
		frmCherryPicking.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCherryPicking.getContentPane().setLayout(null);

		//initialize display panel - contains scores and chat room
		JPanel displayPanel = new JPanel();
		displayPanel.setBackground(Color.BLACK);
		displayPanel.setBounds(0, 0, 180, 461);
		frmCherryPicking.getContentPane().add(displayPanel);

		//groupName display
		JLabel groupLabel = new JLabel("Group Name:");
		groupLabel.setBounds(10, 11, 110, 20);
		groupLabel.setForeground(Color.WHITE);
		groupLabel.setFont(displayFont);

		JLabel groupNameLabel = new JLabel(Client.groupName);
		groupNameLabel.setBounds(10, 37, 160, 30);
		groupNameLabel.setForeground(Color.RED);
		groupNameLabel.setFont(groupNameFont);

		//scoresList display
		JLabel scoresLabel = new JLabel("Scores:");
		scoresLabel.setBounds(10, 73, 110, 25);
		scoresLabel.setForeground(Color.WHITE);
		scoresLabel.setFont(displayFont);

		JList scoresList = new JList();
		scoresList.setBounds(10, 104, 160, 128);
		scoresList.setLayoutOrientation(JList.VERTICAL_WRAP);
		scoresList.setVisibleRowCount(-1);

		//chat room display
		JLabel chatLabel = new JLabel("Chat:");
		chatLabel.setBounds(10, 238, 110, 25);
		chatLabel.setForeground(Color.WHITE);
		chatLabel.setFont(displayFont);

		JPanel chatDisplayPanel = new JPanel();
		chatDisplayPanel.setBounds(10, 264, 160, 131);
		displayPanel.add(chatDisplayPanel);
		chatDisplayPanel.setLayout(new BorderLayout(0, 0));

		JTextArea chatDisplay = new JTextArea();
		chatDisplayPanel.add(new JScrollPane(chatDisplay), BorderLayout.CENTER);
		chatDisplay.setText("");
		chatDisplay.setLineWrap(true);
		chatDisplay.setWrapStyleWord(true);
		chatDisplay.setBackground(Color.WHITE);

		JPanel chatMessagePanel = new JPanel();
		chatMessagePanel.setBounds(10, 406, 113, 44);
		displayPanel.add(chatMessagePanel);
		chatMessagePanel.setLayout(new BorderLayout(0, 0));

		JTextArea chatMessage = new JTextArea();
		chatMessagePanel.add(new JScrollPane(chatMessage), BorderLayout.CENTER);
		chatMessage.setText("");
		chatMessage.setLineWrap(true);
		chatMessage.setWrapStyleWord(true);
		chatMessage.setBackground(Color.WHITE);

		JButton sendButton = new JButton(">");
		sendButton.setBounds(129, 406, 41, 44);

		displayPanel.setLayout(null);
		displayPanel.add(groupLabel);
		displayPanel.add(groupNameLabel);
		displayPanel.add(scoresLabel);
		displayPanel.add(chatLabel);
		displayPanel.add(scoresList);
		displayPanel.add(sendButton);

		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				msg = chatMessage.getText().trim();
				if (msg!=""){
					chatDisplay.append(Client.username + ": " + msg + "\n");
					System.out.println(msg);
					chatMessage.setText("");
				}
			}
		});

		//create and display cards
		JPanel cardsPanel = new JPanel();
		cardsPanel.setBackground(Color.DARK_GRAY);
		cardsPanel.setBounds(179, 297, 607, 122);
		frmCherryPicking.getContentPane().add(cardsPanel);
		cardsPanel.setLayout(null);

		JTextArea handCards[] = new JTextArea[6];
		for (int i=0; i<6; i++){
			handCards[i] = new JTextArea();
			handCards[i].setLineWrap(true);
			handCards[i].setBounds(10 + 99*i, 11, 89, 100);
			handCards[i].setEditable(false);
			handCards[i].setLineWrap(true);
			handCards[i].setWrapStyleWord(true);
			handCards[i].setFont(cardFont);
			cardsPanel.add(handCards[i]);

		}
		for (JTextArea cards : handCards){
			cards.addMouseListener(
					new MouseAdapter(){
						public void mousePressed(MouseEvent e){
							for (int i=0; i<6; i++){
								handCards[i].setBackground(Color.WHITE);
							}
							cards.setBackground(Color.RED);
							System.out.println(cards.getBackground());
						}
					});
		}

		JPanel selectPanel = new JPanel();
		selectPanel.setBackground(Color.LIGHT_GRAY);
		selectPanel.setBounds(179, 418, 607, 43);
		frmCherryPicking.getContentPane().add(selectPanel);

		JButton selectCardButton = new JButton("Select Card");
		selectCardButton.setBounds(205, 11, 196, 21);
		selectCardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i=0; i<6; i++){
					//check for selected card
					if (handCards[i].getBackground()==Color.RED){
						cardSelected=i;
					}else{//card not selected
						cardSelected=-1;
					}
				}

				if (cardSelected!=-1){

				}
			}
		});
		selectPanel.setLayout(null);
		selectPanel.add(selectCardButton);

		JButton helpButton = new JButton("?");
		helpButton.setBounds(560, 10, 37, 23);
		selectPanel.add(helpButton);
		helpButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				HelpScreen helpScreen = new HelpScreen();
		        helpScreen.startHelpScreen();
			}
		});

		JPanel personalPanel = new JPanel();
		personalPanel.setBackground(Color.LIGHT_GRAY);
		personalPanel.setBounds(179, 255, 607, 43);
		frmCherryPicking.getContentPane().add(personalPanel);
		personalPanel.setLayout(null);

		JButton exitButton = new JButton("Leave Game");
		exitButton.setBounds(491, 11, 106, 23);
		exitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				exit();
			}
		});
		personalPanel.add(exitButton);

		JLabel usernameLabel = new JLabel(Client.username);
		usernameLabel.setForeground(Color.BLACK);
		usernameLabel.setFont(new Font("Bodoni MT", Font.ITALIC, 26));
		usernameLabel.setBounds(10, 11, 185, 20);
		personalPanel.add(usernameLabel);

		if (GroupLogin.gameMaker==true){
			JButton startGameButton = new JButton("Start Game");
			startGameButton.setBounds(375, 11, 106, 23);
			startGameButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if (Game.numPlayers>3){
						System.out.println("Starting Game");
						gameActivated = true;
					}
				}
			});
			personalPanel.add(startGameButton);
		}

		JPanel gamePanel = new JPanel();
		gamePanel.setBackground(Color.DARK_GRAY);
		gamePanel.setBounds(179, 0, 607, 256);
		frmCherryPicking.getContentPane().add(gamePanel);
		gamePanel.setLayout(null);

		JTextArea blackPanel = new JTextArea();
		blackPanel.setLineWrap(true);
		blackPanel.setText("Waiting for more players...");
		blackPanel.setForeground(Color.RED);
		blackPanel.setBackground(Color.BLACK);
		blackPanel.setBounds(10, 11, 143, 147);
		gamePanel.add(blackPanel);

		JPanel resultsPanel = new JPanel();
		resultsPanel.setBackground(Color.BLACK);
		resultsPanel.setBounds(163, 11, 434, 234);
		gamePanel.add(resultsPanel);
		resultsPanel.setLayout(null);

		JTextArea displayCards[][] = new JTextArea[2][4];
		for (int i=0; i<2; i++){
			for (int j=0; j<4; j++){
				displayCards[i][j] = new JTextArea();
				displayCards[i][j].setBackground(Color.LIGHT_GRAY);
				displayCards[i][j].setBounds(10+j*107, 11+i*115, 93, 97);
				resultsPanel.add(displayCards[i][j]);
			}
		}

		JLabel countdownLabel = new JLabel("Countdown:");
		countdownLabel.setForeground(Color.WHITE);
		countdownLabel.setFont(new Font("Bodoni MT", Font.ITALIC, 22));
		countdownLabel.setBounds(10, 169, 110, 20);
		gamePanel.add(countdownLabel);

		JLabel timerLabel = new JLabel("00:00:00");
		timerLabel.setForeground(Color.WHITE);
		timerLabel.setFont(new Font("Bodoni MT", Font.PLAIN, 35));
		timerLabel.setBounds(20, 200, 120, 33);
		gamePanel.add(timerLabel);
	}


	public static void exit(){
		frmCherryPicking.dispose();
		active=false;
		Client.clientRunning=false;
	}

	public synchronized boolean getStatus(){
		return active;
	}
}
