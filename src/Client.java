import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
	private static boolean start=false;
	private String username;
	private String groupName;
	private boolean authenticatedName = false;
	private boolean authenticatedGroup = false;
	private int gameSelection=0;
	private static boolean creator;//tells if person created game or joined it
	private static ArrayList<Card>hand;




	public static void main (String args[]){
		new Client().go();
	}

	public void go(){
		String msg="";

		try {
			//declare stuff so no null pointer exceptions
			creator=false;
			hand=new ArrayList<Card>();

			socket = new Socket("localhost", 6666);//start up a socket
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //make reader and writer
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);//AUTOFLUSH IS MY SAVIOUR i hate flushing

			ServerConnectionThread t=new ServerConnectionThread();//create a new thread to handle client/server messages
			t.start();//start the stupid thing

			//this fragment constantly checks for user input and sends it to the server but i don't want that rn
			consoleReader = new BufferedReader(new InputStreamReader(System.in));
			while(reader.ready()) {//write to socket
				msg=reader.readLine();
				if(msg.contains("/chat/")) {
					writer.println(msg);
				}
			}



		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * ServerConnectionThread
	 * handles all messages from client to server
	 */
	class ServerConnectionThread extends Thread{
		boolean loggedIn=false;
		private boolean playing;


		//constructor
		ServerConnectionThread(){
			System.out.println("Login screen open now.");
			//Username Screen
			UsernameLogin userLogin = new UsernameLogin();
			userLogin.startUserScreen();
			while(!loggedIn) {
				System.out.println("threadRun2");
				try {
					System.out.println("threadRun3");
					while (reader.ready()){
						msg=reader.readLine();
						System.out.println("threadRun4");
						if(msg.contains("/send username/") || msg.contains("/no go on the nameroo, jer/")){
							System.out.println("threadRun5");
							while(start==false){	
							}
							System.out.println("threadRun6");
							writer.println("/username check/"+tempMessage);
						}else if(msg.contains("/g2g/")){//prompt for game name after username passes
							System.out.println("threadRun7");
							authenticatedName=true;
							loggedIn=true;
						}
						start=false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		//runs the client side of every game
		public synchronized void run(){
			boolean validCard;

			try {
				while(!playing){//while the user is not in any game, prompt to join or create one
					GroupLogin.startGroupScreen();
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
							if(msg.contains("/new/")){
								creator=true;
							}
						}
						start=false;
						playing = true;
						if (playing){
							GameScreen.startGameScreen();

						}
					}
					writer.println("/start game/");//tells server to start the game, u can replace this with a start button and only have it show if the player created the game


					if(msg.contains("/your hand/")) {//if receiving hand of cards, print them all out
						/*msg = msg.substring(msg.lastIndexOf("/") + 1);//remove the "/your hand/" part of the message

						hand.clear();//empty the current hand, replace with new hand
						while (msg.length() > 1) {//display cards
							hand.add(new Card(msg.substring(0,msg.indexOf("(")), Integer.valueOf(msg.substring(msg.indexOf("(")+1, msg.indexOf(")")))));
							msg = msg.substring(msg.indexOf("+") + 1);//remove first card from string
						}
						for(Card c: hand){//display cards with card numbers
							System.out.println("CARD "+c.getID()+": "+c.getText());
						}

						writer.println("/ready/"+username);//tell server to go to next stage
						System.out.println("player is ready. message sent.");
					} else if(msg.contains("/pick card/")){
						System.out.println("select the number of the card you want: ");
						text=input.nextLine();//get player's card

						for(Card c: hand){//see if player's selected card matches any of the ones in their hand
							if(Integer.valueOf(text)==c.getID()){
								validCard=true;
							}
						}
						while(!validCard){//error checking
							System.out.println("invalid number. try again: ");
							text=input.nextLine();//get player's card

							for(Card c: hand){//see if player's selected card matches any of the ones in their hand
								if(Integer.valueOf(text)==c.getID()){
									validCard=true;
								}
							}
						}
						writer.println("/ready/card/"+text);//tell the server which card the player picked
						 */
					} else{
						System.out.println(msg);
					}
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void startGame(){
		writer.println("/start game/");
	}

	public synchronized boolean getAuthenticateUsername(){
		return authenticatedName;
	}

	public synchronized boolean getAuthenticateGroupName(){
		return authenticatedGroup;
	}

	public synchronized void setTempMessage(String tempMessage){
		this.tempMessage=tempMessage;
	}

	public synchronized void setUsername(String username){
		this.username=username;
	}

	public synchronized void setGroupName(String groupName){
		this.groupName=groupName;
	}

	public synchronized void startRun(){
		start=true;
	}

	public synchronized void setGameSelection(int gameSelection){
		this.gameSelection=gameSelection;
	}

	public synchronized String getUsername(){
		return username;
	}

	public synchronized String getGroupName(){
		return groupName;
	}

}
