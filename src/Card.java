/**
 * Created by Elizabeth Ip on 2017-01-09.
 */
public class Card {
    private String text;
    private String user;
    private boolean isNoun;

    Card (String text, String user, boolean isNoun){
        this.text=text;
        this.user=user;
        this.isNoun=isNoun;
    }

    String getText(){
        return this.text;
    }
    void setText(String t){
        this.text=t;
    }

    String getUser(){
        return this.user;
    }
    void setUser(String u){
        this.user=u;
    }

    boolean getIsNoun(){
        return isNoun;
    }
    void setIsNoun(boolean n){
        this.isNoun=n;
    }
}
