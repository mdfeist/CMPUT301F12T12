package ca.ualberta.cs.completemytask;

import android.app.Application;
import android.content.Context;

public class CompleteMyTask extends Application {
	private static Context context;

    public void onCreate(){
        super.onCreate();
        CompleteMyTask.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return CompleteMyTask.context;
    }
}
