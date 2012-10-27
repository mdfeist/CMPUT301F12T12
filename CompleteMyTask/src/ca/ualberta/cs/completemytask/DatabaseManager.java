package ca.ualberta.cs.completemytask;

import java.io.IOException;

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

public class DatabaseManager {

	private static final String TAG = "DatabaseManager";
	private static final String DATABASE_URL = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T12/";
	
	private static DatabaseManager instance = null;
	
	private boolean hasSynced;

	protected DatabaseManager() {
		this.hasSynced = false;
	}

	public static DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
	}
	
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

				Task task = getTaskFromDatabase(id);

				if (task != null) {
					TaskManager.getInstance().addTask(task);
				}
			}

		} catch (JSONException e) {
			Log.v(TAG, e.toString());
		}

	}
	
	private Task getTaskFromDatabase(String id) {
		
		Task task = null;
		
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
					String name = data.getString("name");
					String description = data.getString("description");
					
					task = new Task(name, description);
					task.setPublic(true);
					task.setId(id);
					
					Log.v(TAG, task.toString());
				}
				
			} catch (JSONException e) {
				Log.v(TAG, "Failed to get list.");
			}        

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return task;
	}
	
	private String insertIntoDatabase(String action, Task task) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			String post = String
					.format("%s?action=%s&summary=%s&description=%s",
							DATABASE_URL, action, "Task", task.getDateAsString());
			
			String save = String.format("%s&id=\"%s\"", task.toJSON(), task.getId());

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
			Log.v(TAG, "ClientProtocolException: " + e.toString());
		} catch (IOException e) {
			Log.v(TAG, "IOException: " + e.toString());
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		return null;
	}

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
			} catch (JSONException e) {
				Log.v(TAG, "Failed to get id.");
			}
			
			task.syncFinished();
		}
	}
	
	public void syncDatabase() {
		// At start get all tasks from database
    	if (!hasSynced) {
    		getAllTasksFromDatabase();
    		hasSynced = true;
		}
    	
    	// Sync all tasks
    	for(Task t : TaskManager.getInstance().getTaskArray()) {
    		Log.v(TAG, "Syncing Task: " + t.getName());
			this.syncTaskToDatabase(t);
		}	
	}
}
