package ca.ualberta.cs.completemytask.activities;

//import java.io.File;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
//import android.widget.ListAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import ca.ualberta.cs.completemytask.R;
import ca.ualberta.cs.completemytask.adapters.AudioAdapter;
import ca.ualberta.cs.completemytask.background.BackgroundTask;
import ca.ualberta.cs.completemytask.background.HandleInBackground;
import ca.ualberta.cs.completemytask.saving.LocalSaving;
import ca.ualberta.cs.completemytask.settings.Settings;
import ca.ualberta.cs.completemytask.userdata.MyAudio;
import ca.ualberta.cs.completemytask.userdata.Task;
import ca.ualberta.cs.completemytask.userdata.TaskManager;
import ca.ualberta.cs.completemytask.userdata.User;
import ca.ualberta.cs.completemytask.views.LoadingView;

/**
* Handles the viewing and adding of Audio to each Task.
* Audio files can be added through the "Add Audio" button.
* Audio files can be played back by pressing them (ListView --> 
* listener to open MediaPlayer).
* 
* @author Ian Watts
*/
public class ViewAudioActivity extends CustomActivity {

	private static final int CAPTURE_AUDIO_REQUEST_CODE = 10;
	//private static final String TAG = "ViewAudioActivity";
	AudioAdapter adapter;
	//ListAdapter adapterList;
	ListView audioListEntries;
	private Task task;
	
	private LoadingView loadingView;
	public LocalSaving saver;
	
	private MediaPlayer   mPlayer = null;
	
	
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
		 
		adapter = new AudioAdapter(this, task);
		
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
    	
		//Following code block sets up a listener to select and delete log entries
	    audioListEntries.setTextFilterEnabled(true);
	    audioListEntries.setOnItemClickListener(new OnItemClickListener() {
	    
	    	public void onItemClick(AdapterView<?> arg0, View v, final int position, long id) {	    		
	    		System.out.println("Item pressed!");
	    		
	    		System.out.println("At Position"+position);
	    		
	    		//private void startPlaying() {

	    	        
	    	    AlertDialog.Builder adb = new AlertDialog.Builder(ViewAudioActivity.this);
	    	    adb.setTitle("Audio Playback");
		    	adb.setMessage("Do you want play "+ task.getAudioAt(position).getAudioName());
		    		
		    	//Close/Stop button Might switch so it's on the right
		    	adb.setNegativeButton("Close", new AlertDialog.OnClickListener(){
	                public void onClick(DialogInterface dialog, int which) {
	                	System.out.println("Future Stop button pressed!");
	                	
	                	if (mPlayer != null){
	                		mPlayer.release();
	                		mPlayer = null;
	                	}
	                }
		    	});
		    		
		    	//Play button for audio
		    	adb.setPositiveButton("Play", new AlertDialog.OnClickListener() {
		    		public void onClick(DialogInterface dialog, int which) {
		               System.out.println("STARTED THE AUDIO");
		                
		               //Reset the media player incase they were just playing a clip
		               if (mPlayer != null){
		            	   mPlayer.release();
		            	   mPlayer = null;
		               }
		                	
		               //Open a player for the audio
			    	   mPlayer = new MediaPlayer();
			    	   try {
			    	        	
			    		   //Make a file from the byte[]
			    	       File tempFile = getFileFromByte(task.getAudioAt(position).getContent());
			    	        	
			    	       //Must convert the Audio to File (make a tempFile for this)
			    	       mPlayer.setDataSource(tempFile.getAbsolutePath());
			    	       mPlayer.prepare();
			    	       mPlayer.start();
			    	   } catch (IOException e) {
			    	       e.printStackTrace();
			    	   }
		                	
		                	
		            }
		                	
		       });  		
		       adb.show();                     
	    		
	    	}
	    });
    	
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.activity_view_audio, menu);
      return true;
  }
  
	/**
	 * Takes a string, decodes it and then writes it as
	 * a file to the SD card
	 * 
	 * @param audioString
	 * @return Audio File
	 */
	public File getFileFromByte(byte[] decodedByteArray) {
		
		//This will be split into a new part
		File decodedAudio = new File(android.os.Environment.getExternalStorageDirectory()+"/Record/test.3gp");
		try {
			FileOutputStream fos = new FileOutputStream(decodedAudio);
			fos.write(decodedByteArray);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return decodedAudio;
	}
  
  
  
	/**
	 * Calls for the AudioCaptureActivity to return an audio file
	 * Extra --> UserID and the Title EditText?
	 * @param view
	 */
	public void takeAudio(View view) {	
	  	
	  if (mPlayer != null){
		mPlayer.release();
     	mPlayer = null;
	  }
	  
	  Intent intentAudio = new Intent(this, AudioCaptureActivity.class);
		
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
	 * Gets the byte[] out of an intent. 
	 * @param intent
	 * @return a byte[] that was carried by the intent
	 */
	private String getAudioName(Intent intent) {		
		Bundle extras = intent.getExtras();
		String newAudioName = (String) extras.get("title");
		return newAudioName;
	}
  
	/**
	 * Adds the Audio 'clip' to both the task and the ListView
	 * Used in OnActivityResult --> use in different spot (user accepts audio)
	 * @param clip
	 */
	private void addAudio(byte[] clip, String name){
		MyAudio audio = new MyAudio();
		audio.setContent(clip);
		audio.setAudioName(name);
		
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
				
				if (task.isPublic()) {
					//UNCOMMENT
					//DatabaseManager.getInstance().syncAudio(audio);
				}
				
				if (task.isLocal()) {
					saver.open();
					saver.saveAudio(audio);
					saver.close();
				}
				
				task.addAudio(audio);

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
				String n = getAudioName(intent);

				addAudio(a, n);
	
			}
		}
	}
  
	/**
	 * Closes the task
	 * @param view
	 */
	public void close(View view) {
		
		if (mPlayer != null){
			mPlayer.release();
		    mPlayer = null;
		}
		
		this.finish();
	}
}