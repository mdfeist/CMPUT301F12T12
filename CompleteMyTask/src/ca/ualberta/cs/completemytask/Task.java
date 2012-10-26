package ca.ualberta.cs.completemytask;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Stores a information about a task.
 * 
 * @author Michael Feist
 * 
 */

public class Task extends UserData {

	private static final String TAG = "Task";

	private boolean shared;
	private boolean complete;
	private boolean sync;

	private String name;
	private String description;

	private String id;

	Task() {
		this("New Task", "No Description");
	}

	Task(String name, String description) {
		super();
		this.shared = false;
		this.complete = false;
		this.sync = true;

		this.id = null;

		this.name = name;
		this.description = description;

	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
		this.sync = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.sync = true;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		this.sync = true;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	public boolean needsSync() {
		return this.sync && this.shared;
	}
	
	public void syncFinished() {
		this.sync = false;
	}

	public String toString() {

		String task = String.format(
				"Task: \n\t Name: %s\n\t Description: %s\n\t Id: %s \n",
				this.name, this.description, this.id);

		return task;
	}
	
	public String toJSON() {
		String save = String
				.format("content={\"type\":\"Task\",\"name\":\"%s\",\"description\":\"%s\"}",
						this.name, this.description);
		
		return save;
	}
}
