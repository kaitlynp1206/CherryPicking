import javax.swing.JFrame;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;

/**
 * GameScreen.java
 * @author Kaitlyn Paglia
 * @version 1.0
 * 1/23/2017
 * Displays main game screen of CherryPicking
 */
public class GameScreen extends Client {

	private Font displayFont = new Font("Bodoni MT", Font.ITALIC, 20);
	private static Font cardFont = new Font("Book Antiqua", Font.PLAIN, 11);
	private Font groupNameFont = new Font("Bodoni MT", Font.BOLD, 25);
	public static JFrame frmCherryPicking;
	public static JFrame helpFrame;
	static DefaultListModel<String> defaultList = new DefaultListModel<>();
	static String msg;
	static boolean gameActivated = false;
	static JTextArea handCards[] = new JTextArea[6];
	private static ArrayList<Card> currentHand = new ArrayList<Card>();
	private static JPanel cardsPanel = new JPanel();
	private static JPanel listPanel = new JPanel();
	private static JPanel displayPanel = new JPanel();
	private static JList<String> scoresList = new JList<String>();
	private static ListSelectionModel listModel;
	

	/**
	 * Window Listener
	 * closes screen and exits program
	 */
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
	 * refreshCards
	 * re-does the cardsPanel and sets new text to cards
	 */
	public static void refreshCards(){
		cardsPanel.removeAll();
		cardsPanel.setBackground(Color.DARK_GRAY);
		cardsPanel.setBounds(179, 297, 607, 122);
		cardsPanel.setLayout(null);
		int count = 0;
		for (Card c: hand){
			handCards[count] = new JTextArea(c.getText());
			handCards[count].setLineWrap(true);
			handCards[count].setBounds(10 + 99*count, 11, 89, 100);
			handCards[count].setEditable(false);
			handCards[count].setLineWrap(true);
			handCards[count].setWrapStyleWord(true);
			handCards[count].setFont(cardFont);
			cardsPanel.add(handCards[count]);
			count++;
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
		cardsPanel.revalidate();
		cardsPanel.repaint();
		frmCherryPicking.revalidate();
		frmCherryPicking.repaint();
	}

	/**
	 * updateScoresList
	 */
	public static void updateScoresList(){
		defaultList = new DefaultListModel<>();
		if (players.size()==scores.size()){
			for (int i=0; i<players.size(); i++){
				defaultList.addElement(players.get(i)+": "+scores.get(i));
			}
		}
		
		listPanel.removeAll();
		scoresList=new JList<>(defaultList);
		listModel = scoresList.getSelectionModel();
		//listModel.addListSelectionListener(new ListSelectionHandler());
		
		listPanel.setBounds(10, 104, 160, 128);
		displayPanel.add(listPanel);
		listPanel.setLayout(new BorderLayout(0, 0));
		
		listPanel.add(scoresList, BorderLayout.CENTER);
		scoresList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		scoresList.setLayoutOrientation(JList.VERTICAL_WRAP);
		scoresList.setVisibleRowCount(-1);
		listPanel.revalidate();
		listPanel.repaint();
		displayPanel.revalidate();
		displayPanel.repaint();
	}

	/**
	 * initialize
	 * Initializes the contents of the frame.
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

		//Personal panel - includes username, exit button, and if creator also includes start button
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

		JLabel usernameLabel = new JLabel(getUsername());
		usernameLabel.setForeground(Color.BLACK);
		usernameLabel.setFont(new Font("Bodoni MT", Font.ITALIC, 26));
		usernameLabel.setBounds(10, 11, 185, 20);
		personalPanel.add(usernameLabel);

		if (isCreator()){
			JButton startGameButton = new JButton("Start Game");
			startGameButton.setBounds(375, 11, 106, 23);
			startGameButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.out.println("GS " + Client.start);
					Client.start=true;
					startRun();
					System.out.println("Starting Game");
					stop();
				}
			});
			personalPanel.add(startGameButton);
		}

		//Display Panel - contains scores and chat room
		displayPanel.setBackground(Color.BLACK);
		displayPanel.setBounds(0, 0, 180, 461);
		frmCherryPicking.getContentPane().add(displayPanel);

		//groupName display
		JLabel groupLabel = new JLabel("Group Name:");
		groupLabel.setBounds(10, 11, 110, 20);
		groupLabel.setForeground(Color.WHITE);
		groupLabel.setFont(displayFont);

		JLabel groupNameLabel = new JLabel(getGroupName());
		groupNameLabel.setBounds(10, 37, 160, 30);
		groupNameLabel.setForeground(Color.RED);
		groupNameLabel.setFont(groupNameFont);

		//scoresList display
		JLabel scoresLabel = new JLabel("Scores:");
		scoresLabel.setBounds(10, 73, 110, 25);
		scoresLabel.setForeground(Color.WHITE);
		scoresLabel.setFont(displayFont);

		listPanel.setBounds(10, 104, 160, 128);
		displayPanel.add(listPanel);
		listPanel.setLayout(new BorderLayout(0, 0));
		
		listPanel.add(scoresList, BorderLayout.CENTER);
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

		JTextArea chatDisplay = new JTextArea(); //messages of group are shown
		chatDisplayPanel.add(new JScrollPane(chatDisplay), BorderLayout.CENTER);
		chatDisplay.setText("");
		chatDisplay.setLineWrap(true);
		chatDisplay.setWrapStyleWord(true);
		chatDisplay.setEditable(false);
		chatDisplay.setBackground(Color.WHITE);

		JPanel chatMessagePanel = new JPanel(); 
		chatMessagePanel.setBounds(10, 406, 113, 44);
		displayPanel.add(chatMessagePanel);
		chatMessagePanel.setLayout(new BorderLayout(0, 0));

		JTextArea chatMessage = new JTextArea(); //type new message
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
		displayPanel.add(sendButton);

		//Send Button - adds actionlistener to send message to server and update display
		sendButton.addActionListener(new ActionListener(){ //send message to group chat
			public void actionPerformed(ActionEvent e){
				msg = chatMessage.getText().trim();
				if (msg!=""){
					chatDisplay.append(getUsername() + ": " + msg + "\n");
					System.out.println(msg);
					setMsg("/chat/" + msg);
					chatMessage.setText("");
				}
			}
		});

		//Initialize Cards Panel
		cardsPanel.setBackground(Color.DARK_GRAY);
		cardsPanel.setBounds(179, 297, 607, 122);
		frmCherryPicking.getContentPane().add(cardsPanel);
		cardsPanel.setLayout(null);

		for (int i=0; i<6; i++){
			handCards[i] = new JTextArea();
			handCards[i].setLineWrap(true);
			handCards[i].setBounds(10 + 99*i, 11, 89, 100);
			//handCards[i].setEditable(false);
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

		//Select Panel - contains instruction button and select card option
		JPanel selectPanel = new JPanel();
		selectPanel.setBackground(Color.LIGHT_GRAY);
		selectPanel.setBounds(179, 418, 607, 43);
		frmCherryPicking.getContentPane().add(selectPanel);

		//Select Card Button - sets selected choice of card and sends it to Client to be sent to Server
		JButton selectCardButton = new JButton("Select Card");
		selectCardButton.setBounds(205, 11, 196, 21);
		selectCardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int select=-1;
				for (int i=0; i<6; i++){
					//check for selected card
					if (handCards[i].getBackground()==Color.RED){
						select=i;
					}
					setSelectedCard(currentHand.get(select).getID());
					startRun();
				}
				for (int i=0; i<6; i++){
					handCards[i].setBackground(Color.WHITE);
				}
			}
		});
		selectPanel.setLayout(null);
		selectPanel.add(selectCardButton);

		//Help button - opens up HelpScreen.java to display instructions
		JButton helpButton = new JButton("?");
		helpButton.setBounds(537, 11, 60, 22);
		selectPanel.add(helpButton);
		helpButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				openHelpScreen();
			}
		});

		//Game Panel - display black and results panels 
		JPanel gamePanel = new JPanel();
		gamePanel.setBackground(Color.DARK_GRAY);
		gamePanel.setBounds(179, 0, 607, 256);
		frmCherryPicking.getContentPane().add(gamePanel);
		gamePanel.setLayout(null);
		
		//Black Panel - displays noun cards
		JTextArea blackPanel = new JTextArea();
		blackPanel.setLineWrap(true);
		blackPanel.setText("Waiting for more players...");
		blackPanel.setForeground(Color.RED);
		blackPanel.setBackground(Color.BLACK);
		blackPanel.setBounds(10, 11, 143, 147);
		blackPanel.setEditable(false);
		gamePanel.add(blackPanel);

		//Results Panel - Displays selected cards of game 
		JPanel resultsPanel = new JPanel();
		resultsPanel.setBackground(Color.BLACK);
		resultsPanel.setBounds(163, 11, 434, 234);
		gamePanel.add(resultsPanel);
		resultsPanel.setLayout(null);

		JTextArea displayCards[][] = new JTextArea[2][4];
		for (int i=0; i<2; i++){
			for (int j=0; j<4; j++){
				displayCards[i][j] = new JTextArea();
				displayCards[i][j].setEditable(false);
				displayCards[i][j].setBackground(Color.LIGHT_GRAY);
				displayCards[i][j].setBounds(10+j*107, 11+i*115, 93, 97);
				resultsPanel.add(displayCards[i][j]);
			}
		}
	}

	/**
	 * Exit
	 * Closes frame
	 */
	public static void exit(){
		frmCherryPicking.dispose();
	}

}
