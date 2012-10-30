package ca.ualberta.cs.completemytask;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * A singleton to handle all the database oppertations.
 * 
 * @author Michael Feist
 *
 */
public class DatabaseManager {
	
	// For testing Don't use ///////////////////////
	public static boolean testSyncComplete = false;
	///////////////////////////////////////////////
	
	private static final String TAG = "DatabaseManager";
	private static final String DATABASE_URL = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T12/";
	private static DatabaseManager instance = null;
	
	private boolean hasSynced;
	
	private WebService webService;
	
	/**
	 * Template for data classes
	 * @author Michael Feist
	 *
	 */
	public class Data {
		String parentID;
		
		public String getParentId() {
			return this.parentID;
		}
	}
	
	/**
	 * Holds the information of a photo
	 * from the database;
	 * @author Michael Feist
	 *
	 */
	public class DataPhoto extends Data {
		public MyPhoto photo;
		
		public DataPhoto(String parentID, MyPhoto photo) {
			this.parentID = parentID;
			this.photo = photo;
		}
		
		public MyPhoto getPhoto() {
			return this.photo;
		}
	}
	
	/**
	 * Holds the information of an audio
	 * file from the database;
	 * @author Michael Feist
	 *
	 */
	public class DataAudio extends Data {
		public MyAudio audio;
		
		public DataAudio(String parentID, MyAudio audio) {
			this.parentID = parentID;
			this.audio = audio;
		}
		
		public MyAudio getAudio() {
			return this.audio;
		}
	}
	
	/**
	 * Data
	 */
	private Map<String, Task> foundTasks;
	private Map<String, DataPhoto> foundPhotos;
	private Map<String, DataAudio> foundAudio;

	protected DatabaseManager() {
		this.hasSynced = false;
		
		this.foundTasks = new HashMap<String, Task>();
		this.foundPhotos = new HashMap<String, DataPhoto>();
		this.foundAudio = new HashMap<String, DataAudio>();
		
		this.webService = new WebService(DATABASE_URL);
	}

	/**
	 * Returns the singleton's instance.
	 * 
	 * @return instances
	 */
	public static DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
	}
	
	/**
	 * Gets the web url of the database.
	 * @return URL as string
	 */
	public static String getDatabaseURL() {
		return DATABASE_URL;
	}
	
	/**
	 * Sorts and stores all the different
	 * data types currently in the database.
	 * For example all the tasks will go into
	 * one array and all the images into another.
	 */
	public void getData() {
		String response = this.webService.getJSONList();
		
		if (response == null) {
			Log.w(TAG, "Failed to get the list from database");
			return;
		}
		
		JSONArray jsonlist;
		String id = null;
		
		try {
			jsonlist = new JSONArray(response);

			for (int i = 0; i < jsonlist.length(); ++i) {
				JSONObject idObject = jsonlist.getJSONObject(i);
				id = idObject.getString("id");
				getData(id);
			}

		} catch (JSONException e) {
			Log.w(TAG, e.toString());
		}
	}
	
	/**
	 * Checks if the object with the given id
	 * has already been retrieved by the database.
	 * @param id of object
	 * @return true if already retrieved
	 */
	private boolean has(String id) {
		if (this.foundTasks.containsKey(id)) {
			return true;
		}
		
		if (this.foundPhotos.containsKey(id)) {
			return true;
		}
		
		if (this.foundAudio.containsKey(id)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Retrieves an object from the database.
	 * @param id of object in database
	 */
	public void getData(String id) {
		// Check if already found
		if(!has(id)) {
			JSONObject data = this.webService.getJSONObjectWithID(id);
			
			if (data != null) {
				String type = null;
				try {
					type = data.getString("type");
				} catch (JSONException e) {
					Log.w(TAG, "Unable to get type in getData");
					return;
				}
				
				if (type.equals("Task")) {
					Task task = decodeTask(data);
					task.setId(id);
					this.foundTasks.put(id, task);
				}
			}
		}
	}
	
	/**
	 * From the given JSONObject retrive the needed
	 * info for the task.
	 * @param JSON data
	 */
	private Task decodeTask(JSONObject data) {
		String userName = "Unknown";
		String name = "Unknown";
		String description = "Unknown";
		
		boolean needsComment = false;
		boolean needsPhoto = false;
		boolean needsAudio = false;
		
		boolean complete = false;
		
		try {
			name = data.getString("name");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get task name.");
			name = "Unknown";
		}
		
		try {
			description = data.getString("description");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get task description.");
			description = "Unknown";
		}		
		
		try {
			userName = data.getString("user");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get user.");
			userName = "Unknown";
		} 
		
		try {
			needsComment = data.getBoolean("needsComment");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get needsComment.");
			needsComment = false;
		} 
		
		try {
			needsPhoto = data.getBoolean("needsPhoto");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get needsPhoto.");
			needsPhoto = false;
		} 
		
		try {
			needsAudio = data.getBoolean("needsAudio");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get needsAudio.");
			needsAudio = false;
		} 
		
		try {
			complete = data.getBoolean("isComplete");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get completion.");
			complete = false;
		} 
		
		User user = new User(userName);
		
		Task task = new Task(name, description);
		task.setUser(user);
		task.setRequirements(needsComment, needsPhoto, needsAudio);
		task.setPublic(true);
		task.setComplete(complete);
		task.setLocal(false);
		task.syncFinished();
		
		return task;
	}
	
	/**
	 * Syncs a task based on it's location in the TaskManager.
	 * 
	 * @param Position of task in TaskManager
	 */
	public void syncTaskToDatabase(int position) {
		Task task = TaskManager.getInstance().getTaskAt(position);
		syncTaskToDatabase(task);
	}
	
	/**
	 * Syncs the given task with the database.
	 * 
	 * @param A task
	 */
	public void syncTaskToDatabase(Task task) {
		
		if (task.needsSync()) {
			
			String action = null;
			
			if (task.getId() == null) {
				action = "post";
			} else {
				action = "update";
			}
			
			String save = String.format("action=%s&content=%s&id=%s", 
					action, task.toJSON(), task.getId());
			
			String response = this.webService.httpRequest(save);
			
			if (response == null) {
				return;
			}
			
			JSONObject json;
			
			try {
				json = new JSONObject(response);
				task.setId(json.getString("id"));
				this.foundTasks.put(task.getId(), task);
			} catch (JSONException e) {
				Log.w(TAG, "Failed to get id.");
			}
			
			task.syncFinished();
		}
	}
	
	/**
	 * Sync all tasks with the database.
	 */
	public void syncDatabase() {
		testSyncComplete = false;
		
		// At start get all tasks from database
    	if (!hasSynced) {
    		for (Task t : TaskManager.getInstance().getTaskArray()) {
    			if (t.getId() != null) {
    				this.foundTasks.put(t.getId(), t);
    			}
    		}
    		
    		getData();
    		
    		for (Task task : this.foundTasks.values()) {
    		    TaskManager.getInstance().addTask(task);
    		}
    		
    		hasSynced = true;
		}
    	
    	// Sync all tasks
    	for(Task t : TaskManager.getInstance().getTaskArray()) {
    		Log.v(TAG, "Syncing Task: " + t.getName());
			this.syncTaskToDatabase(t);
		}	
    	
    	testSyncComplete = true;
	}
}
