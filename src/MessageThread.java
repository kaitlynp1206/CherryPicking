/**
 * Created by Elizabeth Ip on 2017-01-16.
 */
public class MessageThread extends Thread{
    private Game game;
    Queue <String> queue;

    MessageThread(String s){
        game=new Game(s);
        queue=new Queue();
    }

    public void run(){
        String msg;
        //checks message queue
        while(!queue.isEmpty()){
            msg=queue.dequeue();

        }
    }

    public Game getGame(){
        return game;
    }
    public void setGame(Game g){
        game=g;
    }
}