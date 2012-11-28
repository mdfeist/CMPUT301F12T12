package ca.ualberta.cs.completemytask.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import ca.ualberta.cs.completemytask.R;

/**
 * Handles the adding of audio files. The user is
 * able to record an audio clip, sample it and then
 * cancel, reset or submit it. The audio is then added
 * to the Task or discarded.
 * 
 * @author Ian Watts
 *
 */
public class AudioCaptureActivity extends CustomActivity {
	protected final String TAG = "AudioCaptureActivity";
	
	MediaRecorder recorder = new MediaRecorder();
	byte[] returnAudio;
	String captureUserName;
	String captureAudioName;
	File tempAudioFile = null;
	private EditText audioNameTextView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_capture);
        
    }

    public void onStart(){
    	super.onStart();
    	
    	audioNameTextView = (EditText) findViewById(R.id.audioNameView);
    	
    	//I think this is taking a long time if the directory does not exist
    	boolean exists = (new File(android.os.Environment.getExternalStorageDirectory() + "/Record/")).exists();
    	if (!exists) {
    	    new File(android.os.Environment.getExternalStorageDirectory() + "/Record/").mkdirs();
    	}
    	
    	recordInitialize();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_audio_capture, menu);
        return true;
    }
    
    /**
     * Handles the setup of the MediaRecorder recorder that
     * occurs on startup and after stopping and resetting.
     */
    public void recordInitialize(){
    	//Initialize the recorder
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(android.os.Environment.getExternalStorageDirectory()+"/Record/TaskAudio.3gp");
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    /**
     * Starts the recording of audio on button press.
     * Audio is captured until user hits stop
     */
    public void recordStart(View view) throws IOException {
    	try {
    		//Need some sort of check/Disable to stop this
    		recorder.prepare();
    		//System.out.println("Recorder prepared");
    		recorder.start(); 
			//System.out.println("Recorder started");
    	} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to start properly");
			e.printStackTrace();
		}
    }

    /**
     * Resets the recording of audio on button press.
     * New audio can be captured by pressing start again.
     * Sets the temp file back to null to prevent Start>Stop>Start>Submit from working
     */
    public void recordReset(View view){
    	//reset the recorder to record new audio
    	recorder.reset();
    	
    	tempAudioFile = null;
    	
    	//delete the old audio file PROBABLY REDUNDANT HERE --> is deleted on exit
    	boolean exists = (new File(android.os.Environment.getExternalStorageDirectory() + "/Record/TaskAudio.3gp")).exists();
    	if (exists) {
    		//delete the outstanding file?
    		File file = new File(android.os.Environment.getExternalStorageDirectory() + "/Record/TaskAudio.3gp");
    		//boolean deleted = file.delete();
    		file.delete();
    	}
    	
    	recordInitialize();
    }
    
    /**
     * Stops the recording of audio and preps it for submission
     * @param view
     */
    public void recordStop(View view) {
    	try {
			//System.out.println("About to stop...");
			recorder.stop();
			//System.out.println("It worked After stop");
			recorder.reset(); 
			
			//recorder is released on one of the two exit
			//recorder.release();
			//System.out.println("Recorder released");
			
			//Set our audio file
			//Its already in a Try/Catch!
			tempAudioFile = new File(android.os.Environment.getExternalStorageDirectory()+"/Record/TaskAudio.3gp");
			
			recordInitialize();
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			System.out.println("Recorder failed to stop");
			e.printStackTrace();
		}
    }
    
    //Let the user sample the audio they are going to submit
    /**
     * Opens a MediaPlayer and plays the current audio clip.
     * @param view
     */
    public void playAudio(View view){
    	//If the file exists
    	//	Play audio file with media player
    	//else
    	//	"Record an audio clip first"
    }
    
    //A delete function to clean up code
    //Save the file outside a directory so it is not left to be cleaned up
    /**
     * Deletes the audio file on the SD card to cleanup
     * @param view
     */
    public void deleteFile(){
//    	boolean exists = (new File(android.os.Environment.getExternalStorageDirectory() + "/Record/TaskAudio.3gp")).exists();
//    	if (exists) {
//    		//delete the outstanding file?
//    		File file = new File(android.os.Environment.getExternalStorageDirectory() + "/Record/TaskAudio.3gp");
//    		//boolean deleted = file.delete();
//    		file.delete();
//    	}
    }

	/**
	 * This functions converts audio file to a byte array which can be
	 * passed back in the intent
	 */
	public byte[] getByteFromFile(File audioFile) {

		byte []buffer = new byte[(int) audioFile.length()];
		InputStream ios = null;

		try {
			ios = new FileInputStream(audioFile);
			if (ios.read(buffer) == -1) {
				throw new IOException("EOF reached while trying to read the whole file");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.w(TAG, e.getStackTrace().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.w(TAG, e.getStackTrace().toString());
		} finally {
			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
				Log.w(TAG, e.getStackTrace().toString());
			}
		}

		return buffer;
	}
    
	/**
	 * Returns the audio and the audioName back in the intent.
	 * If the tempAudioFile is empty --> RESULT_CANCELED since
	 * there is nothing to submit
	 * @param view
	 */
    public void submitAudio(View view){
    	
    	recorder.release();
    	
    	//Should fix crash but a popup saying NO AUDIO TO SUBMIT would be better
    	//boolean fileExisits = tempAudioFile.exists();
    	//if (fileExisits){
    	if (tempAudioFile != null){
    		
    		//System.out.println("tempAudioFile is not null");
    		
    		//Converts File into byte[]
        	returnAudio = getByteFromFile(tempAudioFile);
        	captureAudioName = audioNameTextView.getText().toString();
    	}
    	
    	//System.out.println("Outside the if statement");
    	
    	boolean exists = (new File(android.os.Environment.getExternalStorageDirectory() + "/Record/TaskAudio.3gp")).exists();
    	if (exists) {
    		//delete the outstanding file?
    		File file = new File(android.os.Environment.getExternalStorageDirectory() + "/Record/TaskAudio.3gp");
    		//boolean deleted = file.delete();
    		file.delete();
    	}
    	
    	//Return info for the Task
		Intent resultData = new Intent();
		resultData.putExtra("title", captureAudioName);
		resultData.putExtra("audioData", returnAudio);
		
		if (tempAudioFile == null) {
			setResult(Activity.RESULT_CANCELED, resultData);
		} else{
			setResult(Activity.RESULT_OK, resultData);
		}
		finish();
    }
    
	/**
	 * Closes the task.
	 * RESULT_CANCELED since the user does not want to submit
	 * @param view
	 */
	public void close(View view) {
		//release the recorder before exiting
		recorder.release();
		
    	boolean exists = (new File(android.os.Environment.getExternalStorageDirectory() + "/Record/TaskAudio.3gp")).exists();
    	if (exists) {
    		//delete the outstanding file?
    		File file = new File(android.os.Environment.getExternalStorageDirectory() + "/Record/TaskAudio.3gp");
    		//boolean deleted = file.delete();
    		file.delete();
    	}
		
    	//Result is nothing!
		Intent resultData = new Intent();
		setResult(Activity.RESULT_CANCELED, resultData);
		finish();
	}
    
}

