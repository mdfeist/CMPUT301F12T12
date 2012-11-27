package ca.ualberta.cs.completemytask.userdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
		
		//Put username and audio into the container that will populate the ListView entries
		//Put the Audio file name here?
		//Put the username after?
		String tempUserName = (task.getAudioAt(position).getAudioUser());
		String tempAudioName = (task.getAudioAt(position).getAudioName());
		
		//Define a map
		
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		//for (RSSItem item : feed.getAllItems()) {
		    Map<String, String> datum = new HashMap<String, String>(2);
		    //datum.put("title", item.getTitle());
		    datum.put("Audio", tempAudioName);
		    datum.put("Name", tempUserName);
		    data.add(datum);
		//}
		    
		//put the data in the map?    
		//does there need to be an XML file for the ListView (like asn 1?)
		    
		SimpleAdapter adapter = new SimpleAdapter(context, data,
                android.R.layout.simple_list_item_2,
                new String[] {"Audio", "Name"},
                new int[] {android.R.id.text1,
                           android.R.id.text2});
		i.setAdapter(adapter);
		
		//i.setImageBitmap(task.getPhotoAt(position).getContent());
		//i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

		return i;
	}

}
