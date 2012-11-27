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
import ca.ualberta.cs.completemytask.R;
import ca.ualberta.cs.completemytask.activities.MainMenuActivity;
import ca.ualberta.cs.completemytask.database.DatabaseManager;
import ca.ualberta.cs.completemytask.userdata.Comment;
import ca.ualberta.cs.completemytask.userdata.MyAudio;
import ca.ualberta.cs.completemytask.userdata.MyPhoto;
import ca.ualberta.cs.completemytask.userdata.Task;
import ca.ualberta.cs.completemytask.userdata.TaskManager;
import ca.ualberta.cs.completemytask.userdata.User;


/**
 * Test's database synchronization at the main menu.
 * 
 * @author Michael Feist
 *
 */
public class MainMenuTest extends ActivityInstrumentationTestCase2<MainMenuActivity> {
	
	private MainMenuActivity main;
	private Button syncButton;
	
	// Task Info
	private String taskName = "Test Task";
	private String taskDescription = "Test Description";
	
	private boolean testIsLocal = false;
	private boolean testIsComplete = false;
	
	private boolean testNeedsComment = true;
	private boolean testNeedsPhoto = true;
	private boolean testNeedsAudio = false;
	
	// Comments
	private int numberOfComments = 1;
	private String commentText = "A comment";
	
	// Photos
	private int numberOfPhotos = 1;
	//private Photo image;
	
	// Audio
	private int numberOfAudio = 1;
	//private Audio sound;
	
	// User Name
	private String userName = "Test User";
	
	public MainMenuTest() {
		super (MainMenuActivity.class);  
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		main = getActivity();
		syncButton = (Button) main.findViewById(R.id.SyncTaskButton); 
		 
	}
	
	/**
	 * Uplaods fake data to the database
	 */
	private void insertFakeTaskIntoDatabase() {
		
	}
	
	/**
	 * Removes the fake data
	 */
	private void removeFakeTaskFromDatabase(Task task) {
		
	}
	
	/**
	 * Wait until syncronization with the database is done
	 */
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
				
				assertTrue("Failed isComplete", task.isComplete() == testIsComplete);
				
				assertTrue("Failed needs Comment", task.needsComment() == testNeedsComment);
				assertTrue("Failed needs Photo", task.needsPhoto() == testNeedsPhoto);
				assertTrue("Failed needs Comment", task.needsAudio() == testNeedsAudio);
				
				assertTrue("User Missing", task.hasUser());
				assertTrue("Wrong Username", task.getUser().getUserName().equals(userName));
				
				assertTrue("Wrong number of comments", task.getNumberOfComments() == numberOfComments);
				
				for (int i = 0; i < task.getNumberOfComments(); i++) {
					String commentContentText = task.getCommentAt(i).getContent();
					
					assertTrue("Comment has wrong name", commentContentText.equals(commentText + (i+1)));
				}
				
				assertTrue("Wrong number of photos", task.getNumberOfPhotos() == numberOfPhotos);
				
				for (int i = 0; i < task.getNumberOfPhotos(); i++) {
					Object image = task.getPhotoAt(i).getContent();
					
				}
				
				assertTrue("Wrong number of audio files", task.getNumberOfAudios() == numberOfAudio);
				
				for (int i = 0; i < task.getNumberOfAudios(); i++) {
					Object audio = task.getAudioAt(i).getContent();
					
				}
				
				foundTask = true;
			}
		}
		
		assertTrue("Task Not Found", foundTask);
	}
	
	/**
	 * Tests the database syncronization.
	 */
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
