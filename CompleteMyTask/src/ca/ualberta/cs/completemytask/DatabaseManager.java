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
	 * Data
	 */
	private Map<String, Task> foundTasks;
	private Map<String, Comment> foundComments;
	private Map<String, MyPhoto> foundPhotos;
	private Map<String, MyAudio> foundAudio;

	protected DatabaseManager() {
		this.hasSynced = false;
		
		this.foundTasks = new HashMap<String, Task>();
		this.foundComments = new HashMap<String, Comment>();
		this.foundPhotos = new HashMap<String, MyPhoto>();
		this.foundAudio = new HashMap<String, MyAudio>();
		
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
		
		if (this.foundComments.containsKey(id)) {
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
				} else if (type.equals("Comment")) {
					Comment comment = decodeComment(data);
					comment.setId(id);
					this.foundComments.put(id, comment);
				}
			}
		}
	}
	
	/**
	 * From the given JSONObject retrieve the needed
	 * info for the task
	 * @param A JSONObject of the comment
	 * @return A comment
	 */
	private Comment decodeComment(JSONObject data) {
		String userName = "Unknown";
		String commentString = "";
		String parentID = "";
		
		try {
			userName = data.getString("user");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get user.");
			userName = "Unknown";
		}
		
		try {
			commentString = data.getString("comment");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get comment.");
			commentString = "";
		}
		
		try {
			parentID = data.getString("parentID");
		} catch (JSONException e) {
			Log.w(TAG, "Failed to get parentID.");
			parentID = "";
		}
		
		User user = new User(userName);
		Comment comment = new Comment();
		comment.setUser(user);
		comment.setContent(commentString);
		comment.setParentId(parentID);
		
		return comment;
	}
	
	/**
	 * From the given JSONObject retrive the needed
	 * info for the task.
	 * @param JSON data
	 * @return A task
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
	
	public void syncData(UserData data) {
		if (data.needsSync()) {
			
			String action = null;
			
			if (data.getId() == null) {
				action = "post";
			} else {
				action = "update";
			}
			
			String save = String.format("action=%s&content=%s&id=%s", 
					action, data.toJSON(), data.getId());
			
			String response = this.webService.httpRequest(save);
			
			if (response == null) {
				return;
			}
			
			JSONObject json;
			
			try {
				json = new JSONObject(response);
				data.setId(json.getString("id"));
			} catch (JSONException e) {
				Log.w(TAG, "Failed to get id.");
			}
			
			data.syncFinished();
		}
	}
	
	/**
	 * Syncs a task based on it's location in the TaskManager.
	 * @param Position of task in TaskManager
	 */
	public void syncTaskToDatabase(int position) {
		Task task = TaskManager.getInstance().getTaskAt(position);
		syncTaskToDatabase(task);
	}
	
	/**
	 * Syncs the given task with the database.
	 * @param A task
	 */
	public void syncTaskToDatabase(Task task) {
		
		if (task.needsSync()) {
			syncData(task);
		}
		
		// Sync Comments
		for (int i = 0; i < task.getNumberOfComments(); i++) {
			Comment comment = task.getCommentAt(i);
			comment.setParentId(task.getId());
			syncData(comment);
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
    		
    		hasSynced = true;
		}
    	
    	// Sync all tasks
    	for(Task t : TaskManager.getInstance().getTaskArray()) {
    		Log.v(TAG, "Syncing Task: " + t.getName());
			this.syncTaskToDatabase(t);
		}	
    	
    	getData();
    	
    	for (Comment comment : this.foundComments.values()) {
    		String parentID = comment.getParentId();
    		Task task = this.foundTasks.get(parentID);
    		
    		if (task != null) {
    			task.addComment(comment);
    		}
    	}
		
		for (Task task : this.foundTasks.values()) {
		    TaskManager.getInstance().addTask(task);
		}
    	
    	testSyncComplete = true;
	}
}
