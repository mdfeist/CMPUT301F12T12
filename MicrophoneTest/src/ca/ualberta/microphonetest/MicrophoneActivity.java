package ca.ualberta.microphonetest;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Bundle;
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

    MediaRecorder recorder = new MediaRecorder();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microphone);
    
    }
    
    public void onStart(){
    	super.onStart();
    	
    	boolean exists = (new File(android.os.Environment.getExternalStorageDirectory() + "/Record/")).exists();
    	if (!exists) {
    	    new File(android.os.Environment.getExternalStorageDirectory() + "/Record/").mkdirs();
    	}
    	
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(android.os.Environment.getExternalStorageDirectory()+"/Record/test.3gp");
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        
//        try {
//            recorder.prepare();
//            System.out.println("Recorder prepared");
//        } catch (Exception e){
//        	System.out.println("Prepare did not work");
//              e.printStackTrace();
//        }

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_microphone, menu);
        return true;
    }

    public void RecordStart(View view) throws IOException {
    	try {
    		recorder.prepare();
    		System.out.println("Recorder prepared");
    		recorder.start(); 
			System.out.println("Recorder started");

    	} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to start properly");
			e.printStackTrace();
		}
    	
    	
    	
    	
    }
    
    public void RecordStop(View view) {
    	try {
			System.out.println("About to stop...");
			recorder.stop();
			System.out.println("It worked After stop");
			recorder.release();
			System.out.println("Recorder released");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			System.out.println("Recorder failed to stop");
			e.printStackTrace();
		}
    }
}