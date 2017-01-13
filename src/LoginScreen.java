import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class LoginScreen {
	
	private static JFrame loginFrame;
	
	public LoginScreen(){
		initialize();
	}
	
	public static void startLogin(){
		LoginScreen window = new LoginScreen();
		loginFrame.setVisible(true);
	}
	
	private void initialize(){
		
		//JFrame
		loginFrame = new JFrame();
		loginFrame.setTitle("Cherry Picking");
		
		//Title Label
		JLabel title = new JLabel("Cherry Picking");
		title.setForeground(Color.BLACK);
		title.setBounds(160, 75,  300,  60);
		
		
		loginFrame.setBounds(100, 100, 450, 400);
		loginFrame.setResizable(false);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

//http://stackoverflow.com/questions/6260855/how-to-clear-reset-a-jframe
