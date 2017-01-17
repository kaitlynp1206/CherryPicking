import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Elizabeth Ip on 2017-01-08.
 */
public class Client {
    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static BufferedReader consoleReader;
    static String username;
    static String groupName;
    static boolean authenticatedName = false;
    static boolean authenticatedGroup = false;
    static volatile boolean clientRunning;
    
    
    public static void main (String args[]){
        clientRunning = true;
    	Client client = new Client();
    	client.go();
    }

    public static boolean authenticateUsername(String username) throws Exception{
    	return true;
    }
    
    public static boolean authenticateGroupExists(String groupName) throws Exception{
    	return true;
    }
    
    public static boolean authenticateGroupName(String groupName) throws Exception{
    	return true;
    }
    
    public void go(){
        String text="";

        try{
            socket=new Socket("localhost", 4444);
            writer=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true); //autoflush is my saviour
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            consoleReader=new BufferedReader(new InputStreamReader(System.in));
            Thread t = new Thread( new ServerConnectionThread()); //make a new thread to check for messages from the server
            t.start(); //start the frickin thing

            while((text=consoleReader.readLine())!=null){ //constantly check for keyboard input
                writer.println(text); //send it
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
        public void run(){
            boolean running=true;

            while(running){
                try {
                    if (reader.ready()) {
                        System.out.println(reader.readLine());
                    }
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }
}
