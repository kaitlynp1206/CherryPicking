import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Client.java
 * @author Kaitlyn Paglia & Elizabeth Ip
 * @version 1.0
 * 1/23/2017
 * Client communicating with Server and storing Client's information to be used by GUI
 */
public class Client {
	private static Socket socket;
	private static PrintWriter writer;
	private static BufferedReader reader;
	private static BufferedReader consoleReader;

	private static String msg = "";
	private static String tempMessage;
	static boolean start;
	private static String username;
	private static String groupName;
	private static boolean authenticatedName = false;
	private static boolean authenticatedGroup = false;
	private static int gameSelection=0;
	private static boolean creator;
	static ArrayList<Card>hand;
	static ArrayList<Card> selected;
	private static boolean proceed = false;
	private static int selectedCard;
	private static boolean validCard;

	static ArrayList<String>players;
	static ArrayList<Integer>scores;

	private static int winningCard;
	private static String chatMessage = "";

	/**
	 * main
	 * Initializes Client
	 * @param args
	 */
	public static void main (String args[]){
		new Client().go();
	}

	/**
	 * go
	 * Connects to server socket
	 */
	public void go(){
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
			start=false;
			//Username Screen
			UsernameLogin.startUserScreen();
			while(!loggedIn) {
				try {
					while (reader.ready()){
						msg=reader.readLine();
						if(msg.contains("/send username/")){
							while(start==false){	
							}
							writer.println("/username check/"+tempMessage);
						}else if(msg.contains("/g2g/")){//prompt for game name after username passes
							setAuthenticateUsername();
							proceed=true;
							loggedIn=true;
						}else if(msg.contains("/no go on the nameroo, jer/")){
							proceed=true;
							while(start==false){	
							}
							writer.println("/username check/"+tempMessage);
						}
						start=false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e);
				}
			}
		}

