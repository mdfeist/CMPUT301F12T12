package ca.ualberta.cs.completemytask;

import java.util.*;

public class TaskManager {
	
	private static TaskManager instance = null;
	
	private List<Task> tasks;

	protected TaskManager() {
		// Exists only to defeat instantiation.
		this.tasks = new ArrayList<Task>();
	}

	public static TaskManager getInstance() {
		if (instance == null) {
			instance = new TaskManager();
		}
		return instance;
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
	}
}
