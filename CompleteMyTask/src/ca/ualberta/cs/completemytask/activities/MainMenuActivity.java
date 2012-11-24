package ca.ualberta.cs.completemytask.activities;

import ca.ualberta.cs.completemytask.R;
import ca.ualberta.cs.completemytask.background.BackgroundTask;
import ca.ualberta.cs.completemytask.background.HandleInBackground;
import ca.ualberta.cs.completemytask.database.DatabaseManager;
import ca.ualberta.cs.completemytask.saving.LocalSaving;
import ca.ualberta.cs.completemytask.settings.Settings;
import ca.ualberta.cs.completemytask.userdata.Task;
import ca.ualberta.cs.completemytask.userdata.TaskAdapter;
import ca.ualberta.cs.completemytask.userdata.TaskManager;
import ca.ualberta.cs.completemytask.views.LoadingView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Main Menu View
 * 
 * @author Michael Feist
 *
 */
public class MainMenuActivity extends Activity{

	private static final String TAG = "MainMenuActivity";
	private TaskAdapter adapter;
	
	private LoadingView loadingView;
	public LocalSaving saver;
	
	// View id's
	//private int ADD_TASK_BUTTON = R.id.AddTaskButton;
	//private int SYNC_TASK_BUTTON = R.id.SyncTaskButton;
	//private int LOAD_VIEW_STUB = R.id.stub_import;
	//private int LOAD_VIEW = R.id.panel_import;
	private int MAIN_MENU_LAYOUT = R.id.main_menu_layout;
	private int TASK_LIST = R.id.TasksList;
	
	
	/**
	 *  Used to know what display are we going to 
	 *  and coming from
	 */
	enum Display {
		ADD_TASK(1),
		VIEW_TASK(2),
		SETTINGS(3),
		NEW_USER(4);
		
		private final int value;
	    private Display(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
	};
	
	/**
	 * Set's up the application. First method called.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        this.loadingView = new LoadingView(this, MAIN_MENU_LAYOUT,
        		"Syncing with Database");
        
        setupSettings();
        setupList();
        
        saver = new LocalSaving();
        saver.open();
        saver.loadAllTasks();
        saver.close();
        
        adapter.notifyDataSetChanged();
        
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
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
        
        saver.open();
        saver.saveAllTasks();
        saver.close();
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
    		Intent intent = new Intent(this, NewUserActivity.class);
        	startActivityForResult(intent, Display.NEW_USER.getValue());
    	}
    }
    
    /**
     * Called when user wants to edit their information.
     * @param A view
     */
    public void settings(View view) {
    	Intent intent = new Intent(this, SettingsActivity.class);
    	startActivityForResult(intent, Display.SETTINGS.getValue());
    } 
    
    /**
     * Called when the user wants to create a new task.
     * @param A view
     */
    public void createNewTask(View view) {
    	Intent intent = new Intent(this, AddTaskActivity.class);
    	TaskManager.getInstance().setCurrentTaskPosition(-1);
    	startActivityForResult(intent, Display.ADD_TASK.getValue());
    }
    
    /**
     * Called when an intent is finished and data is returned.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	// If task added save and update the list view
        if(requestCode == Display.ADD_TASK.getValue()) {
            if(resultCode == RESULT_OK && intent != null) {
            	Log.v(TAG, "Task added");
            	
            	TaskManager.getInstance().sort();
            	adapter.notifyDataSetChanged();
            }
        }
        
        // Save user updated
        if(requestCode == Display.SETTINGS.getValue()) {
            if(resultCode == RESULT_OK && intent != null) {
            	
            }
        }
        
        super.onActivityResult(requestCode, resultCode, intent);
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
				
				Intent intent = new Intent(view.getContext(), ViewTaskActivity.class);
				TaskManager.getInstance().setCurrentTaskPosition(position); 
		    	startActivityForResult(intent, Display.VIEW_TASK.getValue());
			}

		});
    }

    /**
     * Makes a call to the Database and sync's the data base with
     * the TaskManager. While the program is syncing the screen
     * is locked and a spinning load screen will appear. The
     * reason for the screen locking is we don't want the user
     * changing content while we are trying to sync. Sync's are
     * done on another thread because if done
     * on the UI thread the application will crash.
     * @param A view
     */
    public void sync(View view) {
    	BackgroundTask bg = new BackgroundTask();
    	
    	
	    	bg.runInBackGround(new HandleInBackground() {
	    		public void onPreExecute() {
	    		}
	    		
	    		public void onPostExecute(int response) {
	    			TaskManager.getInstance().sort();
	    	 		adapter.notifyDataSetChanged();
	    		}
	    		
	    		public void onUpdate(int response) {
	    			TaskManager.getInstance().sort();
	    	 		adapter.notifyDataSetChanged();
	    		}
	    		
	    		public boolean handleInBackground(Object o) {	
					return DatabaseManager.getInstance().syncDatabase();
	    		}
	    	});

    }
    
    public void syncTask(final int position) {
    	BackgroundTask bg = new BackgroundTask();
    	bg.runInBackGround(new HandleInBackground() {
    		public void onPreExecute() {
    			loadingView.showLoadView(true);	
    		}
    		
    		public void onPostExecute(int response) {
    			TaskManager.getInstance().sort();
    	 		adapter.notifyDataSetChanged();
    	 		
    			loadingView.showLoadView(false);
    		}
    		
    		public void onUpdate(int response) {
    		}
    		
    		public boolean handleInBackground(Object o) {
    			DatabaseManager.getInstance().syncTaskToDatabase(position);
				return true;
    		}
    	});
    	
    }

}
