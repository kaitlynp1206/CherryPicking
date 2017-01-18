import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsernameLogin {

	private static JFrame usernameFrame;
	private JTextField usernameLabel;
	private JButton startButton;
	private JLabel imageCherry;
	private static String username = "";
	private static boolean authenticated = false;
	private JLabel errorLabel;
	private static boolean active = true;
	
	private static WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            exit();
        }
    };
	
	/**
	 * Launch the application.
	 */
	public static void startUserScreen(){
		UsernameLogin window = new UsernameLogin();
		usernameFrame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public UsernameLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		usernameFrame = new JFrame();
		usernameFrame.getContentPane().setBackground(Color.GRAY);
		usernameFrame.getContentPane().setLayout(null);
		
		JLabel titleLabel = new JLabel("Cherry Picking!");
		titleLabel.setBounds(57, 145, 270, 73);
		titleLabel.setFont(new Font("Lithos Pro Regular", Font.ITALIC, 31));
		usernameFrame.getContentPane().add(titleLabel);
		
		usernameLabel = new JTextField("USERNAME");
		usernameLabel.setBounds(69, 217, 245, 28);
		usernameLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				usernameLabel.setText("");
			}
		});
		usernameFrame.getContentPane().add(usernameLabel);
		usernameLabel.setColumns(10);
		
		ImageIcon cherries = new ImageIcon("cherries.jpg");
		Image temp = cherries.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
		cherries = new ImageIcon(temp);
		JLabel imageCherry = new JLabel(cherries);
		imageCherry.setBounds(132, 30, 120, 120);
		
		startButton = new JButton("START!");
		startButton.setBounds(147, 270, 89, 23);
		usernameFrame.getContentPane().add(startButton);
		
		usernameFrame.getContentPane().add(imageCherry);
		
		
		usernameFrame.setBounds(100, 100, 400, 400);
		usernameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				username = usernameLabel.getText();
				try {
					if ((!authenticated) && (Client.authenticateUsername(username))) {
						Client.username = username;
						authenticated = true;
					}
				} catch (Exception error) {
					error.printStackTrace();
				}
				if (authenticated) {
					close();
					Client.authenticatedName = true;
				} else {
					// Error label
					errorLabel = new JLabel("Error: Username has already been taken");
					errorLabel.setForeground(Color.RED);
					errorLabel.setBounds(96, 305, 195, 14);
					usernameFrame.getContentPane().add(errorLabel);
                }
			}
		});
	
	}
	
	public synchronized boolean getStatus(){
		return active;
	}
	
	public static void exit(){
		usernameFrame.dispose();
		active=false;
		Client.clientRunning=false;
	}
	
	public void close() {
		usernameFrame.dispose();
		active=false;
		
	}
}