package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        
        if(Settings.getInstance().hasUser()) {
        	String userName = Settings.getInstance().getUserName();
        	EditText editUserNameEditText = (EditText) findViewById(R.id.EditUserName);
        	editUserNameEditText.setText(userName);
    	}
        
        TextView errorMessageView = (TextView) findViewById(R.id.UserNameErrorMessage);
		errorMessageView.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_info, menu);
        return true;
    }
    
    public void newUser(View view) {
    	
    	EditText editUserNameEditText = (EditText) findViewById(R.id.EditUserName);
    	
    	String userName = editUserNameEditText.getText().toString();
    	
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
    	
    	Intent intent = new Intent();
    	setResult(RESULT_OK, intent);
    	this.finish();
    }
}
