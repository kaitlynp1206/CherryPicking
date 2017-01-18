import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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
    private static ObjectInputStream reader;
    private static BufferedReader consoleReader;
    static String username;
    static String groupName;
    static boolean authenticatedName = false;
    static boolean authenticatedGroup = false;
    static volatile boolean clientRunning;
    

    public static void main (String args[]){
        new Client().go();
    }

    public void go(){
        String text="";

        try{
            socket=new Socket("localhost", 4444);
            consoleReader=new BufferedReader(new InputStreamReader(System.in));
            reader = new ObjectInputStream(socket.getInputStream());
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            ServerConnectionThread t=new ServerConnectionThread(); //make a new thread to check for messages from the server
            t.start(); //start the frickin thing

            while((text=consoleReader.readLine())!=null){ //constantly check for keyboard input
                writer.println("/chat/"+username+": "+text); //send it
            }

        }catch(IOException e){
            System.out.println(e);
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
        ChatObject msg;
        String name;
        Scanner input=new Scanner(System.in);

        //constructor
        ServerConnectionThread(){
            while(!loggedIn) {
                try {
                    while ((msg=(ChatObject)reader.readObject())!=null) {
                        if(msg.getMessage().contains("/send username/")){
                            System.out.print("enter a username: ");
                            name=input.nextLine();
                            writer.println("/username check/"+name);
                        }else if(msg.getMessage().contains("/legit name/")){//prompt for game name after username passes
                            username=name;
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
                        }else if(msg.getMessage().contains("/legit group name/")){
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
                    if ((msg=(ChatObject)reader.readObject())!=null) {
                        System.out.println(msg.getMessage());//print the message from server
                    }
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }
}
