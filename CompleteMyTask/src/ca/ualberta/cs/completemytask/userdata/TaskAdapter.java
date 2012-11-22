package ca.ualberta.cs.completemytask.userdata;

import ca.ualberta.cs.completemytask.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Allows a list view to show the tasks in the TaskManager.
 * <br/>
 * <br/>
 * Original design from Android Hive
 * <br/>
 * <a href="http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/">
 * http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/
 * </a>
 * @author Michael Feist
 * 
 */

public class TaskAdapter extends BaseAdapter {
	// private Activity activity;
	private LayoutInflater inflater = null;

	public TaskAdapter(Activity activity) {
		// this.activity = activity;
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return TaskManager.getInstance().size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	
	
	// Displays a custom list item view
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_row, null);

		TextView shared = (TextView) vi.findViewById(R.id.shared); // Shared
		TextView title = (TextView) vi.findViewById(R.id.title); // title
		TextView user = (TextView) vi.findViewById(R.id.user); // user name
		TextView date = (TextView) vi.findViewById(R.id.date); // duration

		Task task = TaskManager.getInstance().getTaskAt(position);

		// Setting all values in listview
		if (task.isPublic()) {
			shared.setText("Public");
			shared.setTextColor(Color.parseColor("#10BCC9"));
		} else {
			shared.setText("Private");
			shared.setTextColor(0xFFFF0000);
		}

		String userName = "Unknown";
		
		if (task.hasUser()) {
			userName = task.getUser().getUserName();
		}
		
		title.setText(task.getName());
		user.setText("Created by: " + userName);
		date.setText(task.getDateAsString());
		return vi;
	}
}
