/**
 * Created by Elizabeth Ip on 2017-01-16.
 */
import java.io.*;
import java.util.ArrayList;

public class MessageThread extends Thread{
    private Game game;
    private static Queue <String> queue;
    private static ArrayList<Card> deck;

    MessageThread(String s){
        game=new Game(s);
        queue=new Queue<String>();
    }

    public void run(){
        User czar;
        int readyPlayers=0;
        int gameState=0;
        String msg;
        String sender;
        //checks message queue
        while(!queue.isEmpty()){
            try {
                gameState=game.getState();
                msg = queue.dequeue();
                if (msg.contains("/chat/" )) {
                    for (User u : game.getPlayers()) {
                        u.getWriter().println(msg);
                    }
                }
                if (msg.contains("/ready/")) {
                    readyPlayers++;
                    if(readyPlayers==game.getNumPlayers()){
                        if(game.getState()==0){//if waiting to start game
                            game.getPlayers().get((int)(Math.random()*(game.getPlayers().size()))).setCzar(true);//set who is czar
                        }else if(game.getState()==1){//if waiting to start round
                            for(User u: game.getPlayers()){//send cards to each person
                                u.setHand(getHand(deck,u));
                                for(Card c: u.getHand()) {
                                    u.getWriter().println("/card/" + c.getText());
                                    u.getWriter().flush();
                                }
                                u.getWriter().println("/waiting for card/");
                            }
                            game.setState(2);
                        }else if(game.getState()==2){//if waiting for people to pick cards
                            if(msg.contains("/card/")){
                                String card=msg.substring(msg.indexOf("/card/")+5);
                            }
                        }else if(game.getState()==4){
                            game.setState(1);
                        }
                    }
                }
            }catch(Exception e){
                System.out.println(e);
            }

        }
    }

    //picks random cards from the deck to form a hand
    public ArrayList<Card> getHand (ArrayList<Card> d, User u){
        int handSize=7;
        int deckSize=d.size();
        int randomNumber;
        Card card;
        ArrayList<Card> hand=new ArrayList<Card>();

        //loop as many cards as needed
        for(int i=0;i<handSize;i++){
            randomNumber=(int)(Math.random()*deckSize);
            card=deck.get(randomNumber);//draw card from deck

            while(!card.getUser().equals(null)){//check if card is already in another user's hand
                randomNumber=(int)(Math.random()*deckSize);//if so, redraw card from deck
                card=deck.get(randomNumber);
            }

            card.setUser(u);//once card is legit, change user ID to current user
            hand.add(card);//add card to user's hand
        }

        return hand;
    }//end getHand

    public Game getGame(){
        return game;
    }
    public void setGame(Game g){
        game=g;
    }
    public void setQueue(Queue<String> q){
        queue=q;
    }
    public Queue<String> getQueue(){
        return queue;
    }
}
