package ca.ualberta.cs.completemytask;

/**
 * Stores a information about a task.
 * 
 * @author Michael Feist
 *
 */

public class Task extends UserData {
	
	boolean shared;
	boolean complete;
	
	String name;
	String description;
	
	Task() {
		this("New Task", "No Description");
	}
	
	Task(String name, String description) {
		super();
		this.shared = false;
		this.complete = false;
		
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
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
