package ca.ualberta.cs.completemytask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
	
	private int currentPosition;
	
	private File localDataFile;

	protected TaskManager() {
		this.tasks = new ArrayList<Task>();
		this.loadedData = false;
		this.currentPosition = -1;
		this.localDataFile = null;
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
	
	public void setLocalDataFile(File file) {
		this.localDataFile = file;
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
	
	/**
	 * Loads tasks that were saved locally.
	 * @param file
	 */
	public void loadLocalData(File file) {
		if (!loadedData) {
			Log.v(TAG, "Loading local data");
			/*
			if (file.exists()) {
				try {
					// use buffering
					InputStream fileStream = new FileInputStream(file);
					InputStream buffer = new BufferedInputStream(fileStream);
					ObjectInput input = new ObjectInputStream(buffer);
					try {
						// deserialize the List
						@SuppressWarnings("unchecked")
						List<Task> readObject = (List<Task>) input.readObject();
						this.tasks.addAll(readObject);
					} finally {
						input.close();
					}
				} catch (ClassNotFoundException e) {
					Log.w("ExternalStorage", "Cannot perform input. Class not found. "
							+ file, e);
				} catch (IOException e) {
					Log.w("ExternalStorage", "Cannot perform input: " + file, e);
				}
			}
			*/
			this.loadedData = true;
		}
	}
	
	public void saveLocalData() {
		saveLocalData(this.localDataFile);
	}
	
	/**
	 * Save tasks to local storage.
	 * @param A local file
	 */
	public void saveLocalData(File file) {
		/*
		List<Task> saveTasks = new ArrayList<Task>();
		
		for (Task t : tasks) {
			if (t.isLocal()) {
				saveTasks.add(t);
			}
		}
		
		try {
			// use buffering
			OutputStream fileStream = new FileOutputStream(file);
			OutputStream buffer = new BufferedOutputStream(fileStream);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try {
				output.writeObject(saveTasks);
			} finally {
				output.close();
			}
		} catch (IOException e) {
			// Unable to create file
	        Log.w("ExternalStorage", "Error writing " + file, e);
		}
		*/
	}
	
	/**
	 * Delete all tasks from local storage.
	 * @param A local file
	 */
	public void deleteLocalData(File file) {
		/*
		// Delete file
		file.delete();
		
		// Delete from array
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).isLocal()) {
				tasks.remove(i);
			}
		}
		*/
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
