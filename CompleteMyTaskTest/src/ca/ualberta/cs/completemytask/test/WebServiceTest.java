package ca.ualberta.cs.completemytask.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import ca.ualberta.cs.completemytask.WebService;
import ca.ualberta.cs.completemytask.database.DatabaseManager;
import junit.framework.TestCase;

public class WebServiceTest extends TestCase {

	private static final String TAG = "WebServiceTest";
	private WebService web;
	
	public WebServiceTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		web = new WebService(DatabaseManager.getDatabaseURL());
	}
	
	public void testGetData() {
		String response = web.getJSONList();
		
		if (response == null) {
			Log.w(TAG, "Failed to get the list from database");
			return;
		}
		
		JSONArray jsonlist;
		String id = null;
		
		try {
			jsonlist = new JSONArray(response);

			for (int i = 0; i < jsonlist.length(); ++i) {
				JSONObject idObject = jsonlist.getJSONObject(i);
				id = idObject.getString("id");
				
				Log.v(TAG, web.getJSONObjectWithID(id).toString());
			}

		} catch (JSONException e) {
			Log.w(TAG, e.toString());
		}
	}
	
	
	public void testList() {
		String response = web.getJSONList();
		
		assertNotNull("Error with database.", response);
	}

}
