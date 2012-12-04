package ca.ualberta.cs.completemytask.settings;

import android.app.Activity;

public class SettingsItem {
	private String title;
	private int linkid;
	private Class<? extends Activity> new_activity;
	
	public SettingsItem(String title, int linkid, Class<? extends Activity> new_activity) {
		this.title = title;
		this.linkid = linkid;
		this.new_activity = new_activity;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getLink() {
		return this.linkid;
	}
	
	public void setLink(int id) {
		this.linkid = id;
	}
	
	public Class<? extends Activity> getActivity() {
		return this.new_activity;
	}
}
