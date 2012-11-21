package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class NewUserActivity extends Activity {
	
	/**
	 *  Used to know what display are we going to 
	 *  and coming from
	 */
	enum Display {
		LOGIN(1),
		CREATE(2);
		
		private final int value;
	    private Display(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_user, menu);
        return true;
    }
    
    public void createUser(View view) {
    	Intent intent = new Intent(this, CreateUserActivity.class);
    	startActivityForResult(intent, Display.CREATE.getValue());
    }
    
    public void login(View view) {
    	Intent intent = new Intent(this, LoginActivity.class);
    	startActivityForResult(intent, Display.LOGIN.getValue());
    }
    
    public void cancel(View view) {
    	this.finish();
    }
    
    /**
     * Called when an intent is finished and data is returned.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	// Login
        if(requestCode == Display.LOGIN.getValue()) {
            if(resultCode == RESULT_OK && intent != null) {
            	
            }
        }
        
        // Login
        if(requestCode == Display.CREATE.getValue()) {
            if(resultCode == RESULT_OK && intent != null) {
            	
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
