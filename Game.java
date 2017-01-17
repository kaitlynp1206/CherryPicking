/**
 * Created by Elizabeth Ip on 2017-01-16.
 */

import java.util.ArrayList;
public class Game {
    private String name;
    private int numPlayers;
    private ArrayList<User> players;
    private Queue<String> messages;
    private String state;
    //states
    // 0 = waiting to start game
    // 1 = waiting for czar to pick card
    // 2 = waiting for people to pick cards
    // 3 = waiting for czar to pick winning card
    // 4 = waiting for people to accept decision

    Game(String n){
        name=n;
        numPlayers=0;
        state="waiting for players";
    }

    public void addPlayer(){

    }

    public void setName(String n){
        name=n;
    }
    public String getName(){
        return name;
    }

    public void setPlayers(ArrayList<User> p){
        players=p;
    }
    public ArrayList<User> getPlayers(){
        return players;
    }

}
