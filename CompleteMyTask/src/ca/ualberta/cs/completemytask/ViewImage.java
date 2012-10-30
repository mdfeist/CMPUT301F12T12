package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

/**
 * Shows a close up of an image
 * 
 * @author Devon Waldon
 *
 */
public class ViewImage extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_image, menu);
        return true;
    }
    
    /**
     * Take a new photo for the task
     */
    public void addPhoto(){
    	
    }
    
    /**
     * Closes the task
     * 
     * @param view
     */
    public void close(View view) {
    	this.finish();
    }
}
