/**
 * Created by Elizabeth Ip on 2017-01-11.
 */
import java.net.Socket;

public class User {
    private Socket socket;
    private String username;
    private String groupName;
    private int awesomePoints = 0;
    boolean czar = false;
	
	public void setCzar(){
		czar=true;
	}
	
	public boolean isCzar(){
		return czar;
	}
	
    public void endCzarTurn(){
        czar=false;
    }
    
    User(Socket socket, String username){
        this.socket=socket;
        this.username=username;
    }

    public void setSocket(Socket socket){
        this.socket=socket;
    }
    
    public Socket getSocket(){
        return socket;
    }

    public void setUsername(String username){
        this.username=username;
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setGroupName(String groupName){
        this.groupName=groupName;
    }
    
    public String getGroupName(){
        return groupName;
    }
    
    public void addPoints(){
        awesomePoints++;
    }
    
    public String getScoreDisplay(){
        return (username + ": " + awesomePoints);
    }
}