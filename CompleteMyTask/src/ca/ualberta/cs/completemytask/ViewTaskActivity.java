package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ViewTaskActivity extends Activity {

	// Postion of Task in TaskManager
	private int position;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        
        position = (int) getIntent().getIntExtra("Task Position", -1);
        
        if (position >= 0) {
        	Task task = TaskManager.getInstance().getTaskAt(position);
        	
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_task, menu);
        return true;
    }
    
    public void closeTask(View view) {
    	this.finish();
    }
}
