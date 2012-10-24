package ca.ualberta.cs.completemytask;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * Stores a information about a task.
 * 
 * @author Michael Feist
 *
 */

public class Task extends UserData {
	
	private static final String TAG = "Task";
	
	boolean shared;
	boolean complete;
	
	String name;
	String description;
	
	Task() {
		this("New Task", "No Description");
	}
	
	Task(String name, String description) {
		super();
		this.shared = false;
		this.complete = false;
		
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
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void syncDatabase() {
		
		HttpClient httpClient = new DefaultHttpClient();

		try {
			String post = String
					.format("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T12/?action=post&summary=%s&description=%s",
							"Task", name);

			Log.v(TAG, "Post: " + post);

			HttpPost request = new HttpPost(post);

			String save = String
					.format("content={\"name\":\"%s\",\"description\":\"%s\"} ",
							name, description);

			StringEntity postParams = new StringEntity(save);

			request.addHeader("Accept", "text/html");
			request.addHeader("content-type", "application/x-www-form-urlencoded");

			request.setEntity(postParams);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			
			String response = httpClient.execute(request,
					responseHandler);
			
			Log.v(TAG, "Response: " + response);
			
		} catch (ClientProtocolException e) {
			Log.v(TAG, "ClientProtocolException: " + e.toString());
		} catch (IOException e) {
			Log.v(TAG, "IOException: " + e.toString());
		} finally {
			httpClient.getConnectionManager().shutdown();
		}	
	}
}
