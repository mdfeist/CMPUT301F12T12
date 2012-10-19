package ca.ualberta.cs.completemytask;

public class Task {
	
	boolean shared;
	boolean complete;
	String name;
	String description;
	
	Task() {
		this.shared = false;
		this.complete = false;
		this.name = "New Task";
		this.description = "No Description";
	}
}
