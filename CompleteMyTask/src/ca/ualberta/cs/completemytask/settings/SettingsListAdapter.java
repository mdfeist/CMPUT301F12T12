package ca.ualberta.cs.completemytask.settings;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cs.completemytask.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SettingsListAdapter  extends BaseAdapter {

	private LayoutInflater inflater = null;
	private List<SettingsItem> items;
	
	public SettingsListAdapter(Activity activity) {
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		this.items = new ArrayList<SettingsItem>();
	}
	
	public void addItem(SettingsItem item) {
		this.items.add(item);
	}
	
	public void removeItem(int position) {
		this.items.remove(position);
	}
	
	public int getCount() {
		return this.items.size();
	}

	public SettingsItem getItem(int position) {
		return this.items.get(position);
	}

	public long getItemId(int position) {
		return this.items.get(position).getLink();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.settings_list_row, null);

		TextView title = (TextView) vi.findViewById(R.id.title); // title
		
		title.setText(getItem(position).getTitle());
		
		return vi;
	}

}
