package ca.ualberta.cs.completemytask;

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
public class AddTaskActivity extends Activity {

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
        
        position = (int) getIntent().getIntExtra("Task Position", -1);
        
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
    		TaskManager.getInstance().addTask(task);
    	} else {
    		task = TaskManager.getInstance().getTaskAt(position);
    		task.setName(taskName);
    		task.setDescription(taskDescription);
    	}
    	
    	if (Settings.getInstance().hasUser()) {
    		task.setUser(Settings.getInstance().getUser());
    	}
    	
    	task.setRequirements(textRequirementCheckbox.isChecked(), 
    			photoRequirementCheckbox.isChecked(), 
    			audioRequirementCheckbox.isChecked());
    	
    	Log.v(TAG, task.toString());
    	
    	Intent intent = new Intent();
    	
    	if (task.isPublic()) {
    		intent.putExtra("Public", true);
    		intent.putExtra("Task Position", position);
    	}
    	
    	setResult(RESULT_OK, intent);
    	this.finish();
    }
}
