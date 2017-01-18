import java.awt.EventQueue;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class LoginScreen {

	private static JFrame loginFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen window = new LoginScreen();
					window.loginFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//JFrame
		loginFrame = new JFrame();
		loginFrame.setTitle("Cherry Picking");


		loginFrame.setBounds(100, 100, 450, 400);
		loginFrame.setResizable(false);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
