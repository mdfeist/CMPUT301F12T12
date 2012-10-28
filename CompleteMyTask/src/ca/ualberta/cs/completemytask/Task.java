package ca.ualberta.cs.completemytask;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores a information about a task.
 * 
 * @author Michael Feist
 * 
 */

@SuppressWarnings("serial")
public class Task extends UserData {

	private static final String TAG = "Task";

	private boolean shared;
	private boolean complete;
	private boolean sync;
	private boolean local;

	private String name;
	private String description;

	private String id;
	
	private boolean needsComment;
	private boolean needsPhoto;
	private boolean needsAudio;
	
	private List<Comment> comments;

	Task() {
		this("New Task", "No Description");
	}

	Task(String name, String description) {
		super();
		this.shared = false;
		this.complete = false;
		this.sync = true;
		this.local = true;

		this.id = null;

		this.name = name;
		this.description = description;
		
		this.needsComment = false;
		this.needsPhoto = false;
		this.needsAudio = false;
		
		comments = new ArrayList<Comment>();

	}
	
	/**
	 * Get the number of comments contained in this task.
	 * 
	 * @return Number of comments
	 */
	public int getNumberOfComments() {
		return this.comments.size();
	}
	
	/**
	 * Add a comment to this task.
	 * @param A comment
	 */
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
	
	/**
	 * Get the comment at the given position.
	 * 
	 * @param The position in the comments array.
	 * @return The comment at that position.
	 */
	public Comment getCommentAt(int position) {
		return this.comments.get(position);
	}
	
	/**
	 * Set whether or not this task should be saved locally.
	 * @param is local
	 */
	public void setLocal(boolean local) {
		this.local = local;
	}
	
	/**
	 * Should the task be saved to the local storage. 
	 * 
	 * @return true if it should be saved locally
	 */
	public boolean isLocal() {
		return this.local;
	}
	
	/**
	 * Set whether or not this task should shared with
	 * the public.
	 * 
	 * @param true if it should shared with public
	 */
	public void setPublic(boolean shared) {
		this.shared = shared;
	}
	
	/**
	 * Should this be shared with the public.
	 * 
	 * @return true if it should shared with public
	 */
	public boolean isPublic() {
		return shared;
	}
	
	/**
	 * Set if this task is completed or not.
	 * 
	 * @param true if complete
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
		this.sync = true;
	}
	
	/**
	 * Check if this task was completed.
	 * 
	 * @return true if completed
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * Set the name of the task.
	 * 
	 * @param name of task
	 */
	public void setName(String name) {
		this.name = name;
		this.sync = true;
	}
	
	/**
	 * Gets the given name of the task.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the description of the task.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
		this.sync = true;
	}
	/**
	 * Gets the given description of the task.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
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
	public String getId() {
		return this.id;
	}
	
	/**
	 * Set what this task requires.
	 * 
	 * @param comment
	 * @param photo
	 * @param audio
	 */
	public void setRequirements(boolean comment, boolean photo, boolean audio) {
		this.needsComment = comment;
		this.needsPhoto = photo;
		this.needsAudio = audio;
	}
	
	/**
	 * Checks if the task needs a comment.
	 * @return true if task needs a comment
	 */
	public boolean needsComment() {
		return this.needsComment;
	}

	/**
	 * Checks if the task needs a photo.
	 * @return true if task needs a photo
	 */
	public boolean needsPhoto() {
		return this.needsPhoto;
	}
	
	/**
	 * Checks if the task needs a audio stream.
	 * @return true if task needs a audio stream
	 */
	public boolean needsAudio() {
		return this.needsAudio;
	}
	
	/**
	 * Check if the task needs to be synchronized with the database.
	 * @return true if task needs to be synchronized with the database
	 */
	public boolean needsSync() {
		return this.sync && this.shared;
	}
	
	/**
	 * Call when finish synchronizing the task with the database.
	 */
	public void syncFinished() {
		this.sync = false;
	}

	/**
	 * Returns the task as a string.
	 * @return A string
	 */
	public String toString() {
		
		String yes = "YES";
		String no = "NO";
		
		String publicTask = no;
		
		String comments = no;
		String photos = no;
		String audios = no;
		
		String userName = "Unknown";
		
		if (needsComment) {
			comments = yes;
		}
		
		if (needsPhoto) {
			photos = yes;
		}
		
		if (needsAudio) {
			audios = yes;
		}
		
		if (shared) {
			publicTask = yes;
		}
		
		if (hasUser()) {
			userName = getUser().getUserName();
		}

		String task = String.format(
				"Task: \n\t " +
				"Name: %s\n\t " +
				"Description: %s\n\t " +
				"Created By: %s\n\t " +
				"Id: %s \n\t " +
				"Public: %s \n\n\t " +
				"Requirements: \n\t\t " +
				"Needs Comments: %s \n\t\t Needs Photos: %s \n\t\t Needs Audio: %s \n\t",
				this.name, this.description,
				userName,
				this.id, publicTask,
				comments, photos, audios);

		return task;
	}
	
	/**
	 * Gets the task as a string in JSON form.
	 * 
	 * @return String in JSON form
	 */
	public String toJSON() {
		
		JSONObject json = new JSONObject();
		
		String userName = "Unknown";
		
		if (hasUser()) {
			userName = getUser().getUserName();
		}
		
        try {
			json.put( "type", "Task");
			json.put( "name", this.name);
			json.put( "description", this.description);
			json.put( "user", userName);
			json.put( "needsComment", this.needsComment);
			json.put( "needsPhoto", this.needsPhoto);
			json.put( "needsAudio", this.needsAudio);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
        Log.v(TAG, "JSON: " + json.toString());
        
		return json.toString();
	}
}
