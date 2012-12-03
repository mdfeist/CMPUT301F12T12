package ca.ualberta.cs.completemytask.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import ca.ualberta.cs.completemytask.database.DatabaseManager;
import ca.ualberta.cs.completemytask.database.JSONParser;
import junit.framework.TestCase;

public class WebServiceTest extends TestCase {

	private static final String TAG = "WebServiceTest";

	public WebServiceTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	// Check for some kind of response
	public void testGetResponse() {
		JSONParser parser = new JSONParser();
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "Unknown"));

		JSONObject response = parser
				.getJSONFromUrl(DatabaseManager.URL, params);
		assertNull("Error with database connection", response);
	}

	public void testList() {

		JSONParser parser = new JSONParser();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action",
				DatabaseManager.list_tasks_tag));
		params.add(new BasicNameValuePair("limit", String.valueOf(10)));
		params.add(new BasicNameValuePair("date", "0"));
		JSONObject json = parser.getJSONFromUrl(DatabaseManager.URL, params);

		// Check to see if we get a proper response
		assertNotNull("Error with getting server response.", json);

		try {
			// Check to see have a success key and it's equal to 1
			if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
				String res = json.getString(DatabaseManager.KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {

				} else {
					fail("Success key is equal to 0");
				}
			} else {
				fail("No Success key");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// Check to if login is correct
	public void testLogin() {
		// create user
		DatabaseManager.getInstance().createUser("test", "", "test");

		// Test incorrect login
		JSONObject json = DatabaseManager.getInstance().login("", "");

		assertNotNull("Error with getting server response.", json);

		try {
			if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
				String res = json.getString(DatabaseManager.KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					fail("Success key is equal to 1");
				} else {
					if (json.getString(DatabaseManager.KEY_ERROR) != null) {
						String res_error = json
								.getString(DatabaseManager.KEY_ERROR);
						if (Integer.parseInt(res_error) == 1) {

						} else {
							fail("Error key is equal to 0");
						}
					} else {
						fail("No Error key");
					}
				}
			} else {
				fail("No Success key");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Test incorrect login
		json = DatabaseManager.getInstance().login("Test", "Test");

		assertNotNull("Error with getting server response.", json);

		try {
			if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
				String res = json.getString(DatabaseManager.KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					fail("Success key is equal to 1");
				} else {
					if (json.getString(DatabaseManager.KEY_ERROR) != null) {
						String res_error = json
								.getString(DatabaseManager.KEY_ERROR);
						if (Integer.parseInt(res_error) == 1) {

						} else {
							fail("Error key is equal to 0");
						}
					} else {
						fail("No Error key");
					}
				}
			} else {
				fail("No Success key");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		json = DatabaseManager.getInstance().login("test", "apple");

		assertNotNull("Error with getting server response.", json);

		try {
			if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
				String res = json.getString(DatabaseManager.KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {
					fail("Success key is equal to 1");
				} else {
					if (json.getString(DatabaseManager.KEY_ERROR) != null) {
						String res_error = json
								.getString(DatabaseManager.KEY_ERROR);
						if (Integer.parseInt(res_error) == 1) {

						} else {
							fail("Error key is equal to 0");
						}
					} else {
						fail("No Error key");
					}
				}
			} else {
				fail("No Success key");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		json = DatabaseManager.getInstance().login("test", "test");

		assertNotNull("Error with getting server response.", json);

		try {
			if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
				String res = json.getString(DatabaseManager.KEY_SUCCESS);
				if (Integer.parseInt(res) == 1) {

				} else {
					fail("Success key is not equal to 1");
				}
			} else {
				fail("No Success key");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
