import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class HelpScreen {

	private JFrame helpFrame;

	public static void startHelpScreen(){
		HelpScreen window = new HelpScreen();
		window.helpFrame.setVisible(true);
	}

	public HelpScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		helpFrame = new JFrame();
		helpFrame.getContentPane().setBackground(Color.GRAY);
		helpFrame.getContentPane().setLayout(null);

		ImageIcon cherries = new ImageIcon("cherries.jpg");
		Image temp = cherries.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		cherries = new ImageIcon(temp);
		JLabel imageCherry = new JLabel(cherries);
		imageCherry.setBounds(10, 11, 73, 73);
		helpFrame.getContentPane().add(imageCherry);

		JLabel imageCherry2 = new JLabel(cherries);
		imageCherry2.setBounds(351, 11, 73, 73);
		helpFrame.getContentPane().add(imageCherry2);

		JLabel label = new JLabel("Cherry Picking!");
		label.setFont(new Font("Lithos Pro Regular", Font.ITALIC, 31));
		label.setBounds(82, 11, 270, 73);
		helpFrame.getContentPane().add(label);

		JTextArea txtrInstructions = new JTextArea();
		txtrInstructions.setLineWrap(true);
		txtrInstructions.setWrapStyleWord(true);
		txtrInstructions.setText("Instructions:\r\nFor groups of 3-8. Users are each given 6 white cards containing nouns on them. For each round, the user must select a white card that they feel is best suited to the black card displayed. The czar within the group will select the winner of the round. First player to reach 10 points wins. ");
		txtrInstructions.setBackground(Color.LIGHT_GRAY);
		txtrInstructions.setBounds(20, 95, 393, 143);
		helpFrame.getContentPane().add(txtrInstructions);

		helpFrame.setBounds(100, 100, 450, 300);
		helpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
