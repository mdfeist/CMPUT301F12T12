package ca.ualberta.microphonetest;

import android.R.string;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;



/**
 * Simple test for capturing audio with the 
 * microphone in Android.
 * 
 * Unable to test with AVD.
 * 
 * Things to check/change:  What audio encoding?
 * What filetype?  What filepath (need existing folder)?
 * How to play the audio back (use media player)?
 * 
 * @author Ian Watts
 *
 */

public class MicrophoneActivity extends Activity {

	private static final String file = "audio";	
    MediaRecorder recorder = new MediaRecorder();

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microphone);
    
    }
    
    public void onStart(){
    	super.onStart();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + file);
        
        try {
            recorder.prepare();
        } catch (Exception e){
              e.printStackTrace();
        }
 
        /**
         * On button press start recording
         */
        //recorder.start(); 
        
        /**
         * On button press stop recording
         */
        //recorder.stop();
        //recorder.release();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_microphone, menu);
        return true;
    }

    public void RecordStart(View view) {
    	recorder.start(); 
    }
    
    public void RecordStop(View view) {
        // Kabloey
        recorder.stop();
        recorder.release();
    }
    
    
    public void RecordAudio(string file){
/*        final Button button = (Button) findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            }
        });*/
    }
}

