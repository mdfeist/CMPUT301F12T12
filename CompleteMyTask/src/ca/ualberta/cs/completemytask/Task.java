package ca.ualberta.cs.completemytask;

import java.util.GregorianCalendar;

public class Task {
	
	boolean shared;
	boolean complete;
	
	String name;
	String description;
	
	GregorianCalendar date;
	
	Task() {
		this("New Task", "No Description");
	}
	
	Task(String name, String description) {
		this.shared = false;
		this.complete = false;
		
		this.name = name;
		this.description = description;
		
		this.date = new GregorianCalendar();
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

	public GregorianCalendar getDate() {
		return date;
	}
}