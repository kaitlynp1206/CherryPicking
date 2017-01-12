
public class User {

	private String username;
	private String groupName;
	private int score=0;
	
	public void setUsername(String username){
		this.username=username;
	}
	
	public void setGroupName(String groupName){
		this.groupName=groupName;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getGroupName(){
		return groupName;
	}
	
	public void addScore(){
		score++;
	}
	
	public int getScore(){
		return score;
	}
	
	public String getScoreDisplay(){
		return (username + ": " + score);
	}
}
