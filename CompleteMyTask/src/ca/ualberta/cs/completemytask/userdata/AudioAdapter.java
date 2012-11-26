package ca.ualberta.cs.completemytask.userdata;

import android.widget.BaseAdapter;
import android.widget.ListView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


public class AudioAdapter extends BaseAdapter {
	private Context context;
	private Task task = null;
	
	public AudioAdapter(Context c) {
		this.context = c;
	}
	
	public AudioAdapter(Context c, Task task) {
		this.context = c;
		this.task = task;
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
		ListView i = new ListView(context);
		
		//Put the Audio file name here?
		//Put the username after?
		
		
		//i.setImageBitmap(task.getPhotoAt(position).getContent());
		//i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

		return i;
	}

}
