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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        setUpSettings();
        setupList();
        
        localDataFile = new File(getFilesDir(), "CompleteMyTaskData");
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
    
    public void setUpSettings() {
    	Settings.getInstance().load(this);
    	boolean needsUser = !Settings.getInstance().hasUser();
    	
    	if (needsUser) {
    		Intent intent = new Intent(this, UserInfoActivity.class);
        	startActivityForResult(intent, USER);
    	}
    }
    
    public void editUser(View view) {
    	Intent intent = new Intent(this, UserInfoActivity.class);
    	startActivityForResult(intent, USER);
    }
    
    public void createNewTask(View view) {
    	Intent intent = new Intent(this, AddTaskActivity.class);
    	startActivityForResult(intent, ADD_TASK);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == ADD_TASK) {
            if(resultCode == RESULT_OK && intent != null) {
            	Log.v(TAG, "Task added");
            	adapter.notifyDataSetChanged();
            }
        }
        
        if(requestCode == USER) {
            if(resultCode == RESULT_OK && intent != null) {
            	Log.v(TAG, "User added: \n" + Settings.getInstance().getUserName());
            	Settings.getInstance().save(this);
            }
        }
        
        super.onActivityResult(requestCode, resultCode, intent);
    }
    
    public void sync(View view) {
    	
    	class SyncDatabasteAsyncTask extends AsyncTask<String, Void, Long>{
    		
    		@Override
    		protected void onPreExecute() {
    			enableView(false);
    			ViewStub loadViewStub = (ViewStub) findViewById(LOAD_VIEW_STUB);
    			
    			if (loadViewStub != null) {
    				loadViewStub.inflate();
    			} else {
    				View loadView = (View) findViewById(LOAD_VIEW);
    	        	loadView.setVisibility(View.VISIBLE);
    			}
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
	            
	            adapter.notifyDataSetChanged();
	            
	            enableView(true);
	            
	        	View loadView = (View) findViewById(LOAD_VIEW);
	        	loadView.setVisibility(View.INVISIBLE);
	        }        
		}
		
    	SyncDatabasteAsyncTask syncTasks = new SyncDatabasteAsyncTask();
    	syncTasks.execute(); 
    	
    }
    
    private void enableView(boolean enable) {
    	RelativeLayout layout = (RelativeLayout)findViewById(MAIN_MENU_LAYOUT);
        enableView(enable, layout);
    }
    
    private void enableView(boolean enable, ViewGroup viewGroup) {
    	for (int i = 0; i < viewGroup.getChildCount(); i++) {
    		View child = viewGroup.getChildAt(i);
    		
            if (ViewGroup.class.isAssignableFrom(child.getClass())) {
            	enableView(enable, (ViewGroup)child);
            }
            
            child.setEnabled(enable);
        }
    }
    
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
				String name = TaskManager.getInstance().getTaskAt(position)
						.getName();
				Log.v(TAG, "Clicked: " + name);
				
				Intent intent = new Intent(view.getContext(), ViewTask.class);
		    	startActivityForResult(intent, VIEW_TASK);
			}

		});
    }

}
