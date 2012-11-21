package ca.ualberta.cs.completemytask;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
@SuppressLint("HandlerLeak")
public class UserInfoActivity extends Activity {

	private static final String TAG = "UserInfoActivity";
	private static final int UPDATE_ERROR = 1;
	private static String KEY_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);

		if (Settings.getInstance().hasUser()) {
			User user = Settings.getInstance().getUser();
			String userName = user.getUserName();
			String email = user.getEmail();
			EditText editUserNameEditText = (EditText) findViewById(R.id.EditUserName);
			EditText editEmailEditText = (EditText) findViewById(R.id.EditEmail);
			editUserNameEditText.setText(userName);
			editEmailEditText.setText(email);
		}

		TextView errorMessageView = (TextView) findViewById(R.id.UserNameErrorMessage);
		errorMessageView.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_user_info, menu);
		return true;
	}

	public void login(View view) {
		new Thread(new Runnable() {
			public void run() {
				EditText editUserNameEditText = (EditText) findViewById(R.id.EditUserName);
				EditText passwordEditText = (EditText) findViewById(R.id.EditPassword);

				String username = editUserNameEditText.getText().toString();
				String password = passwordEditText.getText().toString();

				// Check if user name valid
				if (username.length() < 5 || username.length() > 25) {
					Message msg = handler.obtainMessage();
				    msg.what = UPDATE_ERROR;
				    msg.obj = "User name must be between 5 and 25 characters";
				    handler.sendMessage(msg);
					return;
				}

				JSONObject json = DatabaseManager.getInstance().login(username,
						password);

				if (json == null) {
					return;
				}

				Log.v(TAG, json.toString());

				// check for login response
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (Integer.parseInt(res) == 1) {
							
							JSONObject j_user = json.getJSONObject("user");
							String email = j_user.getString("email");
							
							// Create new User
							User user = new User(username, email);
							user.setPassword(password);

							Settings.getInstance().setUser(user);
							Settings.getInstance().save(getBaseContext());

							Intent intent = new Intent();
							setResult(RESULT_OK, intent);

							// Close Login Screen
							finish();
						} else {
							Message msg = handler.obtainMessage();
						    msg.what = UPDATE_ERROR;
						    msg.obj = "Login Error: invalid username or password";
						    handler.sendMessage(msg);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	final Handler handler = new Handler(){
		  @Override
		  public void handleMessage(Message msg) {
		    if(msg.what==UPDATE_ERROR){
		    	TextView errorMessageView = (TextView) findViewById(R.id.UserNameErrorMessage);
		    	errorMessageView.setText((String)msg.obj);
		    }
		    super.handleMessage(msg);
		  }
		};

	
	/**
	 * Attaches a UserName to the user to use as identification when interacting
	 * with Tasks.
	 */
	public void newUser(View view) {
		Intent intent = new Intent(this, CreateUserActivity.class);
		startActivity(intent);
	}
}
