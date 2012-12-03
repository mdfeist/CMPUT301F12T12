package ca.ualberta.cs.completemytask.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
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
	MediaPlayer Player = null;
	//private boolean isRecording = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_capture);
        
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
     * Closes the mediaPlayer currently in use and sets it back to null
     */
    public void closePlayer(){
        if (Player != null){
        	Player.release();
        	Player = null;
        }
    }

    /**
     * Starts the recording of audio on button press.
     * Audio is captured until user hits stop
     */
    public void recordStart(View view) throws IOException {
    	try {
    		
    		closePlayer();
    		
    		
    		
    		//TESTING THIS LINE
        	recorder.reset();
    		
        	//TESTING THIS LINE
        	recordInitialize();
        	
    		//if (isRecording == false) {
				//Need some sort of check/Disable to stop this
				recorder.prepare();
				System.out.println("Recorder prepared");
				recorder.start();
				System.out.println("Recorder started");
			//}

    	} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to start properly");
			e.printStackTrace();
		}
    	
    	//isRecording = true;
    }

    /**
     * Resets the recording of audio on button press.
     * New audio can be captured by pressing start again.
     * Sets the temp file back to null to prevent Start>Stop>Start>Submit from working
     */
    public void recordReset(View view){
    	//reset the recorder and mediaPlayer to record new audio
    	
    	//TESTING STOP
    	//recorder.stop();
    	recorder.reset();
    	//closePlayer();
    	
    	tempAudioFile = null;
    	
    	//MIGHT BE THE PROBLEM!!!!!!!
    	//delete the old audio file PROBABLY REDUNDANT HERE --> is deleted on exit
    	//Tempfile or the SD file?
    	deleteTempFile();
    	
    	recordInitialize();
    }
    
    /**
     * Stops the recording of audio and preps it for submission
     * @param view
     */
    public void recordStop(View view) {
    	try {
    		closePlayer();

			//recorder is released on one of the two exit
			//recorder.release();
			//System.out.println("Recorder released");
			
			//if (isRecording) {
				System.out.println("About to stop...");
				//recorder.stop();
				System.out.println("It worked After stop");
				recorder.reset();
				//Set our audio file
				tempAudioFile = new File(
						android.os.Environment.getExternalStorageDirectory()
								+ "/Record/TaskAudio.3gp");
				//Re-initialize the recorder
				
				//recorder = null;
				
				recordInitialize();
				
				//isRecording = false;
			//}
			
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
        //Reset the media player in case they were just playing a clip
    	closePlayer();
    	
    	//TESTING the stop before reset
    	//recorder.stop();
    	recorder.reset();
    	
    	
    	//If no tempAudioFile
    	//Pop up --> "No Audio to Preview!"
    		
        if (tempAudioFile != null) {
			//Open a player for the audio
			Player = new MediaPlayer();
			try {

				//Must convert the Audio to File (make a tempFile for this)
				Player.setDataSource(tempAudioFile.getAbsolutePath());
				Player.prepare();
				Player.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
        
        
        //TESTING 
        recordInitialize();
    }
    
    /**
     * Button response that will stop the mediaPlayer currently playing
     * @param view
     */
    public void stopAudio(View view){
    	//The button simply closes the player
    	closePlayer();
    }
    
    /**
     * Deletes the audio file on the SD card to cleanup
     * @param view
     */
    public void deleteTempFile(){
    	boolean exists = (new File(android.os.Environment.getExternalStorageDirectory() + "/Record/TaskAudio.3gp")).exists();
    	if (exists) {
    		//delete the outstanding file?
    		File file = new File(android.os.Environment.getExternalStorageDirectory() + "/Record/TaskAudio.3gp");
    		//boolean deleted = file.delete();
    		file.delete();
    	}
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
    	
    	//isRecording = false;
    	
    	//release the recorder and close mediaPlayer before submitting
    	recorder.release();
    	//closePlayer();
    	
    	
    	if (tempAudioFile != null){
    		//Converts File into byte[] form
        	returnAudio = getByteFromFile(tempAudioFile);
        	captureAudioName = audioNameTextView.getText().toString();
    	}
    	
    	//Done with the File on SD card, delete it
    	deleteTempFile();
    	
    	//Return info for the Task
		Intent resultData = new Intent();
		resultData.putExtra("title", captureAudioName);
		resultData.putExtra("audioData", returnAudio);
		
		
		//Catch null Audio Data
		//Check return audio instead
		if (tempAudioFile == null) {
			System.out.println("tempAudioFile is null");
			setResult(Activity.RESULT_CANCELED, resultData);
		} else{
			System.out.println("tempAudioFile is not null");
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
		//release the recorder and close mediaPlayer before exiting
		//isRecording = false;
		
		recorder.release();
		//closePlayer();
		
		//Get rid of the temp file before exiting
		deleteTempFile();
		
    	//Result is nothing!
		Intent resultData = new Intent();
		setResult(Activity.RESULT_CANCELED, resultData);
		finish();
	}
    
}

