import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.awt.event.*;
/**
 * Created by Elizabeth Ip on 2017-01-08.
 */
public class Client {
    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static BufferedReader consoleReader;
    private static User player;

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
    }

    class ServerConnectionThread extends Thread{
        boolean loggedIn=false;
        String msg;
        String name;
        Scanner input=new Scanner(System.in);

        //constructor
        ServerConnectionThread(){
            while(!loggedIn) {
                try {
                    while ((msg=reader.readLine())!=null){
                        System.out.println(msg);
                        if(msg.contains("/send username/")){
                            System.out.print("enter a username: ");
                            name=input.nextLine();
                            writer.println("/username check/"+name);
                        }else if(msg.contains("/legit name/")){//prompt for game name after username passes
                            player=new User(socket, name);
                            System.out.println("you are now logged in. username: " + name);
                            System.out.print("enter n to start a new game, or j to join an existing one");
                            name=input.nextLine();
                            if(name.equalsIgnoreCase("n")){
                                name.equals("/new game/");
                            }else if(name.equalsIgnoreCase("j")){
                                name.equals("/join game");
                            }
                            System.out.print("enter a group name: ");
                            name=name+input.nextLine();
                            writer.println(name);
                        }else if(msg.contains("/legit group name/")){
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
                            player.getHand().add(new Card(msg.substring(6)));
                        }
                        else if(msg.contains("/waiting for card/")){
                            for(Card c: player.getHand()){
                                System.out.println("card: "+c.getText());
                            }
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
}
