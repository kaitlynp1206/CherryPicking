/**
 * Created by Elizabeth Ip on 2017-01-16.
 */
import java.io.*;

public class MessageThread extends Thread{
    private Game game;
    private static Queue <ChatObject> queue;

    MessageThread(String s){
        game=new Game(s);
        queue=new Queue<ChatObject>();
    }

    public void run(){
        ChatObject msg;
        //checks message queue
        while(!queue.isEmpty()){
            try {
                msg = queue.dequeue();
                if (msg.getMessage().contains("/chat/")) {
                    for (User u : game.getPlayers()) {
                        u.getWriter().writeObject(msg);
                    }
                }
                if (msg.getMessage().contains("/ready/")) {

                }
            }catch(Exception e){
                System.out.println(e);
            }

        }
    }

    public Game getGame(){
        return game;
    }
    public void setGame(Game g){
        game=g;
    }
    public void setQueue(Queue<ChatObject> q){
        queue=q;
    }
    public Queue<ChatObject> getQueue(){
        return queue;
    }
}
