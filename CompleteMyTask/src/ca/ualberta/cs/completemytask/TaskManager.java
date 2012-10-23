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
	
	public int size() {
		return this.tasks.size();
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
		
		TaskComparator comparator = new TaskComparator();
		Collections.sort(this.tasks, comparator);
	}
	
	public Task getTaskAt(int position) {
		return this.tasks.get(position);
	}
}
