package ca.ualberta.cs.completemytask;

import java.util.*;

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

	protected TaskManager() {
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
	
	public void syncDatabase() {
		
		class SendPostReqAsyncTask extends AsyncTask<String, Void, String>{

	        @Override
	        protected String doInBackground(String... params) {
	        	
	        	for(Task t : tasks) {
	        		Log.v(TAG, "Saving: " + t.getName());
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
