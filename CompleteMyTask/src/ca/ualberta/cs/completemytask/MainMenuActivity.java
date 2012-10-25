package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
        
        createFakeTable();
        
        adapter.notifyDataSetChanged();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }
    
    public void setupList() {
    	// get a reference for the TableLayout
		ListView list = (ListView) findViewById(R.id.TasksList);

		// Getting adapter by passing xml data ArrayList
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
