/**
 * Created by Elizabeth Ip on 2017-01-09.
 */
public class Card {
    private String text;
    private String player;
    private boolean isNoun;
    private int cardID;

    Card(String text){
        this.text=text;
    }

    Card (String text, String user, boolean isNoun, int cardID){
        this.text=text;
        this.player=user;
        this.isNoun=isNoun;
        this.cardID=cardID;
    }

    //COPY THIS CONSTRUCTOR
    Card (String text, int cardID){
        this.text=text;
        this.cardID=cardID;
    }

    public String getText(){
        return this.text;
    }
    public void setText(String t){
        this.text=t;
    }

    public String getPlayer(){
        return this.player;
    }
    public void setPlayer(String p){
        this.player=p;
    }

    public boolean getNoun(){
        return isNoun;
    }
    public void setNoun(boolean n){
        this.isNoun=n;
    }


    public void setID(int i){
        cardID=i;
    }
    public int getID(){
        return cardID;
    }
}
