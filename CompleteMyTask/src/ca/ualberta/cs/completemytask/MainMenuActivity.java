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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        setupList();
        
        // add a click-listener on the add button
        Button addTaskButton = (Button) findViewById(R.id.AddTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	 // create a new Task
    	        Task task = new Task("My Task", "");
    	        task.setShared(true);
    	        
    	        TaskManager.getInstance().addTask(task);
    	        adapter.notifyDataSetChanged();
        	}
        });
        
        // add a click-listener on the sync button
        Button syncButton = (Button) findViewById(R.id.SyncTaskButton);
        syncButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	sync();
        	}
        });
        
        createFakeTable();
        adapter.notifyDataSetChanged();
        
    }
    
    private void sync() {
    	final ViewStub loadView = (ViewStub) findViewById(R.id.stub_import);
    	
    	class SyncDatabasteAsyncTask extends AsyncTask<String, Void, Long>{
    		
    		@Override
    		protected void onPreExecute() {
    			enableView(false);
    	    	//loadView.setVisibility(View.VISIBLE);
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
	            super.onPostExecute(result);

	            if(result == 1){
	            	Log.v(TAG, "HTTP post done");
	            }else{
	            	Log.v(TAG, "Invalid HTTP post");
	            }
	            
	            adapter.notifyDataSetChanged();
	            
	        	enableView(true);
	        	//loadView.setVisibility(View.INVISIBLE);
	        }        
		}
		
    	SyncDatabasteAsyncTask syncTasks = new SyncDatabasteAsyncTask();
    	syncTasks.execute(); 
    	
    }
    
    private void enableView(boolean enable) {
    	RelativeLayout layout = (RelativeLayout)findViewById(R.id.main_menu_layout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(enable);
            
            if (!enable) {
            	child.setAlpha(0.5f);
            } else {
            	child.setAlpha(1.0f);
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
		ListView list = (ListView) findViewById(R.id.TasksList);

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
