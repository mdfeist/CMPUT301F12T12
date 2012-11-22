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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);

		if (Settings.getInstance().hasUser()) {
			User user = Settings.getInstance().getUser();
			String username = user.getUserName();
			String email = user.getEmail();
			TextView userNameText = (TextView) findViewById(R.id.UserName);
			EditText editEmailEditText = (EditText) findViewById(R.id.EditEmail);
			userNameText.setText(username);
			editEmailEditText.setText(email);
		} else {
			Intent intent = new Intent(this, NewUserActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_user_info, menu);
		return true;
	}

	
	/**
	 * Attaches a UserName to the user to use as identification when interacting
	 * with Tasks.
	 */
	public void save(View view) {
		// Save User
		EditText editEmailEditText = (EditText) findViewById(R.id.EditEmail);
		final String email = editEmailEditText.getText().toString();
		
		User user = Settings.getInstance().getUser();
		user.setEmail(email);
		Settings.getInstance().save(this);
		
		final String username = user.getUserName();
		
		BackgroundTask bg = new BackgroundTask();
		bg.runInBackGround(new HandleInBackground() {
    		public void onPreExecute() {
    		}
    		
    		public void onPostExecute() {
    		}
    		
    		public int handleInBackground(Object o) {
    			DatabaseManager.getInstance().updateEmail(username, email);
				return 0;
    		}
    	});
		
		this.finish();
	}
	
	public void cancel(View view) {
		this.finish();
	}
}
