package ca.ualberta.cs.completemytask;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Stores information about a user's content. 
 * Used in tasks and content.
 * 
 * @author Michael Feist
 *
 */
@SuppressWarnings("serial")
public abstract class UserData implements Serializable {
	
	User user = null;
	GregorianCalendar date;
	
	UserData() {
		date = new GregorianCalendar();
	}
	
	UserData(User user) {
		this.user = user;
		date = new GregorianCalendar();
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public boolean hasUser() {
		if (user != null) {
			return true;
		}
		
		return false;
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
	
	abstract public String toJSON();
}
