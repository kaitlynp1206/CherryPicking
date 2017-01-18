/**
 * Created by Elizabeth Ip on 2017-01-10.
 */

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread{
    Socket clientSocket;
    int clientID=-1;
    boolean running=true;
    String msg;
    BufferedReader reader;
    PrintWriter writer;
    Queue<String> queue;

    //constructor
    ClientThread(Socket s, int i, Queue<String> q){
        clientSocket=s;
        clientID=i;
        queue=q;
    }

    public void run(){
        try{
            reader=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer=new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

            while ((msg=reader.readLine())!=null){
                System.out.println("client says: "+msg);
                queue.enqueue(msg);
            }

        }catch(IOException e){
            System.out.println(e);
        }
    }
}