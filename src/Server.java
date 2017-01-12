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
    private static Queue<String> messageQueue;
    private static ArrayList<User> userList;

    public static void main (String args[]) throws Exception{
        new Server().go();
    }

    public void go() throws Exception{
        try {
            userList = new ArrayList<User>();
            messageQueue = new Queue<String>();
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

        while(fileReader.hasNext()){
            text=fileReader.nextLine();

            if(text.equals("/adjective")){
                isNoun=false;
            }else if(text.equals("/noun")){
                isNoun=true;
            }else if(!text.equals("")){
                d.add(new Card(text,null,isNoun));
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

    class ClientThread extends Thread{
        Socket clientSocket;
        int clientID=-1;
        boolean running=true;
        boolean legitName=true;
        String msg;
        BufferedReader reader;
        PrintWriter writer;
        String name;

        //constructor
        ClientThread(Socket s, int i){
            clientSocket=s;
            clientID=i;
        }

        public void run(){

            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

                while ((msg = reader.readLine()) != null) {
                    System.out.println("client says: " + msg);
                    if(msg.contains("/username check/")){//check username against list of connected users
                        name=msg.substring(15);//format: "/username check/username"
                        for(User u: userList){
                            if(name.equalsIgnoreCase(u.getName())){
                                legitName=false;
                            }
                        }
                        if (legitName){
                            userList.add(new User(clientSocket, name));
                        }

                        name="";
                        legitName=true;
                    }else if(msg.contains("/leaving/")){//check if socket is closed
                        name=msg.substring(9); //format: "/leaving/username"
                        for(User u: userList){//find user in list
                            if(u.getName().equals(name)){
                                userList.remove(u); //remove user
                            }
                        }
                    }else {
                        for (User u : userList) {
                            writeMessage(u.getSocket(), msg);
                            System.out.println("/server/ message sent");
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println("/error/ "+e);
                for(User u: userList){//find user in list
                    if(u.getSocket().equals(clientSocket)){
                        userList.remove(u); //remove user
                    }
                }
                System.out.println("/server/ user removed from list");
            }
        }
    }
}