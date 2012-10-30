package ca.ualberta.cs.completemytask;

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
		// TODO Auto-generated method stub
		return null;
	}

}
