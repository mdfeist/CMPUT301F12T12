package ca.ualberta.cs.completemytask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Space;
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
	
	private Button editTask;
	private Space spacer;
	private int spacerIndex;
	
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
        
        saver = new LocalSaving(this);
        
        position = TaskManager.getInstance().getCurrentTaskPosition();
    	
		if (position >= 0) {
			Task task = TaskManager.getInstance().getTaskAt(position);

			if (!task.isPublic()) {
				editTask = new Button(this);
				editTask.setText("Edit Task");
				editTask.setBackgroundResource(R.drawable.button_selector);
				editTask.setTextColor(Color.WHITE);

				editTask.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						editTask(v);
					}
				});

				spacer = (Space) findViewById(R.id.EditTaskSpacer);

				editTask.setLayoutParams(spacer.getLayoutParams());

				ViewGroup parent = (ViewGroup) spacer.getParent();
				spacerIndex = parent.indexOfChild(spacer);
				parent.removeView(spacer);

				parent.addView(editTask, spacerIndex);
			}
		}
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	position = TaskManager.getInstance().getCurrentTaskPosition();
    	
		if (position >= 0) {
			Task task = TaskManager.getInstance().getTaskAt(position);
			
			if (task.isPublic()) {
				if (editTask != null) {
					ViewGroup parent = (ViewGroup) editTask.getParent();
					
					if (parent != null) {
						parent.removeView(editTask);
						parent.addView(spacer, spacerIndex);
					}
				}
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
    
    public void editTask(View view) {
    	Intent intent = new Intent(view.getContext(), AddTaskActivity.class);
    	startActivityForResult(intent, EDIT_TASK);
    }
    
    /**
     * Called to view the comments for a particular task
     * @param A view
     */
    public void viewComments(View view){
    	Intent intent = new Intent(this, CommentActivity.class);
    	startActivityForResult(intent, VIEW_COMMENTS);
    }
    
    /**
     * Called to view the images for a particular task
     * @param A view
     */
    public void viewImage(View view){
    	Intent intent = new Intent(this, ViewImageActivity.class);
    	startActivityForResult(intent, VIEW_IMAGE);
    }
    
    /**
     * Called to view the audio for a particular task
     * @param A view
     */
    public void viewAudio(View view){
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	// If task added save and update the list view
        if(requestCode == EDIT_TASK) {
            if(resultCode == RESULT_OK && intent != null) {
            	
            	int position = intent.getIntExtra("Task Position", -1);
            	
            	if (position >= 0) {
					// If task is public then sync with database
					if (intent.getBooleanExtra("Public", false)) {
						syncTask(position);
					}
					
					saver.open();
					saver.saveTask(TaskManager.getInstance().getTaskAt(position));
					saver.close();
            	}
            }
        }
    }
    
    public void syncTask(final int position) {
    	class SyncTaskAsyncTask extends AsyncTask<String, Void, Long>{
    		
    		@Override
    		protected void onPreExecute() {
    		}
    		
	        @Override
	        protected Long doInBackground(String... params) {
	        	
	        	DatabaseManager.getInstance().syncTaskToDatabase(position);
	        	
				return (long) 1;
			}
	        
	        @Override
	        protected void onProgressUpdate(Void... voids) {
	        	
	        }
	        
	        @Override
	        protected void onPostExecute(Long result) {
	            super.onPostExecute(null);
	        }        
		}
		
    	SyncTaskAsyncTask syncTask = new SyncTaskAsyncTask();
    	syncTask.execute(); 
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
