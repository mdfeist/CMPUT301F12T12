package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 
 * @author mdfeist
 * 
 * Test Comments/Commit
 *
 */

public class MainMenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        // add a click-listener on the add button
        Button addTaskButton = (Button) findViewById(R.id.AddTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	createFakeTable();
        	}
        }); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }
    
    public void createFakeTable() {
    	for (int i = 0; i < 25; i ++) {
	    	// get a reference for the TableLayout
	        TableLayout table = (TableLayout) findViewById(R.id.MyTasksTable);
	    	
	        // create a new TableRow
	        TableRow row = new TableRow(this);
	        
	        // create a new TextView for Task Name
	        TextView name = new TextView(this);
	        // set the text
	        name.setText("My Task " + i);
	        
	        // add the TextView and the Button to the new TableRow
	        row.addView(name);
	
	        // add the TableRow to the TableLayout
	        table.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	        		LayoutParams.WRAP_CONTENT));
    	}
    	
    	for (int i = 0; i < 25; i ++) {
	    	// get a reference for the TableLayout
	        TableLayout table = (TableLayout) findViewById(R.id.PublicTasksTable);
	    	
	        // create a new TableRow
	        TableRow row = new TableRow(this);
	        
	        // create a new TextView for Task Name
	        TextView name = new TextView(this);
	        // set the text
	        name.setText("Public Task " + i);
	        
	        // add the TextView and the Button to the new TableRow
	        row.addView(name);
	
	        // add the TableRow to the TableLayout
	        table.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	        		LayoutParams.WRAP_CONTENT));
    	}
    }

}
