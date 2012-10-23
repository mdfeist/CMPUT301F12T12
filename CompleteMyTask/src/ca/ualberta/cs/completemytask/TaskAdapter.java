package ca.ualberta.cs.completemytask;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaskAdapter extends BaseAdapter {
	//private Activity activity;
	private LayoutInflater inflater = null;
	
	TaskAdapter(Activity activity) {
		//this.activity = activity;
		this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	        if(convertView==null)
	            vi = inflater.inflate(R.layout.list_row, null);
	 
	        TextView title = (TextView)vi.findViewById(R.id.title); // title
	        TextView user = (TextView)vi.findViewById(R.id.artist); // artist name
	        TextView date = (TextView)vi.findViewById(R.id.date); // duration
	 
	        Task task = TaskManager.getInstance().getTaskAt(position);
	 
	        // Setting all values in listview
	        title.setText(task.getName());
	        user.setText(task.getUser());
	        date.setText(task.getDateAsString());
	        return vi;
	    }
}
