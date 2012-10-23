package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;

/**
 * 
 * @author Michael Feist
 *
 */

public class MainMenuActivity extends Activity {

	private static final String TAG = "MainMenuActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        // add a click-listener on the add button
        Button addTaskButton = (Button) findViewById(R.id.AddTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
        	}
        });
        
        createFakeTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }
    
    public void createFakeTable() {
    	for (int i = 0; i < 50; i ++) {
	    	// get a reference for the TableLayout
    		ListView list = (ListView) findViewById(R.id.taskList);
	    	
	        // create a new TableRow
	        final Task task = new Task("My Task " + i, "");
	        
	        // Getting adapter by passing xml data ArrayList
	        TaskAdapter adapter = new TaskAdapter(this);
	        list.setAdapter(adapter);
	 /*
	        // Click event for single list row
	        list.setOnItemClickListener(new OnItemClickListener() {
	 
	            @Override
	            public void onItemClick(AdapterView&lt;?&gt; parent, View view,
	                    int position, long id) {
	 
	            }
	        });
	        */
    	}
    	
    }

}
