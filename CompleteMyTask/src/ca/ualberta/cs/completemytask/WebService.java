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

public class WebService {
	
	private static final String TAG = "WebService";
	private String url;
	
	public WebService(String url) {
		this.url = url;
	}
	
	/**
	 * A generic POST to an http server
	 * @param params that POST will send for example action=get&id=1
	 * @return server response
	 */
	public String httpRequest(String params) {
		String response = null;
		
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpPost request = new HttpPost(url);
			
			StringEntity se = new StringEntity(params);
			request.setEntity(se);
			
			request.setHeader("Connection", "keep-alive");
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/x-www-form-urlencoded");
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = httpClient.execute(request, responseHandler);
			
			return response;

		} catch (ClientProtocolException e) {
			Log.w(TAG, "ClientProtocolException: " + e.toString());
		} catch (IOException e) {
			Log.w(TAG, "IOException: " + e.toString());
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		return response;
	}
	
	/**
	 * Returns a list of all the objects
	 * currently in the database as a
	 * JSON array.
	 * @return A string in the format of a JSON array
	 */
	public String getJSONList() {
		
		String params = String.format("action=list");
		String json = httpRequest(params);
		
		return json;
	}
	
	/**
	 * Gets the content of the object with the given id.
	 * @param id of the object in the database
	 * @return returns a JSONObject of the content
	 */
	public JSONObject getJSONObjectWithID(String id) {
		
		JSONObject data = null;
		
		String params = String.format("action=get&id=%s", id);
		String response = httpRequest(params);
		
		if (response == null) {
			Log.w(TAG, "Failed to get object with id: " + id);
			return null;
		}
		
		try {
			JSONObject json = new JSONObject( response );
			
			try {
				data = json.getJSONObject("content");
			} catch (JSONException e) {
				Log.w(TAG, "Failed to get content");
			} 
			
		} catch (JSONException e) {
			Log.w(TAG, "Failed to convert the http response in JSONObject");
		} 
		
		return data;
		
	}
	
	/**
	 * Adds a new entry to the database.
	 * @param summary of entry
	 * @param description of entry
	 * @param content that you want submitted
	 * @return {"summary":"example summary","content":{},"id":"9b42669cb3ba373ff7877ab826984bde4859ccf0","description":"example description"}
	 */
	public String insertEntry(String summary, String description, String content) {
		String params = String.format("action=post&summary=%s&description=%s&content=%s",
				summary, description, content);
		return httpRequest(params);
	}
	
	/**
	 * Replaces the entry with the given id with the given entry.
	 * @param id of the entry you want replaced
	 * @param summary of new entry
	 * @param description of new entry
	 * @param content that you want submitted
	 * @return {"summary":"updated summary","content":{},"id":"faccfa7b932f56fc8bcda5bb279cdeee49ef35a7","description":"updated description"}
	 */
	public String replaceEntry(String id, String summary, String description, String content) {
		String params = String.format("action=update&id=%s&summary=%s&description=%s&content=%s",
				id, summary, description, content);
		return httpRequest(params);
	}
	
	/**
	 * Removes an entry from the database with the given id.
	 * @param id of entry
	 * @return {"id":"faccfa7b932f56fc8bcda5bb279cdeee49ef35a7","message":"removed"}
	 */
	public String removeEntry(String id) {
		String params = String.format("action=remove&id=%s", id);
		return httpRequest(params);
	}
	
	/**
	 * Removes everything from the database.
	 */
	public void nukeDatabase() {
		String params = String.format("action=nuke&key=judgedredd");
		httpRequest(params);
	}
	
}
