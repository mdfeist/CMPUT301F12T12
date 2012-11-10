package ca.ualberta.cs.completemytask;

//import java.io.File;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

/**
 * Handles the viewing and adding of Audio to each Task.
 * Audio files can be added through the "Add Audio" button.
 * Audio files can be played back by pressing them (ListView --> 
 * listener to open MediaPlayer).
 * 
 * @author Ian Watts
 */
public class ViewAudioActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_audio);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_audio, menu);
        return true;
    }
    
    public void takeAudio(){
    	
    }
    
    //More stuff here soon
    
	/**
	 * Closes the task
	 * @param view
	 */
	public void close(View view) {
		this.finish();
	}
}
