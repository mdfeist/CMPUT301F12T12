package ca.ualberta.cs.completemytask.activities;

import ca.ualberta.cs.completemytask.R;
import ca.ualberta.cs.completemytask.activities.MainMenuActivity.Display;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

public class CustomActivity extends Activity {
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_settings:
            	settings(null);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * Called when user wants to edit their information.
     * @param A view
     */
   public void settings(View view) {
    	Intent intent = new Intent(this, SettingsActivity.class);
    	startActivityForResult(intent, Display.SETTINGS.getValue());
    } 
}
