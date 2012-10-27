package ca.ualberta.cs.completemytask;

import android.app.Activity;
import android.content.SharedPreferences;

public class Settings {
	
	public static final String PREFS_NAME = "CompleteMyTaskPrefsFile";
	
	private static Settings instance = new Settings();
	
	private User user;
	
	private Settings() {
		this.user = null;
	}
	
	public static Settings getInstance() {
		return instance;
	}
	
	public void load(Activity activity) {
		// Restore preferences
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String userName = settings.getString("username", "Unknown");
		
		if (!userName.equals("Unknown")) {
			this.user = new User(userName);
		}
	}
	
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
	
	public boolean hasUser() {
		if (user != null) {
			return true;
		}
		
		return false;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public boolean setUserName(String name) {
		if (!hasUser()) {
			return false;
		}
		
		this.user.setUserName(name);
		
		return true;
	}
	
	public String getUserName() {
		if (!hasUser()) {
			return null;
		}
		
		return this.user.getUserName();
	}
}
