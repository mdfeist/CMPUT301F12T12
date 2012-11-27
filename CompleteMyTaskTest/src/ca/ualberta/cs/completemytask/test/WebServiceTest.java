package ca.ualberta.cs.completemytask.test;

import junit.framework.TestCase;

public class WebServiceTest extends TestCase {

	private static final String TAG = "WebServiceTest";
	
	public WebServiceTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testGetData() {
		String response = null;
		assertNotNull("Error with database.", response);
	}
	
	
	public void testList() {
		String response = null;
		assertNotNull("Error with database.", response);
	}

}
