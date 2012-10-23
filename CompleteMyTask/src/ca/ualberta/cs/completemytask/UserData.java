package ca.ualberta.cs.completemytask;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Stores information about a user. 
 * Used in tasks and content.
 * 
 * @author Michael Feist
 *
 */

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
	
	public String getDateAsString() {
		String strDate = String.format("%d-%d-%d", 
				this.date.get(Calendar.YEAR), 
				this.date.get(Calendar.MONTH) + 1,
				this.date.get(Calendar.DAY_OF_MONTH));
		
		return strDate;
	}
}
