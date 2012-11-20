package ca.ualberta.cs.completemytask;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * A singleton to handle the settings of
 * of the application.
 * 
 * Currently only holds the apps user.
 * 
 * @author Michael Feist
 *
 */
public class Settings {
	
	public static final String PREFS_NAME = "CompleteMyTaskPrefsFile";
	public static final String DATA_NAME = "CompleteMyTaskData";
	
	private static Settings instance = new Settings();
	
	private User user;
	
	private Settings() {
		this.user = null;
	}
	
	/**
	 * Returns the singleton's instance.
	 * 
	 * @return instances
	 */
	public static Settings getInstance() {
		return instance;
	}
	
	/**
	 * Loads the application settings.
	 * @param activity
	 */
	public void load(Activity activity) {
		// Restore preferences
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String userName = settings.getString("username", "");
		String email = settings.getString("email", "");
		
		if (!userName.equals("")) {
			this.user = new User(userName, email);
		}
	}
	
	/**
	 * Saves the application settings.
	 * @param activity
	 */
	public void save(Activity activity) {
		if (hasUser()) {
			// We need an Editor object to make preference changes.
			// All objects are from android.context.Context
			SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("username", user.getUserName());

			// Commit the edits!
			editor.commit();
		}
	}
	
	/**
	 * Checks to see if there is a user.
	 * @return true if user is not null
	 */
	public boolean hasUser() {
		if (user != null) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gives the settings a user.
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Returns the user.
	 * @return user
	 */
	public User getUser() {
		return this.user;
	}
	
	/**
	 * Set the user's name to the given string.
	 * @param name
	 * @return true if there is a user
	 */
	public boolean setUserName(String name) {
		if (!hasUser()) {
			return false;
		}
		
		this.user.setUserName(name);
		
		return true;
	}
	
	/**
	 * Gives the user's name.
	 * @return user's name
	 */
	public String getUserName() {
		if (!hasUser()) {
			return null;
		}
		
		return this.user.getUserName();
	}
}
