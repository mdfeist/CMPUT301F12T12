package ca.ualberta.cs.completemytask;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class CreateUserActivity extends Activity {

	private static final String TAG = "Create User";

	Button create;

	private static final int UPDATE_ERROR = 1;
	// private static String KEY_UID = "id";
	// private static String KEY_NAME = "username";
	// private static String KEY_EMAIL = "email";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_user);

		create = (Button) findViewById(R.id.CreateButton);

		// Login button Click Event
		create.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				new Thread(new Runnable() {
					public void run() {
						EditText editUserNameEditText = (EditText) findViewById(R.id.EditUserName);
						EditText editEmailEditText = (EditText) findViewById(R.id.EditEmail);

						EditText passwordEditText = (EditText) findViewById(R.id.EditPassword);
						EditText rePasswordEditText = (EditText) findViewById(R.id.EditRePassword);

						String username = editUserNameEditText.getText()
								.toString();
						String email = editEmailEditText.getText().toString();

						String password = passwordEditText.getText().toString();
						String repassword = rePasswordEditText.getText()
								.toString();

						// Check if user name valid
						if (username.length() < 5 || username.length() > 25) {
							Message msg = handler.obtainMessage();
						    msg.what = UPDATE_ERROR;
						    msg.obj = "User name must be between 5 and 25 characters";
						    handler.sendMessage(msg);
							return;
						}
						
						if (password.length() < 4 || username.length() > 25) {
							Message msg = handler.obtainMessage();
						    msg.what = UPDATE_ERROR;
						    msg.obj = "The password must be between 4 and 25 characters";
						    handler.sendMessage(msg);
						    return;
						}

						if (!password.equals(repassword)) {
							Message msg = handler.obtainMessage();
						    msg.what = UPDATE_ERROR;
						    msg.obj = "Error: The passwords are not the same.";
						    handler.sendMessage(msg);
							return;
						}

						JSONObject json = DatabaseManager.getInstance()
								.createUser(username, email, password);

						if (json == null) {
							Message msg = handler.obtainMessage();
							msg.what = UPDATE_ERROR;
							msg.obj = "Error: Can't connect to server.";
							handler.sendMessage(msg);
							return;
						}

						Log.v(TAG, json.toString());

						// check for login response
						try {
							if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
								String res = json.getString(DatabaseManager.KEY_SUCCESS);
								if (Integer.parseInt(res) == 1) {
									// Create new User
									User user = new User(username, email);
									user.setPassword(password);

									Settings.getInstance().setUser(user);
									Settings.getInstance().save(
											getBaseContext());

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
		});

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_user, menu);
		return true;
	}
	
	public void cancel(View view) {
		this.finish();
	}
}
