package ca.ualberta.cs.completemytask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
	private static final String URL = "http://10.0.2.2:8888/TaskServer/";
	private static DatabaseManager instance = null;
	
	 private static String login_tag = "login";
	 private static String register_tag = "register";
	
	private JSONParser jsonParser;

	protected DatabaseManager() {
		this.jsonParser = new JSONParser();
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
	
	public JSONObject createUser(String username, String email, String password) {
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", register_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        // return json
        //Log.e("JSON", json.toString());
        return json;
	}
	
	public JSONObject login(String username, String password) {
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("action", login_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        // return json
        //Log.e("JSON", json.toString());
        return json;
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
		
		Log.v(TAG, "Syncing: " + task.getName());
		
		
		
		Log.v(TAG, "Comments: " + task.getNumberOfComments());
		
		// Sync Comments
		for (int i = 0; i < task.getNumberOfComments(); i++) {
			Comment comment = task.getCommentAt(i);
			comment.setParentId(task.getId());
			
		}
		
		Log.v(TAG, "Photos: " + task.getNumberOfPhotos());
		// Sync Photos
		for (int i = 0; i < task.getNumberOfPhotos(); i++) {
			MyPhoto photo = task.getPhotoAt(i);
			photo.setParentId(task.getId());
			
		}
	}
	
	/**
	 * Sync all tasks with the database.
	 */
	public void syncDatabase() {
		testSyncComplete = false;
		
    	
    	testSyncComplete = true;
	}
}
