/**
 * Created by Elizabeth Ip on 2017-01-11.
 */
import java.net.Socket;
public class User {
    private Socket socket;
    private String name;

    User(Socket s, String n){
        socket=s;
        name=n;
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

}
