import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

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
        private boolean ready;//ready for next phase of game
        private Card card;//selected card
        private ArrayList<Card> hand;

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
        public void setCzar(boolean c){
            czar=c;
        }
        public boolean getCzar(){
            return czar;
        }
        public void setReady(boolean r){
            ready=r;
        }
        public boolean getReady(){
            return ready;
        }
        public void setCard(Card c){
            card=c;
        }
        public Card getCard(){
            return card;
        }
        public void setHand(ArrayList<Card> h){
            hand=h;
        }
        public ArrayList<Card> getHand(){
            return hand;
        }

        //other methods that are not getters and setters
        public void addCard(Card c){
            hand.add(c);
        }
        public void removeCard(Card c){
            hand.remove(c);
        }

        ClientThread(Socket s){
            String msg=""; //temporary variables to store messages from client
            int numHappenings=0;

            //declare most variables just in case nullpointerexception appears
            name="";
            gameName="";
            awesomePoints=0;
            czar=false;
            loggedIn=false;
            playing=false;
            ready=false;
            hand=new ArrayList<Card>();

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
        public synchronized void run(){
            String msg="";
            int numHappenings=0;

            //get user to select or make new game
            try {

                if(!playing){//tell client to send a game name
                    writer.println("/send game name/"); //tell client to send a game name
                }

                while (!playing) {
                    if(reader.ready()){
                        msg=reader.readLine();

                        System.out.println("/msg/"+msg);

                        if(msg.contains("/game name check/")){ //format this line will be in: "/new game/game name check/gameName here" or "/join game/game name check/gameName here"
                            for(Game g: games){//check it against all existing group names
                                System.out.println("game name: "+g.getGameName());
                                if(msg.substring(msg.indexOf("/game name check/")+17).equalsIgnoreCase(g.getGameName())){
                                    numHappenings++;
                                }
                            }


                            /**COPY THIS ENTIRE IF/ELSE STATEMENT**/
                            if(numHappenings==0 && msg.contains("/new game/")){//if name is available and new game
                                gameName=msg.substring(msg.indexOf("/game name check/")+17);
                                Game g=new Game(gameName);//create a new game thread with name
                                games.add(g);
                                g.start();

                                for(ClientThread c: clients){//loop through all clients
                                    if(c.getUsername().equalsIgnoreCase(name)){//find the one belonging to the player, ie. this one
                                        writer.println("/game name okay/new/");//tell client that name is good

                                        g.addPlayer(c);//add the player to the game's arraylist
                                        g.addMessage("/new player/"+c.getUsername());
                                    }
                                }
                                playing=true; //give client go-ahead to play game
                                System.out.println("playing is true");

                            }else if(numHappenings>0 && msg.contains("/join game/")){//if client wants to join game and game exists
                                gameName=msg.substring(msg.indexOf("/game name check/")+17);
                                System.out.println("game name: "+gameName);

                                for(ClientThread c: clients) {//go through all clients and find this one
                                    if(c.getUsername().equalsIgnoreCase(name)) {
                                        for(Game g: games) {//go through all games and find the one they want to join
                                            if (gameName.equals(g.getGameName())) {
                                                System.out.println("BLORP");//debug message

                                                writer.println("/game name okay/join/");
                                                g.addPlayer(c);//add client to game
                                                g.addMessage("/new player/"+c.getUsername());
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

            /**COPY THE ENTIRE TRY/CATCH BLOCK**/
            //handles all messages within the game
            //receives messages from client, puts them in the proper game queue to be dealt with by the game thread
            try {
                while (playing) {
                    msg = reader.readLine();//get the message

                    if (!msg.equals(null)) {//while there are messages from the client
                        System.out.println("u got smth: "+msg);
                        for (Game g : games) {//loop through all games to find the one that matches the client's
                            if (gameName.equalsIgnoreCase(g.getGameName())) {
                                g.addMessage(msg);//add the message to the proper game's queue
                                System.out.println("message added: "+msg);
                                System.out.println("queue is empty: "+g.getQueue().isEmpty());
                            }
                        }
                    }
                }
            }catch(Exception e){//woohoo i love catching exceptions
                int index=-1;//this block lets the server disconnect the client/remove them from the client list

                for(ClientThread c: clients){//find user in list
                    if(c.getSocket().equals(socket)){
                        index=clients.indexOf(c);
                    }
                }
                System.out.println("user disconnected: "+clients.get(index).getUsername());
                clients.remove(index);//remove them

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
        //states (COPY THIS ENTIRE COMMENT BLOCK)
        //0 = waiting to begin game
        //1 = waiting to begin round, if round is 0, hand size 6, if not 0, hand size 1
        //2 = waiting for players to pick cards
        //3 = waiting for czar to pick winner
        //4 = game over

        private int roundNum;//keep track of how many rounds have passed
        private int numReady;
        private String name;
        private ArrayList<ClientThread> players;
        private ArrayList<Card> deck;
        private ArrayList<Card> selectedCards;
        private Queue<String> queue;
        public static final int HAND_SIZE=6;//COPY THIS ITS IMPORTANT

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
            deck=new ArrayList<Card>(); //THIS IS IMPORTANT, COPY THIS
        }

        /**u know what COPY EVERYTHING FROM HERE DOWN**/
        //gets cards from file, returns a deck of cards
        public synchronized ArrayList<Card> getDeck() throws Exception {
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
                }else if(text.equals("/noun/")){
                    isNoun=true;
                }else if(!text.equals("")){
                    d.add(new Card(text,null,isNoun, cardID++));
                }
            }
            return d;//return deck arraylist
        }//end getDeck

        //drawCard
        //returns a card from the deck, randomly selected
        public Card drawCard(String player){
            int randomNum;

            randomNum=(int)(Math.random()*deck.size());
            System.out.println("random number: "+randomNum);

            while(deck.get(randomNum).getPlayer()!=null || deck.get(randomNum).getNoun()){//make sure the card drawn is not already in another player's hand AND it is an adjective card
                randomNum=(int)(Math.random()*deck.size());
                System.out.println("random number: "+randomNum);
            }

            deck.get(randomNum).setPlayer(player);//indicate that card is in player's hand
            return deck.get(randomNum);
        }

        //handToString
        //adds player's cards into a single string -- format: "sexy(165)+motivational(5)+should be gotten rid of ASAP(71)+senseless+(202)"
        public String handToString(ArrayList<Card> h){
            String s="";

            for(Card c: h){
                s+=c.getText()+"("+c.getID()+")+";
            }
            System.out.println("concatenated hand: "+s);

            return s;
        }

        /**yeah just COPY ENTIRE RUN METHOD**/
        //run
        //handles basically everything
        public synchronized void run(){
            String msg;
            int randomNum=0;

            try{
                while(state!=4) {
                    //reset a bunch of variables
                    numReady=0;

                    currentThread().sleep(1);
                    if (!queue.isEmpty()) {
                        msg = queue.dequeue();
                        System.out.println("u got mail: "+msg);
                        if (msg.contains("/chat/")) { //send message to all players if player enters stuff into the game chat
                            for (ClientThread c : players) {
                                c.getWriter().println(msg);
                            }
                        } else if (msg.contains("/new player/")) {//send a "[PLAYER NAME] JOINED GAME!" message to all players
                            for (ClientThread c : players) {
                                c.getWriter().println(msg.substring(msg.indexOf("/new player/") + 12) + " joined the game.");
                                System.out.println("new player joined game");
                            }
                        } else if (msg.contains("/start game/")) {//if person who created game starts the game
                            System.out.println("YAAAS game started");
                            deck=getDeck();//get deck from file
                            System.out.println("OOOOOH deck exists");
                            for (ClientThread c : players) {//go through every player
                                for (int i = 0; i < HAND_SIZE; i++) {//draw six cards randomly from the deck and give to player
                                    c.addCard(drawCard(c.getUsername()));
                                }
                                c.getWriter().println("/your hand/" + handToString(c.getHand()));//concatenate hand into a single string for sending
                            }
                            state = 1;//change state to 1
                        } else if (msg.contains("/ready/")) {//if player is ready, start next phase of game
                            System.out.println("a player is ready to proceed. state: "+state);//debug

                            for (ClientThread c : players) {//find client in list, change status to ready
                                if (msg.substring(msg.indexOf("/ready/") + 7).equals(c.getUsername())) {
                                    c.setReady(true);

                                    if (state == 2 && !c.getCzar()) {//if waiting for players to select cards, and server receives a card selection
                                        for (Card k : c.getHand()) {//go through all cards in the client's hand and find the card they picked
                                            if (k.getID() == Integer.valueOf(msg.substring(msg.indexOf("/card/") + 6))) {//card is identified based on card ID number b/c duplicate cards are a thing
                                                selectedCards.add(k);//add card to list of selected cards that czar can pick from
                                                c.removeCard(k);//remove card from player's hand
                                                c.getWriter().println("/your hand/"+handToString(c.getHand()));//send player new hand
                                            }
                                        }
                                    }
                                }
                            }

                            for (ClientThread c : players) {//count how many players are ready to start the next stage
                                if (c.getReady()) {
                                    numReady++;
                                }
                            }

                            System.out.println("num players ready: "+numReady);

                            if (numReady == players.size()) {//if everyone is ready to move on
                                System.out.println("all players are go");

                                if (state == 1) {//begin round -- pick czar and prompt, give everyone cards, and wait for players to select
                                    state=2;//change state to 2

                                    if(roundNum==0){//pick a czar -- random if round==0, shift over 1 if round>0
                                        players.get((int)(Math.random()*players.size())).setCzar(true);
                                    }else{
                                        randomNum++;
                                        if(randomNum==players.size()){//if reached the end of arraylist, go to first player and make them czar
                                            randomNum=0;
                                        }
                                    }

                                    for(ClientThread c: players) {//give everyone another card if their hand size is <6
                                        if(c.getHand().size()<6 && roundNum!=0){
                                            for(int i=0;i<(6-c.getHand().size());i++){
                                                c.addCard(drawCard(c.getUsername()));
                                                c.getWriter().println("/your hand/"+handToString(c.getHand()));//send hand to player
                                            }
                                        }
                                    }

                                    randomNum=(int)(Math.random()*deck.size());//randomly pick a noun card from the deck
                                    while(deck.get(randomNum).getNoun()==false){//if an adjective card is selected, try again
                                        randomNum = (int)(Math.random()*deck.size());
                                    }
                                    for(ClientThread c: players){//show all players what the card is
                                        c.getWriter().println("/prompt/"+deck.get(randomNum).getText());

                                        if(c.getCzar()){
                                            c.getWriter().println("/czar/");
                                        }else{
                                            c.getWriter().println("/pick card/");//prompt users for card
                                        }
                                    }

                                } else if (state == 2) {//players have all selected their cards -- reveal cards, now wait for czar to pick winner
                                    //reveal cards
                                    //prompt czar to select cards
                                } else if (state == 3) {//czar has chosen winner, reveal winner and wait for everyone to say okay
                                    //
                                }

                                for (ClientThread c : players) {
                                    c.setReady(false);
                                }
                            }
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            System.out.println("game thread ended");
        }
    }
}
