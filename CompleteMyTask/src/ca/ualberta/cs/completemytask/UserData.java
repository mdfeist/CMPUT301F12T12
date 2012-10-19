package ca.ualberta.cs.completemytask;

import java.util.GregorianCalendar;

public class UserData {
	
	String user;
	GregorianCalendar date;
	
	UserData() {
		this.user = "Unknown";
		date = new GregorianCalendar();
	}
	
	UserData(String user) {
		this.user = user;
		date = new GregorianCalendar();
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public GregorianCalendar getDate() {
		return this.date;
	}
}
