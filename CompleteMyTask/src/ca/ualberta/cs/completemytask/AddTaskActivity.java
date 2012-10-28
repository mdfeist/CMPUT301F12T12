package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
	
	/**
	 * Creates the view
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
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
    	
    	Task task = new Task(taskName, taskDescription);
    	
    	if (Settings.getInstance().hasUser()) {
    		task.setUser(Settings.getInstance().getUser());
    	}
    	
    	task.setRequirements(textRequirementCheckbox.isChecked(), 
    			photoRequirementCheckbox.isChecked(), 
    			audioRequirementCheckbox.isChecked());
    	
    	Log.v(TAG, task.toString());
    	
    	
    	TaskManager.getInstance().addTask(task);
    	
    	Intent intent = new Intent();
    	setResult(RESULT_OK, intent);
    	this.finish();
    }
}
