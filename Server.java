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
    private static ArrayList<MessageThread> gameList;//stores names of all games
    private static ArrayList<User> userList;

    public static void main (String args[]) throws Exception{
        new Server().go();
    }

    public void go() throws Exception{
        try {
            gameList=new ArrayList<MessageThread>();
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
                d.add(new Card(text,null,isNoun, cardID++));
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

    class ClientThread extends Thread implements Serializable{
        User player;
        Socket clientSocket;
        int clientID=-1;
        boolean running=true;
        boolean loggedIn=false;
        boolean legitName=true;
        ObjectInputStream reader=null;
        ObjectOutputStream writer=null;
        String name;
        ChatObject msg;

        //constructor
        ClientThread(Socket s, int i){
            try {
                clientSocket = s;
                clientID = i;
                reader = new ObjectInputStream(clientSocket.getInputStream());
                writer = new ObjectOutputStream(clientSocket.getOutputStream());
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public void run(){
            //processes username
            try{
            writer.writeObject(new ChatObject("server", "player connected"));
            while(!loggedIn){
                if((msg = (ChatObject) reader.readObject())!=null){
                    //check username against list of connected users
                    if(msg.getMessage().contains("/username check/")) {
                        name = msg.getMessage().substring(16);//format: "/username check/username"
                        for (User u : userList) {
                            if (name.equalsIgnoreCase(u.getName())) {
                                legitName = false;
                            }
                        }
                        if (legitName) {//if name is not already taken, add user to list
                            player=new User(clientSocket, name);
                            userList.add(player);
                            writer.writeObject(new ChatObject(player.getName(), "/legit name/"));
                        } else {
                            writer.writeObject(new ChatObject(player.getName(), "/illegitimate name/"));
                        }
                        name = ""; //reset values for next connection
                        legitName = true;
                    }else if(msg.getMessage().contains("/new game/") || msg.getMessage().contains("/join game/")){//format: "/new game/game name"
                        name=msg.getMessage().substring(10);
                        for(MessageThread m: gameList){
                            if(m.getName().equals(name)){
                                legitName=false;
                            }
                        }
                        if(msg.getMessage().contains("/new game/") && legitName){ //if user starts a new game
                            gameList.add(new MessageThread(name));
                            player.setGroupName(name);
                            writer.writeObject(new ChatObject(player.getName(),"/legit group name/")); //if user joins an existing game
                        }else if (msg.getMessage().contains("/join game") && !legitName){
                            player.setGroupName(name);
                            for(MessageThread m: gameList){
                                if(m.getGame().getName().equals(name)){
                                    m.getGame().addPlayer(player);
                                }
                            }
                        }
                    }

                }
            }
            }catch(Exception e){
                e.printStackTrace();
            }

            try {
                while ((msg=(ChatObject)reader.readObject())!=null){
                    System.out.println(msg);
                    for(MessageThread m: gameList){
                        if(player.getGroupName().equals(m.getGame().getName())){
                            m.getQueue().enqueue(msg); //add message to appropriate queue/game
                        }
                    }
                }
            } catch (Exception e) {// checks to see if a user has disconnected
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
                    System.out.println(u.getName());
                }
            }
        }
    }
}