package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

/**
 * Shows a close up of an image
 * 
 * @author Devon Waldon
 *
 */
public class ViewImage extends Activity {
	private ImageView myImage = (ImageView) findViewById(R.id.ImageView); 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        myImage.setImageResource(R.drawable.img1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_image, menu);
        return true;
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
