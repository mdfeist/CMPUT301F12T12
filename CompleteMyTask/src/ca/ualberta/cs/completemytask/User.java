package ca.ualberta.cs.completemytask;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String userName = null;
	
	public User() {
	}
	
	public User(String name) {
		this.userName = name;
	}
	
	public void setUserName(String name) {
		this.userName = name;
	}
	
	public String getUserName() {
		return this.userName;
	}
}
