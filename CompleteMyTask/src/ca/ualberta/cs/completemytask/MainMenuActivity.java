package ca.ualberta.cs.completemytask;


import java.io.File;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Main Menu View
 * 
 * @author Michael Feist
 *
 */
public class MainMenuActivity extends Activity {

	private static final String TAG = "MainMenuActivity";
	private TaskAdapter adapter;
	private File localDataFile;
	
	// View id's
	//private int ADD_TASK_BUTTON = R.id.AddTaskButton;
	//private int SYNC_TASK_BUTTON = R.id.SyncTaskButton;
	private int LOAD_VIEW_STUB = R.id.stub_import;
	private int LOAD_VIEW = R.id.panel_import;
	private int MAIN_MENU_LAYOUT = R.id.main_menu_layout;
	private int TASK_LIST = R.id.TasksList;
	
	private static final int ADD_TASK = 1;
	private static final int USER = 2;
	private static final int VIEW_TASK = 3;
	
	/**
	 * Set's up the application. First method called.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        setupSettings();
        setupList();
        
        localDataFile = new File(getFilesDir(), Settings.DATA_NAME);
        TaskManager.getInstance().loadLocalData(localDataFile);
        adapter.notifyDataSetChanged();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	//this.finish();
        return;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.v(TAG, "Finishing");
        TaskManager.getInstance().saveLocalData(localDataFile);
    }
    
    /**
     * Loads the settings file from local memory.
     * Then checks if there is already a user created.
     * If there is no user defined then the user is
     * taken to the UserInfoActivity where they can
     * enter their information.
     */
    public void setupSettings() {
    	Settings.getInstance().load(this);
    	boolean needsUser = !Settings.getInstance().hasUser();
    	
    	if (needsUser) {
    		Intent intent = new Intent(this, UserInfoActivity.class);
        	startActivityForResult(intent, USER);
    	}
    }
    
    /**
     * Called when user wants to edit their information.
     * 
     * @param A view
     */
    public void editUser(View view) {
    	Intent intent = new Intent(this, UserInfoActivity.class);
    	startActivityForResult(intent, USER);
    } 
    
    /**
     * Called when the user wants to create a new task.
     * 
     * @param A view
     */
    public void createNewTask(View view) {
    	Intent intent = new Intent(this, AddTaskActivity.class);
    	TaskManager.getInstance().setCurrentTaskPosition(-1);
    	startActivityForResult(intent, ADD_TASK);
    }
    
    /**
     * Called when an intent is finished and data is returned.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	// If task added save and update the list view
        if(requestCode == ADD_TASK) {
            if(resultCode == RESULT_OK && intent != null) {
            	Log.v(TAG, "Task added");
            	
            	// If task is public then sync with database
            	if (intent.getBooleanExtra("Public", false)) {
            		int position = intent.getIntExtra("Task Position", -1);
            		if (position >= 0) {
            			syncTask(position);
            		}
            	} else {
            		TaskManager.getInstance().saveLocalData(localDataFile);
            	}
            	
            	TaskManager.getInstance().sort();
            	adapter.notifyDataSetChanged();
            }
        }
        
        // Save user updated
        if(requestCode == USER) {
            if(resultCode == RESULT_OK && intent != null) {
            	if (intent.getBooleanExtra("User", false)) {
            		Log.v(TAG, "User added: \n" + Settings.getInstance().getUserName());
            		Settings.getInstance().save(this);
            	}
            	
            	if (intent.getBooleanExtra("Data", false)) {
            		adapter.notifyDataSetChanged();
            	}
            }
        }
        
        super.onActivityResult(requestCode, resultCode, intent);
    }
    
    /**
     * Hides and displays the loading screen when the
     * application is syncing with the database.
     * 
     * @param whether the screen should be displayed
     */
    private void displaySyncScreen(boolean display) {
    	if (display) {
    		enableView(false);
			ViewStub loadViewStub = (ViewStub) findViewById(LOAD_VIEW_STUB);
			
			if (loadViewStub != null) {
				loadViewStub.inflate();
			} else {
				View loadView = (View) findViewById(LOAD_VIEW);
	        	loadView.setVisibility(View.VISIBLE);
			}
    	} else {
    		TaskManager.getInstance().sort();
    		adapter.notifyDataSetChanged();
            
            enableView(true);
            
        	View loadView = (View) findViewById(LOAD_VIEW);
        	loadView.setVisibility(View.INVISIBLE);
    	}
    }
    
