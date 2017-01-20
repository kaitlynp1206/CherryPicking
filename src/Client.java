import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Elizabeth Ip on 2017-01-08.
 */
public class Client {
	private static Socket socket;
	private static PrintWriter writer;
	private static BufferedReader reader;
	private static BufferedReader consoleReader;

	private static String msg = "";
	private String tempMessage;
	private boolean start=false;
	private String username;
	private String groupName;
	private boolean authenticatedName = false;
	private boolean authenticatedGroup = false;
	private int gameSelection=0;



	public static void main (String args[]){
		new Client().go();
	}

	public void go(){
		String msg="";

		try {
			socket = new Socket("localhost", 6666);//start up a socket
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //make reader and writer
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);//AUTOFLUSH IS MY SAVIOUR i hate flushing

			ServerConnectionThread t=new ServerConnectionThread();//create a new thread to handle client/server messages
			t.start();//start the stupid thing

			/* this fragment constantly checks for user input and sends it to the server but i don't want that rn
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while((msg=consoleReader.readLine())!=null) {//write to socket
                writer.println(msg);
            }
			 */

		}catch(Exception e){
			e.printStackTrace();
		}
		/*Username Screen
      		UsernameLogin userLogin = new UsernameLogin();
      		userLogin.startUserScreen();
      		while (userLogin.getStatus()){
      		}

      		//GroupName Screen
      		GroupLogin groupLogin = new GroupLogin();
      		groupLogin.startGroupScreen();
      		while (groupLogin.getStatus()){
      		}

      		//GameScreen
      		GameScreen gameDisplay = new GameScreen();
      		gameDisplay.startGameScreen();
      		while (gameDisplay.getStatus()){
      		}
		 */
	}

	class ServerConnectionThread extends Thread{
		boolean loggedIn=false;
		private boolean playing;

		//constructor
		ServerConnectionThread(){
			while(!loggedIn) {
				try {
					while (reader.ready()){
						msg=reader.readLine();
						if(msg.contains("/send username/")){
							while(start==false){	
							}
							writer.println("/username check/"+tempMessage);
						}else if(msg.contains("/g2g/")){//prompt for game name after username passes
							authenticatedName=true;
							loggedIn=true;
						}else if(msg.contains("/no go on the nameroo, jer/")){
							while(start==false){	
							}
							writer.println("/username check/"+tempMessage);
						}
						start=false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		//runs the client side of every game
		public void run(){

			try {
				while(!playing){//while the user is not in any game, prompt to join or create one
					while(reader.ready()){
						msg=reader.readLine();
						System.out.println("/msg/"+msg);


						if(msg.contains("/send game name/") || msg.contains("/game name invalid/")){//if prompted to send game name
							if(msg.contains("/game name invalid/")){
								System.out.println("that name is invalid. try again.");//tell the user what was wrong with their previous game name attempt
							}

							while(start==false){	
							}
							if(gameSelection==1){
								writer.println("/new game/game name check/" + tempMessage);//send the proposed game name to the server

							}else if(gameSelection==2){
								writer.println("/join game/game name check/"+ tempMessage);
							}

						}else if(msg.contains("/game name okay/")){//if user is able to join game/create a new one
							playing=true;//allow user to move on
							authenticatedGroup=true;
						}
						start=false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean getAuthenticateUsername(){
		return authenticatedName;
	}

	public boolean getAuthenticateGroupName(){
		return authenticatedGroup;
	}
	
	public void setTempMessage(String tempMessage){
		this.tempMessage=tempMessage;
	}
	
	public void setUsername(String username){
		this.username=username;
	}
	
	public void setGroupName(String groupName){
		this.groupName=groupName;
	}
	
	public void startRun(){
		start=true;
	}
	
	public void setGameSelection(int gameSelection){
		this.gameSelection=gameSelection;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getGroupName(){
		return groupName;
	}
	
}
