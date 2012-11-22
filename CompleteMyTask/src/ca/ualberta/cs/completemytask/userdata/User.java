package ca.ualberta.cs.completemytask.userdata;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String userName = null;
	private String password = null;
	private String email = "";
	
	public User() {
	}
	
	public User(String name) {
		this.userName = name;
	}
	
	public User(String name, String email) {
		this.userName = name;
		this.email = email;
	}
	
	public void setUserName(String name) {
		this.userName = name;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setEmail(String email) {
		this.email= email;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
}
