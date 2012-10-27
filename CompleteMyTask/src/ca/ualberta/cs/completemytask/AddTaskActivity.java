package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddTaskActivity extends Activity {

	private static final String TAG = "AddTaskActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_task, menu);
        return true;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.v(TAG, "destroyed");
    }
    
    public void cancel(View view) {
    	this.finish();
    }
    
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
