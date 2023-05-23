package application.model;

public class User
{
	//fields
	private String username =  "";
	private String password = "";
	private int avatar;
	private boolean status = false;
	
	//constructor
	public User(String username, String password, int avatar, boolean status) {
		this.username = username;
		this.password = password;
		this.avatar = avatar;
		this.status = status;
	}
	
	//methods
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getAvatar() {
		return avatar;
	}
	
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	
	public String getStatus() {
		String activity = "";
		
		if (this.status) {activity = "Online";}
		else {activity = "Offline";}
		
		return activity;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
}
