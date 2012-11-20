package ca.ualberta.cs.completemytask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

	//private static final String TAG = "UserInfoActivity";
	private boolean newUser;
	private boolean dataCleared;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        
        dataCleared = false;
        
        if(Settings.getInstance().hasUser()) {
        	newUser = false;
        	User user = Settings.getInstance().getUser();
        	String userName = user.getUserName();
        	String email = user.getEmail();
        	EditText editUserNameEditText = (EditText) findViewById(R.id.EditUserName);
        	EditText editEmailEditText = (EditText) findViewById(R.id.EditEmail);
        	editUserNameEditText.setText(userName);
        	editEmailEditText.setText(email);
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
    
    /**
     * Attaches a UserName to the user to use as identification
     * when interacting with Tasks.
     */
    public void newUser(View view) {
    	
    	EditText editUserNameEditText = (EditText) findViewById(R.id.EditUserName);
    	EditText editEmailEditText = (EditText) findViewById(R.id.EditEmail);
    	
    	String userName = editUserNameEditText.getText().toString();
    	String email = editEmailEditText.getText().toString();
    	
    	boolean needsUpdate = true;
    	Intent intent = new Intent();
    	
    	if (dataCleared) {
    		intent.putExtra("Data", true);
    	}
    	
    	if (!newUser) {
    		User user = Settings.getInstance().getUser();
    		if (user != null) {
	    		if(user.getUserName().equals(userName) &&
	    				user.getEmail().equals(email)) {
	    			needsUpdate = false;
	    		}
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
	    	User user = new User(userName, email);
	    	Settings.getInstance().setUser(user);
	    	
	    	intent.putExtra("User", true);
    	}
    	
    	setResult(RESULT_OK, intent);
    	this.finish();
    }
}
