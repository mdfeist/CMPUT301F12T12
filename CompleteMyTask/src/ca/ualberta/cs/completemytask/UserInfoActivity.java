package ca.ualberta.cs.completemytask;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A view for entering user information.
 * 
 * @author Michael Feist
 *
 */
public class UserInfoActivity extends Activity {

	private static final String TAG = "UserInfoActivity";
	private boolean newUser;
	private boolean dataCleared;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        
        dataCleared = false;
        
        if(Settings.getInstance().hasUser()) {
        	newUser = false;
        	String userName = Settings.getInstance().getUserName();
        	EditText editUserNameEditText = (EditText) findViewById(R.id.EditUserName);
        	editUserNameEditText.setText(userName);
    	} else {
    		newUser = true;
    	}
        
        TextView errorMessageView = (TextView) findViewById(R.id.UserNameErrorMessage);
		errorMessageView.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_info, menu);
        return true;
    }
    
    public void deleteLocalData(View view) {
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("This will delete all tasks stored on the local drive.");
        builder.setCancelable(true);
    	// Add the buttons
    	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	               // User clicked OK button
    	        	   // Clear local data
    	        	   Log.v(TAG, "Deleting local data file.");
    	        	   File localDataFile = new File(getFilesDir(), Settings.DATA_NAME);
    	        	   TaskManager.getInstance().deleteLocalData(localDataFile);
    	        	   
    	        	   dataCleared = true;
    	           }
    	       });
    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	               // User cancelled the dialog
    	           }
    	       });

    	// Create the AlertDialog
    	AlertDialog dialog = builder.create();
        dialog.show();
    	
    }
    
    public void newUser(View view) {
    	
    	EditText editUserNameEditText = (EditText) findViewById(R.id.EditUserName);
    	
    	String userName = editUserNameEditText.getText().toString();
    	
    	boolean needsUpdate = true;
    	Intent intent = new Intent();
    	
    	if (dataCleared) {
    		intent.putExtra("Data", true);
    	}
    	
    	if (!newUser) {
    		if(Settings.getInstance().getUserName().equals(userName)) {
    			needsUpdate = false;
    		}
    	}
    	
    	if (needsUpdate) {
	    	// Check if user name valid
	    	if (userName.length() < 5 ||
	    			userName.length() > 25) {
	    		TextView errorMessageView = (TextView) findViewById(R.id.UserNameErrorMessage);
	    		errorMessageView.setText("User name must be between 5 and 25 characters");
	    		return;
	    	}
	    	
	    	// Create new User
	    	User user = new User(userName);
	    	Settings.getInstance().setUser(user);
	    	
	    	intent.putExtra("User", true);
    	}
    	
    	setResult(RESULT_OK, intent);
    	this.finish();
    }
}
