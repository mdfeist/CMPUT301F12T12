package ca.ualberta.cs.completemytask;

import java.util.GregorianCalendar;

import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;

public class Task extends TableRow {
	
	boolean shared;
	boolean complete;
	
	String name;
	String description;
	
	GregorianCalendar date;
	
	Task(Context context) {
		this(context, "New Task", "No Description");
	}
	
	Task(Context context, String name, String description) {
		super(context);
		this.shared = false;
		this.complete = false;
		
		this.name = name;
		this.description = description;
		
		this.date = new GregorianCalendar();
		
		// create a new TextView for Task Name
        TextView nameField = new TextView(context);
        // set the text
        nameField.setText(name);
        
        // add the TextView and the Button to the new TableRow
        this.addView(nameField);
	}
	
	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public GregorianCalendar getDate() {
		return date;
	}
}
