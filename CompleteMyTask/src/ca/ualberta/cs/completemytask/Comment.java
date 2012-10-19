package ca.ualberta.cs.completemytask;

public class Comment extends UserData implements UserContent<String> {

	String comment;
	
	Comment() {
		this.comment = "Blank Comment";
	}
	
	Comment(String comment) {
		this.comment = comment;
	}
	
	public String getContent() {
		return this.comment;
	}

	public void setContent(String comment) {
		this.comment = comment;
	}

}
