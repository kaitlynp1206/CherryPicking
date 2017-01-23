import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * HelpScreen.java
 * @author Kaitlyn Paglia
 * @version 1.0
 * 1/23/2017
 * Displays instructions/help
 */
public class HelpScreen {

	private static JFrame helpFrame;

	/**
	 * startHelpScreen
	 * Creates window/frame
	 */
	public static void startHelpScreen(){
		helpFrame.setVisible(true);
	}

	/**
	 * HelpScreen constructor
	 */
	public HelpScreen() {
		initialize();
	}
	
	/**
	 * Window Listener
	 * set only current frame visibility to false when closing
	 */
	private static WindowListener windowListener = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			helpFrame.setVisible(false);
		}
	};

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		helpFrame = new JFrame();
		helpFrame.getContentPane().setBackground(Color.GRAY);
		helpFrame.getContentPane().setLayout(null);

		//creates images of cherries display
		ImageIcon cherries = new ImageIcon("cherries.jpg");
		Image temp = cherries.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		cherries = new ImageIcon(temp);
		JLabel imageCherry = new JLabel(cherries);
		imageCherry.setBounds(10, 11, 73, 73);
		helpFrame.getContentPane().add(imageCherry);
		JLabel imageCherry2 = new JLabel(cherries);
		imageCherry2.setBounds(351, 11, 73, 73);
		helpFrame.getContentPane().add(imageCherry2);

		//screen title label
		JLabel label = new JLabel("Cherry Picking!");
		label.setFont(new Font("Lithos Pro Regular", Font.ITALIC, 31));
		label.setBounds(82, 11, 270, 73);
		helpFrame.getContentPane().add(label);

		//add instructions
		JTextArea txtrInstructions = new JTextArea();
		txtrInstructions.setLineWrap(true);
		txtrInstructions.setWrapStyleWord(true);
		txtrInstructions.setText("Instructions:\r\nFor groups of 3-8. Users are each given 6 white cards containing nouns on them. For each round, the user must select a white card that they feel is best suited to the black card displayed. The czar within the group will select the winner of the round. First player to reach 10 points wins. Once game has started, no additional players will be able to enter the game. ");
		txtrInstructions.setBackground(Color.LIGHT_GRAY);
		txtrInstructions.setBounds(20, 95, 393, 143);
		txtrInstructions.setEditable(false);
		helpFrame.getContentPane().add(txtrInstructions);

		helpFrame.setBounds(100, 100, 450, 300);
		helpFrame.setResizable(false);
		helpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
