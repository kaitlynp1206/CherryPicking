/**
 * Created by Elizabeth Ip on 2017-01-17.
 */
public class ChatObject {
    private String sender;
    private String message;

    ChatObject(String u, String s){
        sender=u;
        message=s;
    }

    public String getSender(){
        return sender;
    }

    public String getMessage(){
        return message;
    }

}