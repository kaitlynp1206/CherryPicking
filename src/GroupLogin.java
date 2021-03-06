import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * GroupLogin.java
 * @author Kaitlyn Paglia
 * @version 1.0
 * 1/23/2017
 * Obtains and authenticates groupName - GUI
 */
public class GroupLogin extends Client {

	private static JFrame groupFrame;
	private JTextField groupNameLabel;
	private static String groupName;
	private static boolean authenticated = false;

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
	 * Launch the application.
	 */
	public static void startGroupScreen(){
		GroupLogin window = new GroupLogin();
		groupFrame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public GroupLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		groupFrame = new JFrame();
		groupFrame.getContentPane().setBackground(Color.GRAY);
		groupFrame.getContentPane().setLayout(null);
		groupFrame.setResizable(false);

		JLabel titleLabel = new JLabel("Cherry Picking!");
		titleLabel.setFont(new Font("Lithos Pro Regular", Font.ITALIC, 31));
		titleLabel.setBounds(57, 145, 270, 73);
		groupFrame.getContentPane().add(titleLabel);

		JButton newGameButton = new JButton("NEW GAME");
		newGameButton.setBounds(69, 270, 118, 23);
		groupFrame.getContentPane().add(newGameButton);

		JButton joinGameButton = new JButton("JOIN GAME");
		joinGameButton.setBounds(196, 270, 118, 23);
		groupFrame.getContentPane().add(joinGameButton);

		ImageIcon cherries = new ImageIcon("cherries.jpg");
		Image temp = cherries.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
		cherries = new ImageIcon(temp);
		JLabel imageCherry = new JLabel(cherries);
		imageCherry.setBounds(132, 30, 120, 120);
		groupFrame.getContentPane().add(imageCherry);

		groupNameLabel = new JTextField("GROUP NAME");
		groupNameLabel.setBounds(69, 217, 245, 28);
		groupNameLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				groupNameLabel.setText("");
			}
		});
		groupFrame.getContentPane().add(groupNameLabel);

		JLabel errorLabel = new JLabel("");
		errorLabel.setForeground(Color.RED);
		errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		errorLabel.setBounds(110, 303, 264, 23);
		groupFrame.getContentPane().add(errorLabel);
		groupFrame.setBounds(100, 100, 400, 400);
		groupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		newGameButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				groupName = groupNameLabel.getText().trim();
				setTempMessage(groupName);
				setGameSelection(1);
				startRun();
				while(!getRun()){ //allow for client to get to if statement after try&catch
				}
				try {
					if (getAuthenticateGroupName()){
						setGroupName(groupName);
						authenticated=true;
						}
				} catch (Exception error){
					error.printStackTrace();
				}
				if (authenticated){
					System.out.println("Good1");
					close();
				} else{
					errorLabel.setText("Error: Group name already exists");
					System.out.println("Error: Group name already exists");
				}
				stopRun();
				stop();
			}
		});

		joinGameButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				groupName = groupNameLabel.getText().trim();
				setTempMessage(groupName);
				setGameSelection(2);
				startRun();
				while(!getRun()){ //allow for client to get to if statement after try&catch
				}
				try {
					if (getAuthenticateGroupName()){
						setGroupName(groupName);
						authenticated=true;
											}
				} catch (Exception error){
					error.printStackTrace();
				}
				if (authenticated){
					System.out.println("Good2");
					close();
				} else {
					errorLabel.setText("Error: Group name not found");
					System.out.println("Error: Group name not found");
				}
				stopRun();
				stop();
			}
		});
	}

	/**
	 * exit
	 * closes frame after exit button is clicked
	 */
	public static void exit(){
		groupFrame.dispose();
	}

	/**
	 * close
	 * closes frame after start button is clicked
	 */
	public static void close() {
		groupFrame.dispose();
		GameScreen.startGameScreen();

	}

}
