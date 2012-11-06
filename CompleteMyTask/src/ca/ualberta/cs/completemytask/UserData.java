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
	
	User user;
	GregorianCalendar date;
	
	protected String id;
	protected String parentID;
	protected boolean sync;
	
	UserData() {
		this(null);
	}
	
	UserData(User user) {
		this.user = user;
		this.id = null;
		this.sync = true;
		
		this.parentID = null;
		
		date = new GregorianCalendar();
	}
	
	/**
	 * Set the id of the task.
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the id of the task.
	 * @return id
	 */
	public String getParentId() {
		return this.parentID;
	}
	
	/**
	 * Set the id of the task.
	 * @param id
	 */
	public void setParentId(String id) {
		this.parentID = id;
	}
	
	/**
	 * Gets the id of the task.
	 * @return id
	 */
	public String getId() {
		return this.id;
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
	
	abstract public String toJSON();
}
