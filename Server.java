import java.net.*;
import java.io.*;
import java.util.*;

class Server{
    private static ServerSocket serverSocket;
    private static BufferedReader reader;
    private static ArrayList<ClientThread> clients;
    private static ArrayList<Game> games;


    public static void main(String args[]){
        new Server().go();//idk why this works but rayan said mr mangat did it like this amd it works so we should do this
    }

    /**
     * go method
     * runs everything
     */
    public void go(){
        Socket clientSocket;

        try{
            games=new ArrayList<Game>();//create arraylist of game threads

            clients=new ArrayList<ClientThread>();
            serverSocket=new ServerSocket(6666);

            while(true){
                clientSocket=serverSocket.accept();//accept the connection

                ClientThread t = new ClientThread(clientSocket); //start up a new thread for each client
                clients.add(t);//add thread to array
                t.start(); //start the frickin thing

            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * ClientThread
     * contains writer/reader for each socket to keep socket writing and reading simple
     * handles messages between server and individual client
     */
    class ClientThread extends Thread{
        private Socket socket;
        private PrintWriter writer;
        private BufferedReader reader;
        private String name;
        private String gameName;
        private int awesomePoints;
        private boolean czar;
        private boolean loggedIn;
        private boolean playing;

        public Socket getSocket(){
            return socket;
        }
        public PrintWriter getWriter(){
            return writer;
        }
        public BufferedReader getReader(){
            return reader;
        }
        public String getUsername(){
            return name;
        }
        public String getGameName(){
            return gameName;
        }
        public int getPoints(){
            return awesomePoints;
        }
        public boolean getCzar(){
            return czar;
        }

        /**
         * ClientThread
         * @param s //the socket
         * basically takes care of everything the client sends to the server
         */
        ClientThread(Socket s){ //constructor, includes login
            String msg=""; //temporary variables to store messages from client
            int numHappenings=0;

            //declare most variables just in case nullpointerexception appears
            name="";
            gameName="";
            awesomePoints=0;
            czar=false;
            loggedIn=false;
            playing=false;

            //now try the rest
            try {
                socket = s;
                writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
                reader=new BufferedReader(new InputStreamReader(s.getInputStream()));

                writer.println("/send username/");

                //getting username so can log in
                while(!loggedIn) {
                    while (reader.ready()) {
                        msg = reader.readLine();
                        if (msg.contains("/username check/")) {
                            msg=msg.substring(16);
                            for (ClientThread c : clients) {//run through each client thread in arraylist
                                if (msg.equalsIgnoreCase(c.getUsername())){ //check if the user's proposed name matches any existing names, IGNORING CASE
                                    numHappenings++; //if name has already been taken, add 1 to number of times name is in arraylist
                                }
                            }

                            System.out.println("numHappenings uName: "+numHappenings);//debug

                            if(numHappenings==0){ //if name is good, happenings should be 0
                                name=msg;//set client's username
                                writer.println("/g2g/");//tell client that user is good to go (g2g)
                                loggedIn=true; //tell server user is logged in and good to go
                            }else{
                                writer.println("/no go on the nameroo, jer/");//tell client that user must try again
                            }
                            numHappenings=0;//reset the variable -- IMPORTANT
                        }
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        //main method basically
        //runs games and game selection
        public void run(){
            String msg="";
            int numHappenings=0;

            //get user to select or make new game
            try {

                if(!playing){//tell client to send a game name
                    writer.println("/send game name/"); //tell client to send a game name
                }

                while (!playing) {
                    while(reader.ready()){
                        msg=reader.readLine();

                        System.out.println("/msg/"+msg);

                        if(msg.contains("/game name check/")){ //format this line will be in: "/new game/game name check/gameName here" or "/join game/game name check/gameName here"
                            System.out.println("game array size: "+games.size());
                            for(Game g: games){//check it against all existing group names
                                System.out.println("game name: "+g.getGameName());
                                if(msg.substring(msg.indexOf("/game name check/")+17).equalsIgnoreCase(g.getGameName())){
                                    numHappenings++;
                                }
                            }

                            System.out.println("numHappenings game: "+numHappenings);//debug

                            if(numHappenings==0 && msg.contains("/new game/")){//if name is available and new game
                                Game g=new Game(msg.substring(msg.indexOf("/game name check/")+17));//create a new game thread with name
                                games.add(g);

                                for(ClientThread c: clients){//loop through all clients
                                    if(c.getUsername().equalsIgnoreCase(name)){//find the one belonging to the player, ie. this one
                                        g.addPlayer(c);//add the player to the game's arraylist
                                    }
                                }

                                writer.println("/game name okay/");//tell client that name is good
                                playing=true; //give client go-ahead to play game
                            }else if(numHappenings>0 && msg.contains("/join game/")){//if client wants to join game and game exists
                                for(ClientThread c: clients) {//go through all clients and find this one
                                    if(c.getUsername().equalsIgnoreCase(name)) {
                                        for(Game g: games) {//go through all games and find the one they want to join
                                            if (msg.substring(msg.indexOf("/game name check/")+17).equals(g.getGameName())) {
                                                g.addPlayer(c);//add client to game
                                                playing=true;
                                            }
                                        }
                                    }
                                }
                            }else{//if game name is invalid
                                writer.println("/game name invalid/");//tell the client that.
                            }

                            numHappenings=0; //reset variable -- IMPORTANT
                        }
                    }
                }
            }catch(Exception e){//mmm exceptions
                e.printStackTrace();
            }

            //handles all messages within the game
            //receives messages from client, puts them in the proper game queue to be dealt with by the game thread
            try{
                while(reader.ready()){//while there are messages from the client
                    msg=reader.readLine();//get the message
                    for(Game g: games){//loop through all games to find the one that matches the client's
                        if(gameName.equalsIgnoreCase(g.getGameName())){
                            g.addMessage(msg);//add the message to the proper game's queue
                        }
                    }
                }
            }catch(Exception e){//woohoo i love catching exceptions
                e.printStackTrace();
            }
        }
    }

    /**
     * Game
     * handles group messages
     */
    class Game extends Thread{
        private int state;
        //states:
        //0 = waiting to begin round, if round is 0, hand size 6, if not 0, hand size 1
        //1 = waiting for players to pick cards
        //2 = waiting for czar to pick winner

        private int numPlayers;
        private String name;
        private ArrayList<ClientThread> players;
        private Queue<String> queue;

        public int getNumPlayers(){
            return numPlayers;
        }
        public String getGameName(){
            return name;
        }
        public ArrayList<ClientThread> getPlayers(){
            return players;
        }
        public Queue<String> getQueue(){
            return queue;
        }
        public void addPlayer(ClientThread c){
            players.add(c);
        }
        public void addMessage(String s){
            queue.enqueue(s);
        }

        Game(String n){//constructor
            state=0;
            players=new ArrayList<ClientThread>();
            name=n;
            queue=new Queue<String>();

        }

        public void run(){
            String text;

            try{
                while(!queue.isEmpty()){
                    text=queue.dequeue();
                    if(text.contains("/chat/")) { //send message to all players if player enters stuff into the game chat
                        for (ClientThread c : players) {
                            c.getWriter().println(text);
                        }
                    }else if(text.contains("/new player/")){

                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }
}