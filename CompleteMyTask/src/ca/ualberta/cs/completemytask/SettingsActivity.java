package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SettingsActivity extends Activity {

	private SettingsListAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
		// get a reference for the TableLayout
		ListView list = (ListView) findViewById(R.id.SettingsListView);

		// Custom Adapter
		adapter = new SettingsListAdapter(this);
		list.setAdapter(adapter);
		
		adapter.addItem(new SettingsItem("Change User", 0, NewUserActivity.class));
		adapter.addItem(new SettingsItem("Edit User", 0, UserInfoActivity.class));

		// Click event for single list row
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SettingsItem item = adapter.getItem(position);
				Intent intent = new Intent(view.getContext(), item.getActivity());
				startActivity(intent);
			}

		});
        
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return true;
    }
    
    public void cancel(View view) {
		this.finish();
	}
}
