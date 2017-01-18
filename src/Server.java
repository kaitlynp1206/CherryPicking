/**
 * Created by Elizabeth Ip on 2017-01-08.
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server{
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static String input;

    private static ArrayList<Socket> socketList;//stores clients, possibly replace with User obj
    private static ArrayList<Card> deck;
    private static ArrayList<Game> gameList;//stores names of all games
    private static ArrayList<User> userList;

    public static void main (String args[]) throws Exception{
        new Server().go();
    }

    public void go() throws Exception{
        try {
            gameList=new ArrayList<Game>();
            userList = new ArrayList<User>();
            serverSocket = new ServerSocket(4444);
            int threadID = 0;

            while (true) {
                clientSocket = serverSocket.accept();

                System.out.println("/server/ client connected");

                ClientThread t = new ClientThread(clientSocket, threadID++);
                t.start();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    //reads cards from file
    public static ArrayList<Card> getDeck() throws Exception {
        File cardsFile=new File("cards.txt");
        Scanner fileReader=new Scanner(cardsFile);
        String text;
        boolean isNoun=false;
        ArrayList<Card>d=new ArrayList<Card>();
        int cardID=-1;

        while(fileReader.hasNext()){
            text=fileReader.nextLine();

            if(text.equals("/adjective")){
                isNoun=false;
            }else if(text.equals("/noun")){
                isNoun=true;
            }else if(!text.equals("")){
                d.add(new Card(text, null, isNoun, cardID++));
            }
        }

        return d;//return deck arraylist

    }//end getDeck

    //picks random cards from the deck to form a hand
    public ArrayList<Card> getHand (ArrayList<Card> d, String user){
        int handSize=7;
        int deckSize=d.size();
        int randomNumber;
        Card card;
        ArrayList<Card> hand=new ArrayList<Card>();

        //loop as many cards as needed
        for(int i=0;i<handSize;i++){
            randomNumber=(int)(Math.random()*deckSize);
            card=deck.get(randomNumber);//draw card from deck

            while(!card.getUser().equals(null)){//check if card is already in another user's hand
                randomNumber=(int)(Math.random()*deckSize);//if so, redraw card from deck
                card=deck.get(randomNumber);
            }

            card.setUser(user);//once card is legit, change user ID to current user
            hand.add(card);//add card to user's hand
        }

        return hand;
    }//end getHand

    public static void writeMessage(Socket s, String msg) throws IOException{
        PrintWriter output=new PrintWriter(s.getOutputStream(), true);
        output.println(msg);
    }

    class MessageThread extends Thread{//make multiple message threads
        Queue<String> messageQueue;
        String msg;
        Game game;

        MessageThread(String s){
            game=new Game(s);
            messageQueue=new Queue<String>();

        }
        public void run(){
            try{
                while(!messageQueue.isEmpty()){
                    msg=messageQueue.dequeue();

                    if(msg.contains("/chat/")) {
                        for (User u : game.getPlayers()) {
                            writeMessage(u.getSocket(), msg);
                        }
                    }

                }
            }catch(IOException e){
                System.out.println(e);
            }

        }

    }

    class ClientThread extends Thread{
        User player;
        Socket clientSocket;
        int clientID=-1;
        boolean running=true;
        boolean loggedIn=false;
        boolean legitName=true;
        BufferedReader reader=null;
        PrintWriter writer=null;
        String name;
        String msg;

        //constructor
        ClientThread(Socket s, int i){
            try {
                clientSocket = s;
                clientID = i;
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public void run(){
            //processes username
            writer.println("/send username/");
            while(!loggedIn){
                try{
                    if(reader.ready()){
                        msg=reader.readLine();
                        System.out.println("/msg/ "+msg);
                        //check username against list of connected users
                        if(msg.contains("/username check/")) {
                            name = msg.substring(16);//format: "/username check/username"
                            for (User u : userList) {
                                if (name.equalsIgnoreCase(u.getUsername())) {
                                    legitName = false;
                                }
                            }
                            if (legitName) {//if name is not already taken, add user to list
                                player=new User(clientSocket, name);
                                userList.add(player);
                                writer.println("/legit name/");
                                loggedIn=true;
                            } else {
                                writer.println("/illegitimate name");
                            }
                            name = ""; //reset values for next connection
                            legitName = true;
                        }else if(msg.contains("/new game/") || msg.contains("/join game/") && legitName){//format: "/new game/game name"
                            name=msg.substring(10);
                            for(Game g: gameList){
                                if(g.getName().equals(name)){
                                    legitName=false;
                                }
                            }
                            if(legitName){
                                player.setGroupName(name);
                                writer.println("/legit group name");
                            } else {
                                writer.println("/illegitimate group name");
                            }
                        }

                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            System.out.println("/server/ client successfully logged in");
            try {
                while ((msg=reader.readLine())!=null) {
                    System.out.println(msg);
                     if(msg.contains("/chat/")){//if user enters text into chat window
                         msg=msg.substring(6);
                         for(User u: userList) {
                             writeMessage(u.getSocket(), u.getUsername()+msg);
                         }
                    }
                }
            } catch (IOException e) {
                int index=-1;

                System.out.println("/error/ "+e);
                for(User u: userList){//find user in list
                    if(u.getSocket().equals(clientSocket)){
                        index=userList.indexOf(u);
                    }
                }
                userList.remove(index);
                System.out.println("/server/ user removed from list");

                for(User u: userList){
                    System.out.println(u.getUsername());
                }
            }
        }
    }
}