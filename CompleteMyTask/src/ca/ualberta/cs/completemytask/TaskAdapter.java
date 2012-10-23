package ca.ualberta.cs.completemytask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Allows a list view to show the tasks for the TaskManager.
 * 
 * @author Michael Feist
 * 
 */

public class TaskAdapter extends BaseAdapter {
	// private Activity activity;
	private LayoutInflater inflater = null;

	TaskAdapter(Activity activity) {
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
		if (task.isShared()) {
			shared.setText("Public");
			shared.setTextColor(Color.parseColor("#10BCC9"));
		} else {
			shared.setText("Private");
			shared.setTextColor(0xFFFF0000);
		}

		title.setText(task.getName());
		user.setText(task.getUser());
		date.setText(task.getDateAsString());
		return vi;
	}
}
