import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*CherriesSever.Java
 * Description: Card selection and chat server.
 * The program waits for a minimum of 3 clients to begin the game.
 * Clients may message each other within the group.
 * @author Kaitlyn Paglia & Elizabeth Ip
 * @version 1.0
 */

public class CherriesServer {
	
	ServerSocket serverSock;
	PrintWriter output;
	BufferedReader input;
	boolean running=true;
	
	public static void main (String[]args){
		new CherriesServer().run();
	}
	
	/**
	 * run
	 * Starts the server.
	 */
	public void run(){
		
		System.out.println("running"); //test
		
		try{
			serverSock = new ServerSocket(5000);
			Socket client = serverSock.accept();
			output = new PrintWriter (client.getOutputStream());
			InputStreamReader stream = new InputStreamReader(client.getInputStream());
			input = new BufferedReader(stream);
		}catch(Exception e){
			System.out.println("Error in connection"); //test
			e.printStackTrace();
		}
		
		System.out.println("Client connected"); //test
	}
}