    public void syncTask(final int position) {
    	class SyncTaskAsyncTask extends AsyncTask<String, Void, Long>{
    		
    		@Override
    		protected void onPreExecute() {
    			displaySyncScreen(true);
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

	            if(result == 1){
	            	Log.v(TAG, "HTTP post done");
	            }else{
	            	Log.v(TAG, "Invalid HTTP post");
	            }
	            
	            TaskManager.getInstance().saveLocalData(localDataFile);
	            displaySyncScreen(false);
	        }        
		}
		
    	SyncTaskAsyncTask syncTask = new SyncTaskAsyncTask();
    	syncTask.execute(); 
    }
    
    /**
     * Makes a call to the Database and sync's the data base with
     * the TaskManager. While the program is syncing the screen
     * is locked and a spinning load screen will appear. The
     * reason for the screen locking is we don't want the user
     * changing content while we are trying to sync. Sync's are
     * done on another thread because if done
     * on the UI thread the application will crash.
     * 
     * @param A view
     */
    public void sync(View view) {
    	
    	class SyncDatabasteAsyncTask extends AsyncTask<String, Void, Long>{
    		
    		@Override
    		protected void onPreExecute() {
    			displaySyncScreen(true);
    		}
    		
	        @Override
	        protected Long doInBackground(String... params) {
	        	
	        	DatabaseManager.getInstance().syncDatabase();
	        	
				return (long) 1;
			}
	        
	        @Override
	        protected void onProgressUpdate(Void... voids) {
	        	
	        }
	        
	        @Override
	        protected void onPostExecute(Long result) {
	            super.onPostExecute(null);

	            if(result == 1){
	            	Log.v(TAG, "HTTP post done");
	            }else{
	            	Log.v(TAG, "Invalid HTTP post");
	            }
	            
	            displaySyncScreen(false);
	        }        
		}
		
    	SyncDatabasteAsyncTask syncTasks = new SyncDatabasteAsyncTask();
    	syncTasks.execute(); 
    	
    }
    
    /**
     * Disables the main view so buttons can't be
     * clicked.
     * 
     * @param enable or disable
     */
    private void enableView(boolean enable) {
    	RelativeLayout layout = (RelativeLayout)findViewById(MAIN_MENU_LAYOUT);
        enableView(enable, layout);
    }
    
    /**
     * A helper class of enableView(boolean) to find all
     * the views contained in ViewGroups.
     * 
     * @param enable or disable
     * @param A ViewGroup
     */
    private void enableView(boolean enable, ViewGroup viewGroup) {
    	for (int i = 0; i < viewGroup.getChildCount(); i++) {
    		View child = viewGroup.getChildAt(i);
    		
            if (ViewGroup.class.isAssignableFrom(child.getClass())) {
            	enableView(enable, (ViewGroup)child);
            }
            
            child.setEnabled(enable);
        }
    }
    
    /**
     * Create a new list adapter and 
     * click listener for the main
     * menu list.
     */
    public void setupList() {
    	// get a reference for the TableLayout
		ListView list = (ListView) findViewById(TASK_LIST);

		// Custom Adapter
		adapter = new TaskAdapter(this);
		list.setAdapter(adapter);

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Task task = TaskManager.getInstance().getTaskAt(position);
				Log.v(TAG, "Clicked: " + task.getName());
				
				Intent intent;
				
				if (task.isLocal() &&
						!task.isPublic()) {
					intent = new Intent(view.getContext(), AddTaskActivity.class);
					TaskManager.getInstance().setCurrentTaskPosition(position); 
			    	startActivityForResult(intent, ADD_TASK);
				} else {
					intent = new Intent(view.getContext(), ViewTaskActivity.class);
					TaskManager.getInstance().setCurrentTaskPosition(position); 
			    	startActivityForResult(intent, VIEW_TASK);
				}
			}

		});
    }

}
