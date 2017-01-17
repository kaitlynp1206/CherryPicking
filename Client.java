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
    private static String username;

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
                writer.println("/chat/"+username+": "+text); //send it
            }

        }catch(IOException e){
            System.out.println(e);
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
                    while (reader.ready()) {
                        msg=reader.readLine();
                        if(msg.contains("/send username/")){
                            System.out.print("enter a username: ");
                            name=input.nextLine();
                            writer.println("/username check/"+name);
                        }else if(msg.contains("/legit name/")){
                            username=name;
                            loggedIn=true;
                            System.out.println("you are now logged in. username: " + name);
                        }else if(msg.contains("/legit group name/")){
                            loggedIn=true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void run(){
            System.out.print("/client/ ready for input.");
            while(loggedIn){
                try {
                    if (reader.ready()) {
                        msg=reader.readLine();
                        System.out.println(msg);
                    }
                }catch(IOException e){
                    System.out.println(e);
                }
            }
        }
    }
}
