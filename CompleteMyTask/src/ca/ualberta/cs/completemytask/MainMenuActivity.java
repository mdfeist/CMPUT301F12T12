package ca.ualberta.cs.completemytask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
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
	
	// View id's
	private int ADD_TASK_BUTTON = R.id.AddTaskButton;
	private int SYNC_TASK_BUTTON = R.id.SyncTaskButton;
	private int LOAD_VIEW_STUB = R.id.stub_import;
	private int LOAD_VIEW = R.id.panel_import;
	private int MAIN_MENU_LAYOUT = R.id.main_menu_layout;
	private int TASK_LIST = R.id.TasksList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        setupList();
        
        // add a click-listener on the add button
        Button addTaskButton = (Button) findViewById(ADD_TASK_BUTTON);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	 // create a new Task
    	        Task task = new Task("My Task", "");
    	        task.setPublic(true);
    	        
    	        TaskManager.getInstance().addTask(task);
    	        adapter.notifyDataSetChanged();
        	}
        });
        
        // add a click-listener on the sync button
        Button syncButton = (Button) findViewById(SYNC_TASK_BUTTON);
        syncButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	sync();
        	}
        });
        
        createFakeTable();
        adapter.notifyDataSetChanged();
        
    }
    
    private void sync() {
    	
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
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            
            if (child.getId() != LOAD_VIEW) {
	            child.setEnabled(enable);
	            
	            if (!enable) {
	            	child.setAlpha(0.5f);
	            } else {
	            	child.setAlpha(1.0f);
	            }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
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
			}

		});
    }
    
    public void createFakeTable() {
    	for (int i = 0; i < 2; i ++) {
	    	
	        // create a new Task
	        Task task = new Task("My Task " + i, "");
	        
	        TaskManager.getInstance().addTask(task);
	        
    	}
    	/*
    	for (int i = 0; i < 5; i ++) {
	    	
	        // create a new Task
	        Task task = new Task("Public Task " + i, "");
	        task.setShared(true);
	        
	        TaskManager.getInstance().addTask(task);
	        
    	}
    	*/
    }

}
