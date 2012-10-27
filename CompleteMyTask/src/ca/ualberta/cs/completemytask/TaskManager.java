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
	/*
  
   file = new File(getFilesDir(), "batteryData");
   
	public void save(File file) {
		try {
			// use buffering
			OutputStream fileStream = new FileOutputStream(file);
			OutputStream buffer = new BufferedOutputStream(fileStream);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try {
				output.writeObject(this.taskList);
			} finally {
				output.close();
			}
		} catch (IOException e) {
			// Unable to create file
	        Log.w("ExternalStorage", "Error writing " + file, e);
		}
	}
	
	public void load(File file) {
		try {
			// use buffering
			InputStream fileStream = new FileInputStream(file);
			InputStream buffer = new BufferedInputStream(fileStream);
			ObjectInput input = new ObjectInputStream(buffer);
			try {
				// deserialize the List
				@SuppressWarnings("unchecked")
				List<Task> readObject = (List<Task>) input.readObject();
				this.taskList = readObject;
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
}
