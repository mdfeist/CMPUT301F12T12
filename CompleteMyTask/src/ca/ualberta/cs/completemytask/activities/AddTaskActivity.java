package ca.ualberta.cs.completemytask.activities;

import ca.ualberta.cs.completemytask.R;
import ca.ualberta.cs.completemytask.background.BackgroundTask;
import ca.ualberta.cs.completemytask.background.HandleInBackground;
import ca.ualberta.cs.completemytask.database.DatabaseManager;
import ca.ualberta.cs.completemytask.saving.LocalSaving;
import ca.ualberta.cs.completemytask.settings.Settings;
import ca.ualberta.cs.completemytask.userdata.Task;
import ca.ualberta.cs.completemytask.userdata.TaskManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Handles the adding of a task. The user is
 * able to enter needed information about
 * the task they want to create.
 * 
 * @author Michael Feist
 *
 */
public class AddTaskActivity extends CustomActivity {

	private static final String TAG = "AddTaskActivity";
	private int position;
	private boolean newTask;
	
	/**
	 * Creates the view
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        
        position = TaskManager.getInstance().getCurrentTaskPosition();
        
        if (position < 0) {
        	Log.v(TAG, "New Task");
        	newTask = true;
        } else {
        	Log.v(TAG, "Edit Task");
        	
        	Button commit = (Button) findViewById(R.id.AddTaskButton);
        	commit.setText("Edit Task");
        	
        	// Edit Text Views
        	EditText taskNameEditText = (EditText) findViewById(R.id.EditTaskName);
        	EditText taskDescriptionEditText = (EditText) findViewById(R.id.EditTaskDescription);
        	
        	// Check box views
        	CheckBox textRequirementCheckbox = (CheckBox) findViewById(R.id.TextRequirementCheckbox);
        	CheckBox photoRequirementCheckbox = (CheckBox) findViewById(R.id.PictureRequirementCheckbox);
        	CheckBox audioRequirementCheckbox = (CheckBox) findViewById(R.id.AudioRequirementCheckbox);
        	
        	Task task = TaskManager.getInstance().getTaskAt(position);
        	
        	taskNameEditText.setText(task.getName());
        	taskDescriptionEditText.setText(task.getDescription());
        	
        	textRequirementCheckbox.setChecked(task.needsComment());
        	photoRequirementCheckbox.setChecked(task.needsPhoto());
        	audioRequirementCheckbox.setChecked(task.needsAudio());
        	
        	newTask = false;
        }
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if (position >= 0) {
	    	Button share = (Button) findViewById(R.id.ShareTaskButton);
	    	Task task = TaskManager.getInstance().getTaskAt(position);
	    	
	    	if (task.isPublic()) {
	    		share.setClickable(false);
	    		share.setAlpha(0.0f);
	    	} else {
	    		share.setClickable(true);
	    		share.setAlpha(1.0f);
	    	}
    	}
    }

    /**
     * Creates the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_task, menu);
        return true;
    }
    
    /**
     * Called when finished.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.v(TAG, "Finishing");
    }
    
    /**
     * Cancels the current task and returns to previous activity.
     * @param view
     */
    public void cancel(View view) {
    	this.finish();
    }
    
    /**
     * Makes the task public and commits changes, then returns to previous activity.
     * @param view
     */
    public void shareTask(View view) {
    	Task task = null;
    	if (newTask) {
	    	task = new Task();
	    	position = TaskManager.getInstance().addTask(task);
			newTask = false;
    	} else {
    		task = TaskManager.getInstance().getTaskAt(position);
    	}
		
		task.setPublic(true);
		
		addTask(view);
    }
    
    /**
     * Creates a new Task and adds it to the TaskManager, then returns to previous activity.
     * @param view
     */
    public void addTask(View view) {
    	// Edit Text Views
    	EditText taskNameEditText = (EditText) findViewById(R.id.EditTaskName);
    	EditText taskDescriptionEditText = (EditText) findViewById(R.id.EditTaskDescription);
    	
    	// Check box views
    	CheckBox textRequirementCheckbox = (CheckBox) findViewById(R.id.TextRequirementCheckbox);
    	CheckBox photoRequirementCheckbox = (CheckBox) findViewById(R.id.PictureRequirementCheckbox);
    	CheckBox audioRequirementCheckbox = (CheckBox) findViewById(R.id.AudioRequirementCheckbox);
    	
    	//Task Data
    	String taskName = taskNameEditText.getText().toString();
    	String taskDescription = taskDescriptionEditText.getText().toString();
    	
    	Task task = null;
    	
    	if (newTask) {
    		task = new Task(taskName, taskDescription);
    		position = TaskManager.getInstance().addTask(task);
    		
    		Log.v(TAG, "Creating New Task: \n" + task.toString());
    	} else {
    		task = TaskManager.getInstance().getTaskAt(position);
    		task.setName(taskName);
    		task.setDescription(taskDescription);
    		
    		Log.v(TAG, "Editing Task: \n" + task.toString());
    	}
    	
    	if (Settings.getInstance().hasUser()) {
    		task.setUser(Settings.getInstance().getUser());
    	}
    	
    	task.setRequirements(textRequirementCheckbox.isChecked(), 
    			photoRequirementCheckbox.isChecked(), 
    			audioRequirementCheckbox.isChecked());
    	
    	Intent intent = new Intent();
    	
    	if (task.isPublic()) {
    		BackgroundTask bg = new BackgroundTask();
        	bg.runInBackGround(new HandleInBackground() {
        		public void onPreExecute() {
        		}
        		
        		public void onPostExecute(int response) {
        		}
        		
        		public void onUpdate(int response) {
        		}
        		
        		public boolean handleInBackground(Object o) {
        			DatabaseManager.getInstance().syncTaskToDatabase(position);
    				return true;
        		}
        	});
    	}
		
    	LocalSaving saver = new LocalSaving();
		saver.open();
		saver.saveTask(TaskManager.getInstance().getTaskAt(position));
		saver.close();
    	
    	setResult(RESULT_OK, intent);
    	this.finish();
    }
}