		/**
		 * run
		 * Executes the Client's side of the communication
		 */
		public synchronized void run(){
			try {
				while(!playing){//while the user is not in any game, prompt to join or create one
					while(reader.ready()){
						msg=reader.readLine();
						System.out.println("/msg/"+msg);
						if(msg.contains("/send game name/")){//if prompted to send game name
							//wait for player to hit button
							while(!start){
							}
							//receive if new or joined game
							if(getGameSelection()==1) {//if user starts a new game, prompt for name and send with "/new game/"
								writer.println("/new game/game name check/" + tempMessage);//send the proposed game name to the server
							}else{//if user joins a game, prompt for name and send with "/join game/"
								writer.println("/join game/game name check/"+ tempMessage);
							}
						}else if (msg.contains("/game name invalid/")){
							proceed=true; //run as invalid
							//wait for player to hit button
							while(!start){
							}
							//receive if new or joined game
							if(getGameSelection()==1) {//if user starts a new game, prompt for name and send with "/new game/"
								writer.println("/new game/game name check/" + tempMessage);//send the proposed game name to the server
							}else{//if user joins a game, prompt for name and send with "/join game/"
								writer.println("/join game/game name check/"+ tempMessage);
							}
						}else if(msg.contains("/game name okay/")){//if user is able to join game/create a new one
							setAuthenticateGroupName();
							System.out.println("C"+getAuthenticateGroupName());
							proceed=true;
							start=false;
							if(msg.contains("/new/")){
								System.out.println("Check");
								creator=true;
								System.out.println("y"+start);
								/*while(!start){
									System.out.println(start+" GEL");
								}
								System.out.println("x"+start);
								writer.println("/start game/");//tells server to start the game, u can replace this with a start button and only have it show if the player created the game
								playing=true;//allow user to move on
								System.out.println(playing);*/
							}
							playing=true;
						}

					}
				}

				while (playing) {//game loop basically
					if (reader.ready()) {//if server sent a message
						msg = reader.readLine();
						validCard = false;
						System.out.println("C: "+msg);
						//Receiving and Display hand
						if (msg.contains("/your hand/")) {//if receiving hand of cards, print them all out
							msg = msg.substring(msg.lastIndexOf("/") + 1);//remove the "/your hand/" part of the message

							hand.clear();//empty the current hand, replace with new hand
							while (msg.length() > 1) {//display cards
								hand.add(new Card(msg.substring(0, msg.indexOf("(")), Integer.valueOf(msg.substring(msg.indexOf("(") + 1, msg.indexOf(")")))));
								msg = msg.substring(msg.indexOf("+") + 1);//remove first card from string
							}
							System.out.println("YOUR HAND: ");//COPY THIS just for aesthetics
							for (Card c : hand) {//display cards with card numbers
								System.out.println("CARD " + c.getID() + ": " + c.getText());
							}

							//writer.println("/ready/" + username);//tell server to go to next stage
						}
						//Displaying everyone's cards
						else if (msg.contains("/selected cards/")) {//display the selected cards
							msg = msg.substring(msg.lastIndexOf("/") + 1);
							selected.clear();//empty the current hand, replace with new hand
							while (msg.length() > 1) {//display cards
								selected.add(new Card(msg.substring(0, msg.indexOf("(")), Integer.valueOf(msg.substring(msg.indexOf("(") + 1, msg.indexOf(")")))));
								msg = msg.substring(msg.indexOf("+") + 1);//remove first card from string
							}
							System.out.println("CONTENDERS: ");//tell players that the following cards are the ones they picked
							for (Card c : selected) {//display cards with card numbers
								System.out.println("CARD " + c.getID() + ": " + c.getText());
							}
							//Client inputs their selected Card
						} else if (msg.contains("/pick card/")) {//if player is prompted to pick a card
							System.out.print("select the number of the card you want: ");
							while (!start){
							}
							for (Card c : hand) {//see if player's selected card matches any of the ones in their hand
								if (Integer.valueOf(getSelectedCard()) == c.getID()) {
									validCard = true;
								}
							}
							while (!validCard) {//error checking
								System.out.println("invalid number. try again: ");
								while (!start){
								}
								for (Card c : hand) {//see if player's selected card matches any of the ones in their hand
									if (Integer.valueOf(getSelectedCard()) == c.getID()) {
										validCard = true;
									}
								}
							}
							writer.println("/ready/" + username + "/card/" + getSelectedCard());//tell the server which card the player picked

						}
						else if (msg.contains("/pick winner/")) {
							System.out.print("select the number of the card you think should win: ");

							for (Card c : selected) {//see if player's selected card matches any of the ones in the selected array
								if (Integer.valueOf(getWinningCard()) == c.getID()) {
									validCard = true;
								}
							}
							while (!validCard) {//error checking
								System.out.println("invalid number. try again: ");
								for (Card c : selected) {//see if player's selected card matches any of the ones in their hand
									if (Integer.valueOf(getWinningCard()) == c.getID()) {
										validCard = true;
									}
								}
							}
							writer.println("/ready/" + username + "/card/" + getWinningCard());//tell the server which card the player picked

						} else if (msg.contains("/you are czar/")) { //send ready message right away if player is czar -- they don't need to pick an adjective card
							System.out.println("you are the czar. waiting for players to send in selections");
							writer.println("/ready/" + username);
						}
						else if (msg.contains("/winner/")) {//if the winner has been picked and server is waiting to start next round
							if (msg.contains("/card/")) {
								System.out.println("WINNER: " + msg.substring(8, msg.indexOf("/card/")));
								System.out.println("CARD: " + msg.substring(msg.indexOf("/card/") + 1));
								System.out.print("enter anything to start the next round: ");
								//text = input.nextLine();
								writer.println("/ready/" + username);
							} else {//if the game is over

							}
						} else if (msg.contains("/confirm start/")) {
							writer.println("/ready/" + username + "/card/");
						} else if (msg.contains("/game over/")) {
							System.out.println("GAME OVER! WINNER: " + msg.substring(11));//tell player game is over
							System.out.print("enter anything to exit the game: ");
							//text = input.nextLine();
							writer.println("/exit/" + username);//tell server user has disconnected
							playing = false;
						}
						else if(msg.contains("/chat/")){
							setChatMessage(msg.substring(7));
						}
						//COPY thiS ELSE IF
						else if(msg.contains("/scores/")){
							msg=msg.substring(8);
							players.clear();
							scores.clear();
							while(msg.length()>1){
								players.add(msg.substring(0,msg.indexOf("(")));
								scores.add(Integer.valueOf(msg.substring(msg.indexOf("(")+1,msg.indexOf(")"))));
								msg=msg.substring(msg.indexOf("+")+1);
							}
							//print the scores and players out
							for(int i=0;i<players.size();i++){
								System.out.println(players.get(i)+": "+scores.get(i));
							}
						}
						else{
							System.out.println(msg);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//Getters and Setters to be used by inherited classes
	
	public void startGame(){
		writer.println("/start game/");
	}

	public void startRun(){
		start=true;
		System.out.println(start);
	}

	public void stop(){
		start=false;
	}

	public void stopRun(){
		proceed=false;
	}

	public boolean getRun(){
		return proceed;
	}

	public void setAuthenticateUsername(){
		authenticatedName=true;
	}

	public void setAuthenticateGroupName(){
		authenticatedGroup=true;
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


	public void setGameSelection(int x){
		gameSelection=x;
	}

	public int getGameSelection(){
		return gameSelection;
	}

	public String getUsername(){
		return username;
	}

	public synchronized String getGroupName(){
		return groupName;
	}

	public boolean isCreator(){
		return creator;
	}

	public void openHelpScreen(){
		HelpScreen.startHelpScreen();
	}

	public void setSelectedCard(int x){
		selectedCard=x;
	}

	public int getSelectedCard(){
		return selectedCard;
	}

	public void setMsg(String message){
		msg=message;
	}

	public int getWinningCard(){
		return winningCard;
	}

	public void setWinningCard(int x){
		winningCard=x;
	}

	public synchronized String getChatMessage(){
		return chatMessage;
	}

	public void setChatMessage(String m){
		chatMessage=m;
	}

}
