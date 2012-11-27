package ca.ualberta.cs.completemytask.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import ca.ualberta.cs.completemytask.R;
import ca.ualberta.cs.completemytask.database.DatabaseManager;
import ca.ualberta.cs.completemytask.settings.Settings;
import ca.ualberta.cs.completemytask.userdata.User;

@SuppressLint("HandlerLeak")
public class LoginActivity extends CustomActivity {

	private static final String TAG = "LoginActivity";
	
	private static final int UPDATE_ERROR = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
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
					Message msg = handler.obtainMessage();
					msg.what = UPDATE_ERROR;
					msg.obj = "Error: Can't connect to server.";
					handler.sendMessage(msg);
					return;
				}

				// check for login response
				try {
					if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
						String res = json.getString(DatabaseManager.KEY_SUCCESS);
						if (Integer.parseInt(res) == 1) {

							JSONObject j_user = json.getJSONObject("user");
							String email = j_user.getString("email");

							// Create new User
							User user = new User(username, email);
							user.setPassword(password);

							Settings.getInstance().setUser(user);
							Settings.getInstance().save(getBaseContext());

							User u = Settings.getInstance().getUser();
							Log.v(TAG, "Name: " + u.getUserName());
							
							Intent intent = new Intent();
							setResult(RESULT_OK, intent);
							// Close Login Screen
							finish();
						} else {
							if (json.getString(DatabaseManager.KEY_ERROR) != null) {
								String res_error = json.getString(DatabaseManager.KEY_ERROR);
								if (Integer.parseInt(res_error) == 1) {
									String error = json.getString(DatabaseManager.KEY_ERROR_MSG);
									
									Message msg = handler.obtainMessage();
									msg.what = UPDATE_ERROR;
									msg.obj = error;
									handler.sendMessage(msg);
								}
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == UPDATE_ERROR) {
				TextView errorMessageView = (TextView) findViewById(R.id.UserNameErrorMessage);
				errorMessageView.setText((String) msg.obj);
			}
			super.handleMessage(msg);
		}
	};

	public void cancel(View view) {
		this.finish();
	}
}
