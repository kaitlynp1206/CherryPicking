/**
 * Created by Elizabeth Ip on 2017-01-18.
 */
import java.net.*;//gonna import everything u can fix it later if u want
import java.io.*;
import java.util.*;

public class Client {
    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static BufferedReader consoleReader;
    private static String username; //just in case i need to reference it
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
        private boolean loggedIn;
        private boolean playing;
        private Scanner input;//replace with BufferedReader later if we have time
        String msg;//for server messages
        String text;//for whatever user types in to console

        ServerConnectionThread(){//constructor
            loggedIn=false; //set most variables
            playing=false;
            input=new Scanner(System.in);//i'm lazy don't want to write a bufferedreader

            msg="";
            text="";

            //runs the username authentication
            try {
                while (!loggedIn) {
                    while (reader.ready()) {//while there are messages from the server
                        msg=reader.readLine();
                        if(msg.contains("/send username/")){
                            System.out.print("enter a username: "); //idk u can replace the system.outs w/the text fields
                            text=input.nextLine();
                            writer.println("/username check/"+text);//send username to server
                        }else if(msg.contains("/g2g/")){//if the username is a valid one
                            username=text;//set username
                            loggedIn=true;//allow client to move on to the next step -- joining a game
                        }else if(msg.contains("/no go on the nameroo, jer/")){//if the name is already taken by another user (bonus points if u get the reference)
                            System.out.print("username not valid. please try again: "); //get another name from the user
                            text=input.nextLine();
                            writer.println("/username check/"+text);//send it to the server
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }//end constructor

        //runs the client side of every game
        public synchronized void run(){
            boolean validCard;

            try {
                while(!playing){//while the user is not in any game, prompt to join or create one
                    while(reader.ready()){
                        msg=reader.readLine();
                        System.out.println("/msg/"+msg);

                        if(msg.contains("/send game name/") || msg.contains("/game name invalid/")){//if prompted to send game name
                            if(msg.contains("/game name invalid/")){
                                System.out.println("that name is invalid. try again.");//tell the user what was wrong with their previous game name attempt
                            }

                            System.out.print("enter n to start a new game, or j to join a game: ");//replace with gui
                            text=input.nextLine();

                            while(!text.equalsIgnoreCase("n") && !text.equalsIgnoreCase("j")){ //error checking, delete when implementing gui
                                System.out.print("not a valid option. enter either n or j: ");
                                text=input.nextLine();
                            }

                            if(text.equals("n")) {//if user starts a new game, prompt for name and send with "/new game/"
                                System.out.print("enter a game name: ");
                                text=input.nextLine();
                                writer.println("/new game/game name check/" + text);//send the proposed game name to the server

                            }else{//if user joins a game, prompt for name and send with "/join game/"
                                System.out.println("enter a game name: ");
                                text=input.nextLine();
                                writer.println("/join game/game name check/"+text);
                            }
                        }else if(msg.contains("/game name okay/")){//if user is able to join game/create a new one
                            if(msg.contains("/new/")){
                                creator=true;
                                System.out.print("enter g to start: ");
                                text=input.nextLine();
                                writer.println("/start game/");//tells server to start the game, u can replace this with a start button and only have it show if the player created the game
                            }
                            playing=true;//allow user to move on
                        }
                    }
                }

                /**COPY THE ENTIRE WHILE LOOP**/
                while(playing) {//game loop basically
                    if (reader.ready()) {//if server sent a message
                        msg = reader.readLine();
                        validCard=false;

                        if(msg.contains("/your hand/")) {//if receiving hand of cards, print them all out
                            msg = msg.substring(msg.lastIndexOf("/") + 1);//remove the "/your hand/" part of the message

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

                        } else{
                            System.out.println(msg);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
