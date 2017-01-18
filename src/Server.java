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
    private Socket clientSocket;
    private static String input;

    private static ArrayList<Socket> socketList;//stores clients, possibly replace with User obj
    private static ArrayList<Card> deck;
    private static ArrayList<MessageThread> gameList;//stores names of all games
    private static ArrayList<User> userList;

    public static void main (String args[]) throws Exception{
        new Server().go();
    }

    public void go(){
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
                writer = new  PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public void run(){
            //processes username
            try{
            writer.println("/send username/");//format: "/s/sender/m/message"
            System.out.println("message sent");
            while(!loggedIn){
                if((msg = reader.readLine())!=null){
                    //check username against list of connected users
                    if(msg.contains("/username check/")) {
                        name = msg.substring(16);//format: "/username check/username"
                        for (User u : userList) {
                            if (name.equalsIgnoreCase(u.getName())) {
                                legitName = false;
                            }
                        }
                        if (legitName) {//if name is not already taken, add user to list
                            player=new User(clientSocket, name);
                            userList.add(player);
                            writer.println("/legit name/");
                        } else {
                            writer.println("/illegitimate name/");
                        }
                        name = ""; //reset values for next connection
                        legitName = true;
                    }else if(msg.contains("/new game/") || msg.contains("/join game/")){//format: "/new game/game name"
                        if(msg.contains("/new game/")){
                            name=msg.substring(10);
                        }else{
                            name=msg.substring(11);
                        }
                        for(MessageThread m: gameList){
                            if(m.getName().equals(name)){
                                legitName=false;
                            }
                        }
                        if(msg.contains("/new game/") && legitName){ //if user starts a new game
                            player.setGroupName(name);
                            MessageThread m=new MessageThread(name);
                            gameList.add(m);
                            m.getGame().addPlayer(player);
                            writer.println("/legit group name/");
                        }else if (msg.contains("/join game") && !legitName){//if user joins an existing game
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
                while ((msg=reader.readLine())!=null){
                    System.out.println(msg);
                    for(User u: userList){
                        u.getWriter().println(msg);
                    }
                    for(MessageThread m: gameList){
                        if(player.getGroupName().equals(m.getGame().getName())){
                            m.getQueue().enqueue(msg); //add message to appropriate queue/game
                            writer.println("you are now connected to game "+msg);
                            writer.println("/waiting for start/");
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