package ca.ualberta.cs.completemytask;

import java.io.IOException;
import java.util.*;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
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
	
	private boolean hasSynced;

	protected TaskManager() {
		this.tasks = new ArrayList<Task>();
		this.hasSynced = false;
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
	
	private void getAllTasksFromDataBase() {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			String get = String
					.format("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T12/?action=list");

			HttpGet request = new HttpGet(get);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String response = httpClient.execute(request, responseHandler);

			Log.v(TAG, "Response: " + response);

			JSONArray jsonlist;
			String id = null;

			try {
				jsonlist = new JSONArray(response);

				for (int i = 0; i < jsonlist.length(); ++i) {
					JSONObject idObject = jsonlist.getJSONObject(i);
					id = idObject.getString("id");

					Log.v(TAG, "ID: " + id);

					Task task = getTaskFromDatabase(id);

					if (task != null) {
						tasks.add(task);
					}
				}

			} catch (JSONException e) {
				Log.v(TAG, "Failed to get list.");
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private Task getTaskFromDatabase(String id) {
		
		Task task = null;
		
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			String get = String
					.format("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T12/?action=get&id=%s", id);
    		
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
					task.setShared(true);
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
	
	public void syncDatabase() {
		
		class SendPostReqAsyncTask extends AsyncTask<String, Void, String>{

	        @Override
	        protected String doInBackground(String... params) {
	        	// At start get all tasks from database
	        	if (!hasSynced) {
	        		getAllTasksFromDataBase();
	        		hasSynced = true;
	    		}
	        	
	        	// Sync all tasks
	        	for(Task t : tasks) {
	        		Log.v(TAG, "Syncing Task: " + t.getName());
	    			t.syncDatabase();
	    		}
				
				return "finished";
			}
	        
	        @Override
	        protected void onPostExecute(String result) {
	            super.onPostExecute(result);

	            if(result.equals("finished")){
	            	Log.v(TAG, "HTTP post done");
	            }else{
	            	Log.v(TAG, "Invalid HTTP post");
	            }
	        }        
		}
		
		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
	    sendPostReqAsyncTask.execute();  
		
	}
}
