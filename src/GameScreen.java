import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameScreen {

	private Font displayFont = new Font("Bodoni MT", Font.ITALIC, 20);
	private Font groupNameFont = new Font("Bodoni Bd BT", Font.BOLD, 25);
	private Font countdownFont = new Font("Bodoni MT", Font.PLAIN, 35);
	public static JFrame frmCherryPicking;
	static DefaultListModel<String> defaultList = new DefaultListModel<>();
	private static User currentUser;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameScreen window = new GameScreen();
					window.frmCherryPicking.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            close();
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
	 * getCurrentUser
	 * @return User - current user on the client 
	 */
	public static User getCurrentUser(){
		return currentUser;
	}
	
	/**
	 * 
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
		frmCherryPicking.setSize(new Dimension(802, 500));
		frmCherryPicking.setLocationRelativeTo(null);
		frmCherryPicking.setResizable(false);
		frmCherryPicking.addWindowListener(windowListener);
		frmCherryPicking.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCherryPicking.getContentPane().setLayout(null);
		
		
		//initialize the current user
		currentUser = new User();
		
		//initialize display panel - contains scores and chat room
		JPanel displayPanel = new JPanel();
		displayPanel.setBackground(Color.BLACK);
		displayPanel.setBounds(0, 0, 180, 461);
		frmCherryPicking.getContentPane().add(displayPanel);
		
		//groupName display
		JLabel groupLabel = new JLabel("Group Name:");
		groupLabel.setForeground(Color.WHITE);
		groupLabel.setFont(displayFont);
		
		JLabel groupNameLabel = new JLabel("New label");
		groupNameLabel.setForeground(Color.RED);
		groupNameLabel.setFont(groupNameFont);
		
		//scoresList display
		JLabel scoresLabel = new JLabel("Scores:");
		scoresLabel.setForeground(Color.WHITE);
		scoresLabel.setFont(displayFont);
		
		JList scoresList = new JList();
		
		JLabel chatLabel = new JLabel("Chat:");
		chatLabel.setForeground(Color.WHITE);
		chatLabel.setFont(displayFont);
		
		JTextArea chatDisplay = new JTextArea();
		
		JTextArea chatMessage = new JTextArea();
		
		JButton sendButton = new JButton(">");
		GroupLayout gl_displayPanel = new GroupLayout(displayPanel);
		gl_displayPanel.setHorizontalGroup(
			gl_displayPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_displayPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_displayPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_displayPanel.createSequentialGroup()
							.addComponent(groupLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(244))
						.addGroup(gl_displayPanel.createSequentialGroup()
							.addComponent(groupNameLabel)
							.addContainerGap(49, Short.MAX_VALUE))
						.addGroup(gl_displayPanel.createSequentialGroup()
							.addComponent(scoresLabel, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(55, Short.MAX_VALUE))
						.addGroup(gl_displayPanel.createSequentialGroup()
							.addComponent(chatLabel, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(55, Short.MAX_VALUE))
						.addGroup(gl_displayPanel.createSequentialGroup()
							.addGroup(gl_displayPanel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(scoresList, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(chatDisplay, Alignment.LEADING)
								.addGroup(Alignment.LEADING, gl_displayPanel.createSequentialGroup()
									.addComponent(chatMessage, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(sendButton)))
							.addContainerGap(5, Short.MAX_VALUE))))
		);
		gl_displayPanel.setVerticalGroup(
			gl_displayPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_displayPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(groupLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(groupNameLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scoresLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scoresList, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chatLabel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(1)
					.addComponent(chatDisplay, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_displayPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(chatMessage, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
						.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		displayPanel.setLayout(gl_displayPanel);
		
		JPanel cardsPanel = new JPanel();
		cardsPanel.setBackground(Color.DARK_GRAY);
		cardsPanel.setBounds(179, 297, 607, 122);
		frmCherryPicking.getContentPane().add(cardsPanel);
		cardsPanel.setLayout(null);
		
		JButton card1 = new JButton("New button");
		card1.setBackground(Color.RED);
		card1.setBounds(10, 11, 89, 100);
		cardsPanel.add(card1);
		
		JButton card2 = new JButton("New button");
		card2.setBounds(109, 11, 89, 100);
		cardsPanel.add(card2);
		
		JButton card3 = new JButton("New button");
		card3.setBounds(208, 11, 89, 100);
		cardsPanel.add(card3);
		
		JButton card4 = new JButton("New button");
		card4.setBounds(307, 11, 89, 100);
		cardsPanel.add(card4);
		
		JButton card5 = new JButton("New button");
		card5.setBounds(406, 11, 89, 100);
		cardsPanel.add(card5);
		
		JButton card6 = new JButton("New button");
		card6.setBounds(505, 11, 89, 100);
		cardsPanel.add(card6);
		
		JPanel selectPanel = new JPanel();
		selectPanel.setBackground(Color.LIGHT_GRAY);
		selectPanel.setBounds(179, 418, 607, 43);
		frmCherryPicking.getContentPane().add(selectPanel);
		
		JButton selectCardButton = new JButton("Select Card");
		selectCardButton.setBounds(205, 11, 196, 21);
		selectCardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		selectPanel.setLayout(null);
		selectPanel.add(selectCardButton);
		
		JPanel personalPanel = new JPanel();
		personalPanel.setBackground(Color.LIGHT_GRAY);
		personalPanel.setBounds(179, 255, 607, 43);
		frmCherryPicking.getContentPane().add(personalPanel);
		personalPanel.setLayout(null);
		
		JButton exitButton = new JButton("Leave Game");
		exitButton.setBounds(491, 11, 106, 23);
		personalPanel.add(exitButton);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setForeground(Color.BLACK);
		usernameLabel.setFont(new Font("Bodoni MT", Font.ITALIC, 26));
		usernameLabel.setBounds(10, 11, 399, 20);
		personalPanel.add(usernameLabel);
		
		JPanel gamePanel = new JPanel();
		gamePanel.setBackground(Color.DARK_GRAY);
		gamePanel.setBounds(179, 0, 607, 256);
		frmCherryPicking.getContentPane().add(gamePanel);
		gamePanel.setLayout(null);
		
		JPanel blackPanel = new JPanel();
		blackPanel.setBackground(Color.BLACK);
		blackPanel.setBounds(10, 11, 143, 147);
		gamePanel.add(blackPanel);
		
		JPanel resultsPanel = new JPanel();
		resultsPanel.setBackground(Color.BLACK);
		resultsPanel.setBounds(163, 11, 434, 234);
		gamePanel.add(resultsPanel);
		
		JLabel countdownLabel = new JLabel("Countdown:");
		countdownLabel.setForeground(Color.WHITE);
		countdownLabel.setFont(new Font("Bodoni MT", Font.ITALIC, 20));
		countdownLabel.setBounds(10, 169, 110, 20);
		gamePanel.add(countdownLabel);
		
		JLabel timerLabel = new JLabel("00:00:00");
		timerLabel.setForeground(Color.WHITE);
		timerLabel.setFont(new Font("Bodoni MT", Font.PLAIN, 35));
		timerLabel.setBounds(20, 200, 120, 33);
		gamePanel.add(timerLabel);
	}
	
	public static void close(){
		frmCherryPicking.dispose();
	}
}
