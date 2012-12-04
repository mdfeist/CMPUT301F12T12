package ca.ualberta.cs.completemytask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import ca.ualberta.cs.completemytask.R;
import ca.ualberta.cs.completemytask.settings.Settings;

public class NewUserActivity extends CustomActivity {
	
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
    public void onResume() {
    	super.onResume();
    	
    	Button cancel = (Button) findViewById(R.id.BackButton);
    	if (!Settings.getInstance().hasUser()) {
    		cancel.setClickable(false);
    		cancel.setAlpha(0.0f);
    	} else {
    		cancel.setClickable(true);
    		cancel.setAlpha(1.0f);
    	}
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
            	this.finish();
            }
        }
        
        // Login
        if(requestCode == Display.CREATE.getValue()) {
            if(resultCode == RESULT_OK && intent != null) {
            	this.finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
