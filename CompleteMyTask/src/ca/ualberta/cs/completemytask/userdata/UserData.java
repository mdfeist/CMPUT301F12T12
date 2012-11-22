package ca.ualberta.cs.completemytask.userdata;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Stores information about a user's content. 
 * Used in tasks and content.
 * 
 * @author Michael Feist
 *
 */
public abstract class UserData {
	
	User user;
	GregorianCalendar date;
	
	protected long id;
	protected long localid;
	protected boolean sync;
	
	UserData() {
		this(null);
	}
	
	UserData(User user) {
		this.user = user;
		this.sync = true;
		
		date = new GregorianCalendar();
	}
	
	/**
	 * Set the id of the task.
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Gets the id of the task.
	 * @return id
	 */
	public long getId() {
		return this.id;
	}
	
	public void setLocalId(long id) {
		this.localid = id;
	}
	
	public long getLocalId() {
		return this.localid;
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
	
	/**
	 * Check if the task needs to be synchronized with the database.
	 * @return true if task needs to be synchronized with the database
	 */
	public boolean needsSync() {
		return this.sync;
	}
	
	/**
	 * Call when finish synchronizing the task with the database.
	 */
	public void syncFinished() {
		this.sync = false;
	}
	
}
