package ca.ualberta.cs.completemytask;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Stores a information about a task.
 * 
 * @author Michael Feist
 *
 */

public class Task extends UserData {
	
	private static final String TAG = "Task";
	
	private boolean shared;
	private boolean complete;
	private boolean sync;
	
	private String name;
	private String description;
	
	private String id;
	
	Task() {
		this("New Task", "No Description");
	}
	
	Task(String name, String description) {
		super();
		this.shared = false;
		this.complete = false;
		this.sync = true;
		
		this.id = null;
		
		this.name = name;
		this.description = description;

	}
	
	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
		this.sync = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.sync = true;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		this.sync = true;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public boolean needsSync() {
		return this.sync && this.shared;
	}

	public String toString() {
		
		String task = String.format("Task: \n\t Name: %s\n\t Description: %s\n\t Id: %s \n", 
				this.name, this.description, this.id);
		
		return task;
	}
	
	public void syncDatabase() {
		
		if (needsSync()) {
			HttpClient httpClient = new DefaultHttpClient();
	
			try {
				String post;
				boolean taskNeedsUpdate = false;
				
				if (this.id == null) {
					post = String
							.format("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T12/?action=post&summary=%s&description=%s",
									"Task", this.getDateAsString());
				} else {
					taskNeedsUpdate = true;
					
					post = String
							.format("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T12/?action=update&summary=%s&description=%s&id=%s",
									"Task", this.getDateAsString(), this.id);
				}

				Log.v(TAG, "Post: " + post);
	
				HttpPost request = new HttpPost(post);
	
				String save = String
						.format("content={\"type\":\"Task\",\"name\":\"%s\",\"description\":\"%s\"} ",
								this.name, this.description);
	
				StringEntity postParams = new StringEntity(save);
	
				request.addHeader("Accept", "text/html");
				request.addHeader("content-type", "application/x-www-form-urlencoded");
	
				request.setEntity(postParams);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				
				String response = httpClient.execute(request,
						responseHandler);
				
				Log.v(TAG, "Response: " + response);
				
				if (!taskNeedsUpdate) {
					JSONObject json;
					try {
						json = new JSONObject( response );
						this.id = json.getString( "id" );
						Log.v(TAG, "ID: " + this.id);
					} catch (JSONException e) {
						Log.v(TAG, "Failed to get id.");
					} 
				}
		        
		        this.sync = false;
				
			} catch (ClientProtocolException e) {
				Log.v(TAG, "ClientProtocolException: " + e.toString());
			} catch (IOException e) {
				Log.v(TAG, "IOException: " + e.toString());
			} finally {
				httpClient.getConnectionManager().shutdown();
			}	
		}
	}
}
