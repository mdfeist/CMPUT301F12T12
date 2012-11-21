package ca.ualberta.cs.completemytask;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * A text comment.
 * 
 * @author Michael Feist
 *
 */
public class Comment extends ChildUserData implements UserContent<String> {
	protected final String TAG = "Comment";
	
	String comment;
	
	public Comment() {
		this.comment = "Blank Comment";
	}
	
	public Comment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Returns the comment as a String.
	 * @return comment
	 */
	public String getContent() {
		return this.comment;
	}

	/**
	 * Set the comment.
	 * @param comment
	 */
	public void setContent(String comment) {
		this.comment = comment;
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		
		String userName = "Unknown";
		
		if (hasUser()) {
			userName = getUser().getUserName();
		}
		
        try {
			json.put( "type", "Comment");
			json.put( "user", userName);
			json.put( "comment", this.comment);
			json.put( "parentID", this.parentID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
		return json.toString();
	}
	
	/**
	 * From the given JSONObject retrieve the needed
	 * info for the task
	 * @param A JSONObject of the comment
	 * @return A comment
	 */
	public void decodeComment(JSONObject data) {
		String userName = "Unknown";
		String commentString = "";
		long parentID = 0;
		
		try {
			userName = data.getString("user");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get user.");
			userName = "Unknown";
		}
		
		try {
			commentString = data.getString("comment");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get comment.");
			commentString = "";
		}
		
		try {
			parentID = data.getLong("parentID");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get parentID.");
			parentID = 0;
		}
		
		User user = new User(userName);
		this.setUser(user);
		this.setContent(commentString);
		this.setParentId(parentID);
	}

}
