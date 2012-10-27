package ca.ualberta.cs.completemytask;

public class User {
	private String userName = null;
	
	User() {
	}
	
	User(String name) {
		this.userName = name;
	}
	
	public void setUserName(String name) {
		this.userName = name;
	}
	
	public String getUserName() {
		return this.userName;
	}
}
