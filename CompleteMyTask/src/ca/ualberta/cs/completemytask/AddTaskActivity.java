package ca.ualberta.cs.completemytask;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AddTask extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_task, menu);
        return true;
    }
}
