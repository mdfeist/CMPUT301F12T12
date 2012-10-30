package ca.ualberta.cs.completemytask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A text comment.
 * 
 * @author Michael Feist
 *
 */
@SuppressWarnings("serial")
public class Comment extends UserData implements UserContent<String> {

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

	@Override
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

}
