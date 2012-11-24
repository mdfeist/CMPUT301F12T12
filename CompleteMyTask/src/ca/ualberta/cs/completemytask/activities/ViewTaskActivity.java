package ca.ualberta.cs.completemytask.activities;

import ca.ualberta.cs.completemytask.R;
import ca.ualberta.cs.completemytask.background.BackgroundTask;
import ca.ualberta.cs.completemytask.background.HandleInBackground;
import ca.ualberta.cs.completemytask.database.DatabaseManager;
import ca.ualberta.cs.completemytask.saving.LocalSaving;
import ca.ualberta.cs.completemytask.settings.Settings;
import ca.ualberta.cs.completemytask.userdata.Task;
import ca.ualberta.cs.completemytask.userdata.TaskManager;
import ca.ualberta.cs.completemytask.userdata.User;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This class handles the ViewTask activity
 * 
 * @author Devon Waldon, Michael Feist and Ian Watts
 * 
 */

public class ViewTaskActivity extends Activity {

	// Position of Task in TaskManager
	private int position;

	// View IDs
	private static final int EDIT_TASK = 1;
	private static final int VIEW_IMAGE = 2;
	private static final int VIEW_COMMENTS = 3;
	private static final int VIEW_AUDIO = 4;

	public LocalSaving saver;

	/**
	 * Sets up the task view by populating text fields with relevant data and
	 * adding images and audio to the respective galleries
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task);

		saver = new LocalSaving();
		position = TaskManager.getInstance().getCurrentTaskPosition();

		BackgroundTask bg = new BackgroundTask();
		bg.runInBackGround(new HandleInBackground() {
			public void onPreExecute() {
			}

			public void onPostExecute(int response) {

			}

			public void onUpdate(int response) {
			}

			public boolean handleInBackground(Object o) {
				Task task = TaskManager.getInstance().getTaskAt(position);
				DatabaseManager.getInstance().setNumberOfAttachments(task);
				return true;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		position = TaskManager.getInstance().getCurrentTaskPosition();

		if (position >= 0) {
			Task task = TaskManager.getInstance().getTaskAt(position);

			User taskUser = task.getUser();

			Button editTask = (Button) findViewById(R.id.EditTaskButton);
			if (taskUser.getUserName().equals(
					Settings.getInstance().getUserName()) && !task.isComplete()) {
				editTask.setClickable(true);
				editTask.setAlpha(1.0f);
			} else {
				editTask.setClickable(false);
				editTask.setAlpha(0.0f);
			}
			
			Button completeTask = (Button) findViewById(R.id.CompleteButton);
			if (task.isComplete()) {
				completeTask.setClickable(false);
				completeTask.setAlpha(0.0f);
			} else {
				completeTask.setClickable(true);
				completeTask.setAlpha(1.0f);
			}

			TextView taskName = (TextView) findViewById(R.id.TaskName);
			taskName.setText(task.getName());

			TextView taskDescription = (TextView) findViewById(R.id.TaskDescription);
			taskDescription.setText(task.getDescription());

			String requires = "";

			if (task.needsComment()) {
				requires += "Needs Text\n";
			}

			if (task.needsPhoto()) {
				requires += "Needs Image\n";
			}

			if (task.needsAudio()) {
				requires += "Needs Audio\n";
			}

			if (requires.equals("")) {
				requires = "Nothing";
			}

			TextView taskRequirements = (TextView) findViewById(R.id.TaskRequirements);
			taskRequirements.setText(requires);
		}
	}

	private void showComplete(Task task) {

		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		helpBuilder.setTitle("Complete Task");
		helpBuilder.setMessage("Enter a message:");
		final EditText input = new EditText(this);
		input.setSingleLine();
		input.setText("");
		helpBuilder.setView(input);
		helpBuilder.setPositiveButton("Send",
				new DialogInterface.OnClickListener() {
			
					public void onClick(DialogInterface dialog, int which) {
						Task task = TaskManager.getInstance().getTaskAt(position);
						task.setComplete(true);
						
						LocalSaving saver = new LocalSaving();
						saver.open();
						saver.saveTask(task);
						saver.close();
						
						final long taskid = task.getId();
						final String from = Settings.getInstance().getUserName();
						final String to = task.getUser().getUserName();
						final String message = input.getText().toString();
						
						BackgroundTask bg = new BackgroundTask();	
				    	bg.runInBackGround(new HandleInBackground() {
				    		public void onPreExecute() {
				    		}
				    		
				    		public void onPostExecute(int response) {
				    			
				    		}
				    		
				    		public void onUpdate(int response) {
				    		}
				    		
				    		public boolean handleInBackground(Object o) {
				    			DatabaseManager.getInstance().completeTask(taskid, from, to, message);
								return true;
				    		}
				    	});
						
					}
				});

		helpBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
						
					}
				});

		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();
		helpDialog.show();

	}

	private void showNotComplete(String message) {

		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		helpBuilder.setTitle("Unable To Complete");
		helpBuilder.setMessage("Task still needs " + message + ".");

		helpBuilder.setNegativeButton("Back",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Do nothing
					}
				});

		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();
		helpDialog.show();

	}

	public void complete(View view) {

		Task task = TaskManager.getInstance().getTaskAt(position);

		if (task.needsComment()) {
			if ((task.getNumberOfComments() + task
					.getNumberOfCommentsOnServer()) == 0) {
				showNotComplete("Comment");
				return;
			}
		}

		if (task.needsPhoto()) {
			if ((task.getNumberOfPhotos() + task.getNumberOfPhotosOnServer()) == 0) {
				showNotComplete("Photo");
				return;
			}
		}

		if (task.needsAudio()) {
			if ((task.getNumberOfAudios() + task.getNumberOfAudiosOnServer()) == 0) {
				showNotComplete("Audio");
				return;
			}
		}

		showComplete(task);
	}

	public void editTask(View view) {
		Intent intent = new Intent(view.getContext(), AddTaskActivity.class);
		startActivityForResult(intent, EDIT_TASK);
	}

	/**
	 * Called to view the comments for a particular task
	 * 
	 * @param A
	 *            view
	 */
	public void viewComments(View view) {
		Intent intent = new Intent(this, CommentActivity.class);
		startActivityForResult(intent, VIEW_COMMENTS);
	}

	/**
	 * Called to view the images for a particular task
	 * 
	 * @param A
	 *            view
	 */
	public void viewImage(View view) {
		Intent intent = new Intent(this, ViewImageActivity.class);
		startActivityForResult(intent, VIEW_IMAGE);
	}

	/**
	 * Called to view the audio for a particular task
	 * 
	 * @param A
	 *            view
	 */
	public void viewAudio(View view) {
		Intent intent = new Intent(this, ViewAudioActivity.class);
		startActivityForResult(intent, VIEW_AUDIO);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_view_task, menu);
		return true;
	}

	/**
	 * Called when an intent is finished and data is returned.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// If task added save and update the list view
		if (requestCode == EDIT_TASK) {
			if (resultCode == RESULT_OK && intent != null) {

			}
		}
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
