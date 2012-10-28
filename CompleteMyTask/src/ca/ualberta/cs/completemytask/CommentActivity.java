package ca.ualberta.cs.completemytask;

import java.io.File;

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
	private EditText commentEditText;
	private TextView commentsTextView;
	private Task task;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		int position = TaskManager.getInstance().getCurrentTaskPosition();
		task = TaskManager.getInstance().getTaskAt(position);
		
		commentsTextView = (TextView) findViewById(R.id.commentsView);
		
		int numberOfComments = task.getNumberOfComments();
		
		for (int i = 0; i < numberOfComments; i++) {
			
			Comment comment = task.getCommentAt(i);
			User user = comment.getUser();
			
			String commentsString = commentsTextView.getText().toString();
			commentsString +=  user.getUserName() + "\n\n\t" + comment.getContent() + "\n\n\n";
			commentsTextView.setText(commentsString);
		}
		
		commentEditText = (EditText) findViewById(R.id.Comment);
		commentEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
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
	
	/**
	 * Send a text to the current task.
	 * 
	 * @param A view
	 */
	public void send(View view) {
		Log.v(TAG, "Sent Comment");
		String commentString = commentEditText.getText().toString();
		commentEditText.setText("");
		
		if (commentString.length() > 0) {
			Comment comment = new Comment(commentString);
			
			User user = null;
			
			if (Settings.getInstance().hasUser()) {
				user = Settings.getInstance().getUser();
			} else {
				user = new User("Unknown");
			}
			
			comment.setUser(user);
			task.addComment(comment);
			
			File localDataFile = new File(getFilesDir(), Settings.DATA_NAME);
			TaskManager.getInstance().saveLocalData(localDataFile);
			
			String commentsString = commentsTextView.getText().toString();
			commentsString +=  user.getUserName() + "\n\n\t" + comment.getContent() + "\n\n\n";
			commentsTextView.setText(commentsString);
		}
	}

	/**
	 * Close activity.
	 * 
	 * @param A view
	 */
	public void close(View view) {
		this.finish();
	}
}
