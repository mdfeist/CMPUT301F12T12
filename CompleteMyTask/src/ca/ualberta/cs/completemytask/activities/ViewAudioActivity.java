package ca.ualberta.cs.completemytask.activities;

//import java.io.File;

import ca.ualberta.cs.completemytask.R;
import ca.ualberta.cs.completemytask.background.BackgroundTask;
import ca.ualberta.cs.completemytask.background.HandleInBackground;
import ca.ualberta.cs.completemytask.database.DatabaseManager;
import ca.ualberta.cs.completemytask.saving.LocalSaving;
import ca.ualberta.cs.completemytask.settings.Settings;
import ca.ualberta.cs.completemytask.userdata.AudioAdapter;
import ca.ualberta.cs.completemytask.userdata.MyAudio;
import ca.ualberta.cs.completemytask.userdata.Task;
import ca.ualberta.cs.completemytask.userdata.TaskManager;
import ca.ualberta.cs.completemytask.userdata.User;
import ca.ualberta.cs.completemytask.views.LoadingView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
* Handles the viewing and adding of Audio to each Task.
* Audio files can be added through the "Add Audio" button.
* Audio files can be played back by pressing them (ListView --> 
* listener to open MediaPlayer).
* 
* @author Ian Watts
*/
public class ViewAudioActivity extends Activity {

	private static final int CAPTURE_AUDIO_REQUEST_CODE = 10;
	//private static final String TAG = "ViewAudioActivity";
	AudioAdapter adapter;
	ListAdapter adapterList;
	ListView audioListEntries;
	private Task task;
	
	private LoadingView loadingView;
	public LocalSaving saver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_audio);	
      
		saver = new LocalSaving();
		
		this.loadingView = new LoadingView(this, R.id.ViewAudioMain,
				"Getting Audio ...");
		
		int position = TaskManager.getInstance().getCurrentTaskPosition();
		task = TaskManager.getInstance().getTaskAt(position);
		
		audioListEntries = (ListView) findViewById(R.id.AudioList);
		
		//Pass the text along with the byte Array
		adapter = new AudioAdapter(this, task);

		////
		
		audioListEntries.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		BackgroundTask bg = new BackgroundTask();	
    	bg.runInBackGround(new HandleInBackground() {
    		public void onPreExecute() {
    		}
    		
    		public void onPostExecute(int response) {
    			if (task.getNumberOfAudios() > 0) {
    				//This needs to be changed
    				
    				//Does the adapter populate the listview at once?
    				
    				//(task.getAudioAt(0).getContent());
    				//imagePreview.setImageBitmap(task.getPhotoAt(0).getContent());
    			}
    			
    			adapter.notifyDataSetChanged();
    		}
    		
    		public void onUpdate(int response) {
    		}
    		
    		public boolean handleInBackground(Object o) {	
    			//UNCOMMENT
    			//DatabaseManager.getInstance().getAudio(task);
				return true;
    		}
    	});
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.activity_view_audio, menu);
      return true;
  }
  
	/**
	 * Calls for the AudioCaptureActivity to return an audio file
	 * Extra --> UserID and the Title EditText?
	 * @param view
	 */
  public void takeAudio(View view) {	  
	    //Getting an error in eclipse when trying to add and activity for this...
		Intent intentAudio = new Intent(this, AudioCaptureActivity.class);
		//Intent intentAudio = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		//Do I want to pass something extra to the intent?
		//Some sort of container?
		//intent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, imageFileUri);
		
		startActivityForResult(intentAudio, CAPTURE_AUDIO_REQUEST_CODE);
  }
  
	/**
	 * Gets the byte[] out of an intent. 
	 * @param intent
	 * @return a byte[] that was carried by the intent
	 */
	private byte[] getAudioByte(Intent intent) {		
		Bundle extras = intent.getExtras();
		byte[] newAudio = (byte[]) extras.get("audioData");
		return newAudio;
	}
  
	/**
	 * Adds the Audio 'clip' to both the task and the ListView
	 * Used in OnActivityResult --> use in different spot (user accepts audio)
	 * @param clip
	 */
	private void addAudio(byte[] clip){
		MyAudio audio = new MyAudio();
		audio.setContent(clip);

		User user = null;

		if (Settings.getInstance().hasUser()) {
			user = Settings.getInstance().getUser();
		} else {
			user = new User("Unknown");
		}

		audio.setUser(user);
		audio.setParentId(task.getId());

		sync(audio);
	}
  
	/**
	 * Syncs the image to the task (copied from CommentActivity, 12/11/01)
	 * @param image
	 */
	public void sync(final MyAudio audio) {
		BackgroundTask bg = new BackgroundTask();
		bg.runInBackGround(new HandleInBackground() {
			public void onPreExecute() {
				loadingView.showLoadView(true);
			}
  		
			public void onPostExecute(int response) {
				loadingView.showLoadView(false);
				//Fix above
				adapter.notifyDataSetChanged();
			}
  		
			public void onUpdate(int response) {
			}
  		
			public boolean handleInBackground(Object o) {
				
				task.addAudio(audio);
				
				if (task.isPublic()) {
					//UNCOMMENT
					//DatabaseManager.getInstance().syncAudio(audio);
				}
				task.addAudio(audio);
				
				if (task.isLocal()) {
					saver.open();
					//UNCOMMENT
					//saver.saveAudio(audio);
					saver.close();
				}

				return true;
			}
		});
	}

	/**
	 * Checks the returned activities for a captured image, then adds the image
	 * to the current task
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		if(requestCode == CAPTURE_AUDIO_REQUEST_CODE){
			
			if (resultCode == RESULT_OK) {
				byte[] a = getAudioByte(intent);
				//imagePreview.setImageBitmap(b);
				addAudio(a);
			}
		}
	}
  
	/**
	 * Closes the task
	 * @param view
	 */
	public void close(View view) {
		this.finish();
	}
}