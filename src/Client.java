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
    private static User player;
    
    private static String msg;
    static String tempMessage;
    static boolean start=false;
    static String username;
	static String groupName;
	static boolean authenticatedName = false;
	static boolean authenticatedGroup = false;
	static int gameSelection=0;



    public static void main (String args[]){
        new Client().go();
    }

    public void go(){
        String text="";

        try{
            socket=new Socket("localhost", 4444);
            consoleReader=new BufferedReader(new InputStreamReader(System.in));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            ServerConnectionThread t=new ServerConnectionThread(); //make a new thread to check for messages from the server
            t.start(); //start the frickin thing

            while((text=consoleReader.readLine())!=null){ //constantly check for keyboard input
                writer.println("/chat/"+player.getName()+": "+text); //send it
            }

        }catch(IOException e){
            e.printStackTrace();
        }
      //Username Screen
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
    }

    class ServerConnectionThread extends Thread{
        boolean loggedIn=false;
        Scanner input=new Scanner(System.in);
        
        //constructor
        ServerConnectionThread(){
            while(!loggedIn) {
                try {
                    while ((msg=reader.readLine())!=null){
                        if(msg.contains("/send username/")){
                        	while(start==false){	
                        	}
                            writer.println("/username check/"+tempMessage);
                        }else if(msg.contains("/legit name/")){//prompt for game name after username passes
                        	authenticatedName=true;
                        	start=false;
                            player=new User(socket, username);
                            System.out.println("you are now logged in. username: " + username);
                            while(start==false){	
                        	}
                            if(gameSelection==1){
                                tempMessage.equals("/new game/"+tempMessage);
                            }else if(gameSelection==2){
                                tempMessage.equals("/join game/"+tempMessage);
                            }
                            writer.println(tempMessage);
                        }else if(msg.contains("/legit group name/")){
                        	authenticatedGroup=true;
                        	start=false;
                            loggedIn=true;//finally allow user to start a game
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
              

            }
        }

        public void run(){
            while(loggedIn){
                try {
                    if(reader.ready()) {
                        msg=reader.readLine();
                        if(msg.contains("/chat/")){
                            System.out.println(msg);//print the message from server
                        }else if(msg.contains("/waiting for start/")){
                            System.out.println("enter anything to begin game");
                            msg=input.nextLine();
                            writer.println("/ready/");
                        }else if(msg.contains("/card/")){
                           // player.getHand().add(new Card(msg.substring(6)));
                        }
                        else if(msg.contains("/waiting for card/")){
                            //for(Card c: player.getHand()){
                            //    System.out.println("card: "+c.getText());
                            //}
                            System.out.println("select a card: ");
                            msg=input.nextLine();
                            writer.println("/ready/sender/"+player.getName()+"/card/"+msg);
                        }

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
       
    }

    public static boolean getAuthenticateUsername(){
    	return authenticatedName;
    }
    
    public static boolean getAuthenticateGroupName(){
    	return authenticatedGroup;
    }
}
