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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_capture);
    	
        //Edit Text for titling the Audio Recording 
    	audioNameTextView = (EditText) findViewById(R.id.audioNameView);
    	
    	//Prepare the MediaRecorder for Audio Capture
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
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(android.os.Environment.getExternalStorageDirectory()+"/TaskAudio.3gp");
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }
    
    /**
     * Closes the mediaPlayer currently in use and sets it back to null.
     * Used to make sure the Player is never in the incorrect state
     * when certain buttons are pressed.
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
    		//Make sure Recorder and Player are in correct State
    		closePlayer();
        	recorder.reset();
        	recordInitialize();
        	
        	//Start Recording!
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
    	//Make sure Recorder is in correct State(recorder.stop caused errors when not recording)
    	recorder.reset();
    	
    	//Delete our temp files and reset Recorder
    	tempAudioFile = null;
    	deleteTempFile();
    	
    	recordInitialize();
    }
    
    /**
     * Stops the recording of audio and prepares it for submission
     * @param view
     */
    public void recordStop(View view) {
    	try {
    		closePlayer();

    		//Using stop causes errors, reset has same functionality
			//recorder.stop();
			recorder.reset();
			
			//Set our audio file
			tempAudioFile = new File(
						android.os.Environment.getExternalStorageDirectory()
								+ "/TaskAudio.3gp");
			//Reset recorder
			recordInitialize();
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			System.out.println("Recorder failed to stop");
			e.printStackTrace();
		}
    }
    
    /**
     * Opens a MediaPlayer and plays the current audio clip.
     * Lets the user sample the audio they are going to submit.
     * @param view
     */
    public void playAudio(View view){
        //Reset the media player in case they were just playing a clip
    	closePlayer();
    	recorder.reset();
    	
    	//Make sure our source has data
    	boolean sdExisits = (new File(android.os.Environment.getExternalStorageDirectory() + "/TaskAudio.3gp")).exists();
        
    	if (tempAudioFile != null & sdExisits) {
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
        
        //Get recorder ready again
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
     * Deletes the leftover audio file on the SD card if it exists
     * @param view
     */
    public void deleteTempFile(){
    	boolean exists = (new File(android.os.Environment.getExternalStorageDirectory() + "/TaskAudio.3gp")).exists();
    	if (exists) {
    		File file = new File(android.os.Environment.getExternalStorageDirectory() + "/TaskAudio.3gp");
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
    	//release the recorder and close mediaPlayer before submitting
    	recorder.release();
    	closePlayer();
    	
    	//Check if the source exists
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
		recorder.release();
		closePlayer();
		
		//Get rid of the temp file before exiting
		deleteTempFile();
		
    	//Result is nothing!
		Intent resultData = new Intent();
		setResult(Activity.RESULT_CANCELED, resultData);
		finish();
	}
    
}

