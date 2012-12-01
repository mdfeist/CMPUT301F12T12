package ca.ualberta.cs.completemytask.adapters;

import ca.ualberta.cs.completemytask.R;
import ca.ualberta.cs.completemytask.userdata.Task;
import ca.ualberta.cs.completemytask.userdata.User;

import android.widget.BaseAdapter;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A modified Adapter for putting the info from AudioCaptures(Title/UserName) 
 * into a ListView on the ViewAudioActivity.  The View vi is connected
 * the the audio_row.xml file in layouts.
 * 
 * @author Ian Watts
 *
 */
public class AudioAdapter extends BaseAdapter {
	//private Context context;
	private Task task = null;
	private LayoutInflater inflater = null;
	
	public AudioAdapter(Context c) {
		//this.context = c;
		this.inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public AudioAdapter(Context c, Task task) {
		//this.context = c;
		this.task = task;
		this.inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		if (task == null) {
			return 0;
		}
		
		return task.getNumberOfAudios();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		String tempUserName = "Unknown";
		String tempAudioName = "Untitled";
		
		if (convertView == null)
			//The "audio_row" will have the information for the list in it
			vi = inflater.inflate(R.layout.audio_row, null);

		//Already being passed in
		//Task task = TaskManager.getInstance().getTaskAt(position);
		
		TextView audioName = (TextView) vi.findViewById(R.id.audio_name); // Shared
		TextView userName = (TextView) vi.findViewById(R.id.user_name); // title
		//TextView dateAdded = (TextView vi.findViewById(R.id.DATE_ID); // date
		
		//Defining values to go in the TextViews
		User user = task.getAudioAt(position).getUser();
		if (user != null) {
			tempUserName = (user.getUserName());
		}
		tempAudioName = (task.getAudioAt(position).getAudioName());
		
		// Setting all values in the ListView
		audioName.setText(tempAudioName);
		audioName.setTextColor(0xFF00FF00);
		userName.setText(tempUserName);
		
		///////////////
		//Add a box for the date added
		///////////////

		return vi;
	}

}
