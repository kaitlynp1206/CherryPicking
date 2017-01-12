import java.io.*;
import java.net.Socket;

/**
 * Created by Elizabeth Ip on 2017-01-08.
 */
public class Client {
    private static Socket socket;
    private static PrintWriter writer;
    private static BufferedReader reader;
    private static BufferedReader consoleReader;

    public static void main (String args[]){
        new Client().go();
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
