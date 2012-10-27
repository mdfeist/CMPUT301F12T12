package ca.ualberta.cs.completemytask;

import java.util.*;

import android.util.Log;

/**
 * A singleton to manage all the tasks.
 * 
 * @author Michael Feist
 *
 */

public class TaskManager {
	
	private static TaskManager instance = null;
	
	private static final String TAG = "TaskManager";
	private List<Task> tasks;
	
	private boolean loadedData;

	protected TaskManager() {
		this.tasks = new ArrayList<Task>();
		this.loadedData = false;
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
	
	public List<Task> getTaskArray() {
		return tasks;
	}
	
	public void loadLocalData() {
		if (!loadedData) {
			Log.v(TAG, "Loading local data");
			
			createFakeTable();
			this.loadedData = true;
		}
	}
	
	private void createFakeTable() {
    	for (int i = 0; i < 2; i ++) {
	    	
	        // create a new Task
	        Task task = new Task("My Task " + i, "");
	        
	        TaskManager.getInstance().addTask(task);
	        
    	}
    	/*
    	for (int i = 0; i < 5; i ++) {
	    	
	        // create a new Task
	        Task task = new Task("Public Task " + i, "");
	        task.setShared(true);
	        
	        TaskManager.getInstance().addTask(task);
	        
    	}
    	*/
    }
}
