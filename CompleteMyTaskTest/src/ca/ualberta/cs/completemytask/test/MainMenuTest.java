package ca.ualberta.cs.completemytask.test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import ca.ualberta.cs.completemytask.DatabaseManager;
import ca.ualberta.cs.completemytask.MainMenuActivity;
import ca.ualberta.cs.completemytask.R;
import ca.ualberta.cs.completemytask.Task;
import ca.ualberta.cs.completemytask.TaskManager;
import ca.ualberta.cs.completemytask.User;

public class MainMenuTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {
	
	private MainMenuActivity main;
	private Button syncButton;
	public MainMenuTest() {
		super (MainMenuActivity.class);  
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		main = getActivity();
		syncButton = (Button) main.findViewById(R.id.SyncTaskButton); 
		 
	}
	
	private void insertFakeTaskIntoDatabase() {
		
		HttpClient httpClient = new DefaultHttpClient();
		
		Task task = new Task("Test Task", "Test Description");
		task.setRequirements(true, true, false);
		task.setPublic(true);
		task.setUser(new User("Test User"));
		
		try {
			String post = String
					.format("%s?action=post&summary=%s&description=%s",
							DatabaseManager.getInstance().getDatabaseURL(),
							"Task", task.getDateAsString());
			
			String save = String.format("content=%s&id=%s", task.toJSON(), task.getId());
			

			HttpPost request = new HttpPost(post);

			StringEntity se = new StringEntity(save);	
			
			request.setEntity(se);
			
			request.setHeader("Connection", "keep-alive");
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/x-www-form-urlencoded");
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			httpClient.execute(request, responseHandler);


		} catch (ClientProtocolException e) {
			assertTrue("ClientProtocolException: " + e.toString(), true);
		} catch (IOException e) {
			assertTrue("IOException: " + e.toString(), true);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	private void removeFakeTaskFromDatabase(Task task) {
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			String post = String
					.format("%s?action=remove",
							DatabaseManager.getInstance().getDatabaseURL());
			
			String save = String.format("id=%s", task.getId());
			

			HttpPost request = new HttpPost(post);

			StringEntity se = new StringEntity(save);	
			
			request.setEntity(se);
			
			request.setHeader("Connection", "keep-alive");
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/x-www-form-urlencoded");
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			httpClient.execute(request, responseHandler);


		} catch (ClientProtocolException e) {
			assertTrue("ClientProtocolException: " + e.toString(), true);
		} catch (IOException e) {
			assertTrue("IOException: " + e.toString(), true);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	private void waitForTaskSync(int atMost) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		int count = 0;
		
		// Wait for sync
		while ((count < atMost || atMost == 0) && !DatabaseManager.testSyncComplete) {
			
			latch.await(1, TimeUnit.SECONDS);
			count++;
		}
		
		// If sync took to long
		if (count > atMost && atMost != 0) {
			assertTrue("Sync Timed Out", false);
		}
		
		// Check if task was synced
		boolean foundTask = false;
		for(Task task : TaskManager.getInstance().getTaskArray()) {
			if(task.getName().equals("Test Task")) {
				removeFakeTaskFromDatabase(task);
				
				assertTrue("Wrong Description", task.getDescription().endsWith("Test Description"));
				assertNotNull("No ID", task.getId());
				
				foundTask = true;
			}
		}
		
		assertTrue("Task Not Found", foundTask);
	}
	
	public void testSync() {
		insertFakeTaskIntoDatabase();
		
		main.runOnUiThread(new Runnable() {
	          public void run() {
	        	  syncButton.performClick();
	          }
	      });
		
		try {
			waitForTaskSync(20);
		} catch (InterruptedException e) {
			assertTrue("CountDownLatch Error", true);
		}
	}

}
