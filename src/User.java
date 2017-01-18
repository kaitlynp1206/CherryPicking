/**
 * Created by Elizabeth Ip on 2017-01-11.
 */
import java.net.Socket;
import java.io.*;

public class User {
    private Socket socket;
    private String name;
    private String groupName;
    private int awesomePoints;
    private boolean czar;
    private boolean ready;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private Queue<ChatObject> queue;

    User(Socket s, String n){
        socket=s;
        name=n;
        awesomePoints=0;
    }

    public void setQueue(Queue<ChatObject> q){
        queue=q;
    }
    public Queue<ChatObject> getQueue(){
        return queue;
    }

    public void setWriter(ObjectOutputStream w){
        writer=w;
    }
    public ObjectOutputStream getWriter(){
        return writer;
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

    public void setGroupName(String g){
        groupName=g;
    }
    public String getGroupName(){
        return groupName;
    }

    public void setSocket(Socket s){
        socket=s;
    }
    public Socket getSocket(){
        return socket;
    }

    public void setName(String n){
        name=n;
    }
    public String getName(){
        return name;
    }

    public void setPoints(int p){
        awesomePoints=p;
    }
    public int getPoints(){
        return awesomePoints;
    }

    public void setReader(ObjectInputStream r){
        reader=r;
    }
    public ObjectInputStream getReader(){
        return reader;
    }
}