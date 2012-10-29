package ca.ualberta.cs.completemytask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
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

	private static final String TAG = "DatabaseManager";
	private static final String DATABASE_URL = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T12/";
	
	//For testing
	public static boolean testSyncComplete = false;
	
	private static DatabaseManager instance = null;
	
	private Map<String, Task> foundTasks;
	
	private boolean hasSynced;

	protected DatabaseManager() {
		this.hasSynced = false;
		this.foundTasks = new HashMap<String, Task>();
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
	
	public String getDatabaseURL() {
		return DATABASE_URL;
	}
	
	/**
	 * Lists the id's of the items in the database.
	 * 
	 * @return String in JSON form
	 */
	public String listDatabase() {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			String get = String
					.format("%s?action=list", DATABASE_URL);

			HttpGet request = new HttpGet(get);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpClient.execute(request, responseHandler);

			return response;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Get all the tasks that are in the database and 
	 * adds them to the TaskManager.
	 */
	private void getAllTasksFromDatabase() {
		
		JSONArray jsonlist;
		String id = null;
		
		String response = listDatabase();

		if (response == null) {
			Log.v(TAG, "Failed to get list from data base");
			return;
		}
		
		try {
			jsonlist = new JSONArray(response);

			for (int i = 0; i < jsonlist.length(); ++i) {
				JSONObject idObject = jsonlist.getJSONObject(i);
				id = idObject.getString("id");

				Log.v(TAG, "ID: " + id);
				
				if (!this.foundTasks.containsKey(id)) {
					Task task = getTaskFromDatabase(id);
	
					if (task != null) {
						TaskManager.getInstance().addTask(task);
					}
				}
			}

		} catch (JSONException e) {
			Log.w(TAG, e.toString());
		}

	}
	
	/**
	 * Loads the task with the given id from the database.
	 * 
	 * @param The task id
	 * @return The task with the given id
	 */
	private Task getTaskFromDatabase(String id) {
		
		Task task = null;
		
		if (this.foundTasks.containsKey(id)) {
			return this.foundTasks.get(id);
		}
		
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			String get = String
					.format("%s?action=get&id=%s", DATABASE_URL, id);
    		
    		HttpGet request = new HttpGet(get);
    		
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			
			String response = httpClient.execute(request,
					responseHandler);
			
			Log.v(TAG, "Response: " + response);
			
			JSONObject json;
			
			try {
				json = new JSONObject( response );
				JSONObject data = json.getJSONObject("content");
				
				String type = data.getString("type");
				
				if (type.equals("Task")) {
					
					String userName = "Unknown";
					String name = "Unknown";
					String description = "Unknown";
					
					boolean needsComment = false;
					boolean needsPhoto = false;
					boolean needsAudio = false;
					
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
					User user = new User(userName);
					
					task = new Task(name, description);
					task.setUser(user);
					task.setRequirements(needsComment, needsPhoto, needsAudio);
					task.setPublic(true);
					task.setId(id);
					task.setLocal(false);
					task.syncFinished();
					
					Log.v(TAG, task.toString());
				}
				
			} catch (JSONException e) {
				Log.w(TAG, "Failed to get list.");
			}        

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return task;
	}
	
	/**
	 * Inserts or updates depending on the given action a task
	 * into the database. The action can either be post or
	 * update.
	 * 
	 * @param Action
	 * @param A task
	 * @return http response
	 */
	private String insertIntoDatabase(String action, Task task) {
		
		HttpClient httpClient = new DefaultHttpClient();

		try {
			String post = String
					.format("%s?action=%s&summary=%s&description=%s",
							DATABASE_URL, action, "Task", task.getDateAsString());
			
			String save = String.format("content=%s&id=%s", task.toJSON(), task.getId());

			Log.v(TAG, action);
			Log.v(TAG, "Content: " + save);

			HttpPost request = new HttpPost(post);

			StringEntity se = new StringEntity(save);	
			
			request.setEntity(se);
			
			request.setHeader("Connection", "keep-alive");
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/x-www-form-urlencoded");
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpClient.execute(request, responseHandler);

			Log.v(TAG, "Response: " + response);
			
			return response;

		} catch (ClientProtocolException e) {
			Log.w(TAG, "ClientProtocolException: " + e.toString());
		} catch (IOException e) {
			Log.w(TAG, "IOException: " + e.toString());
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		return null;
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
			
			String response = insertIntoDatabase(action, task);
			
			if (response == null) {
				return;
			}
			
			JSONObject json;
			try {
				json = new JSONObject(response);
				
				task.setId(json.getString("id"));
				Log.v(TAG, "ID: " + task.getId());
				
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
    		getAllTasksFromDatabase();
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
