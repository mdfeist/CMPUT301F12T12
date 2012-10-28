package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class CommentActivity extends Activity {

	private static final String TAG = "CommentActivity";
	private EditText comment;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        
        comment = (EditText) findViewById(R.id.Comment);
        comment.setOnEditorActionListener(new EditText.OnEditorActionListener() {
        	
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // Send the user message
                	send(null);
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_comment, menu);
        return true;
    }
    
    public void send(View view) {
    	Log.v(TAG, "Send");
    }
    
    public void close(View view) {
    	this.finish();
    }
}
