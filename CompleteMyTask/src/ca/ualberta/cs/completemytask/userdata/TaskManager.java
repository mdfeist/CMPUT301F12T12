package ca.ualberta.cs.completemytask.userdata;

import java.util.*;


//import android.util.Log;

/**
 * A singleton to manage all the tasks.
 * 
 * @author Michael Feist
 *
 */
public class TaskManager {
	
	private static TaskManager instance = null;
	
	//private static final String TAG = "TaskManager";
	private List<Task> tasks;
	
	private int currentPosition;

	protected TaskManager() {
		this.tasks = new ArrayList<Task>();
		this.currentPosition = -1;
	}

	/**
	 * Returns the singleton's instance.
	 * @return instances
	 */
	public static TaskManager getInstance() {
		if (instance == null) {
			instance = new TaskManager();
		}
		return instance;
	}
	
	/**
	 * Returns the number of tasks.
	 * @return number of tasks
	 */
	public int size() {
		return this.tasks.size();
	}
	
	/**
	 * Add a task to the task manager.
	 * @param task
	 * @return index of task
	 */
	public int addTask(Task task) {
		
		if(!this.tasks.contains(task)) {
			this.tasks.add(task);
		}
		
		return this.tasks.indexOf(task);
	}
	
	/**
	 * Sort tasks using TaskComparator
	 */
	public void sort() {
		TaskComparator comparator = new TaskComparator();
		Collections.sort(this.tasks, comparator);
	}
	
	/**
	 * Returns the task at the given position.
	 * @param position of task
	 * @return task at postion
	 */
	public Task getTaskAt(int position) {
		return this.tasks.get(position);
	}
	
	/**
	 * Removes the task at the given position.
	 * @param position of task
	 */
	public void removeTaskAt(int position) {
		this.tasks.remove(position);
	}
	
	/**
	 * Sets the current task that the user is viewing.
	 * @param position of task
	 */
	public void setCurrentTaskPosition(int position) {
		this.currentPosition = position;
	}
	
	/**
	 * Gets the current task that the user is viewing.
	 * @return position of task
	 */
	public int getCurrentTaskPosition() {
		return this.currentPosition;
	}
	
	/**
	 * Returns a list with all the tasks.
	 * @return List<Task>
	 */
	public List<Task> getTaskArray() {
		return tasks;
	}
	
	public void createFakeTable(int local, int global) {
    	for (int i = 0; i < local; i ++) {
	    	
	        // create a new Task
	        Task task = new Task("My Task " + i, "");
	        
	        addTask(task);
	        
    	}
    	
    	for (int i = 0; i < global; i ++) {
	    	
	        // create a new Task
	        Task task = new Task("Public Task " + i, "");
	        task.setPublic(true);
	        
	        TaskManager.getInstance().addTask(task);
	        
    	}
    }
}
