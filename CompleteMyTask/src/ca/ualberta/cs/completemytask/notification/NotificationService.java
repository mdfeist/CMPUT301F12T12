package ca.ualberta.cs.completemytask.notification;

import java.util.Timer;
import java.util.TimerTask;

import ca.ualberta.cs.completemytask.activities.MainMenuActivity;

import android.R;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

@SuppressLint({ "HandlerLeak", "HandlerLeak" })
public class NotificationService extends Service {

	private static final String TAG = "NotificationService";

	private Timer timer;

	private TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {
			updateUI.sendEmptyMessage(0);
		}
	};
	
	private Handler updateUI = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
		    super.dispatchMessage(msg);
		    showNotification();
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Service creating");
		
		timer = new Timer("UpdateTimer");
		//timer.schedule(updateTask, 1000L, 60 * 1000L);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Service destroying");

		timer.cancel();
		timer = null;
	}
	
	/**
     * Show a notification while this service is running.
     */
    private void showNotification() {
    	Log.i(TAG, "Timer task doing work");
    	
    	// Prepare intent which is triggered if the
    	// notification is selected
    	Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
    	PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

    	// Build notification
    	// Actions are just fake
    	Notification noti = new Notification.Builder(this)
    	        .setContentTitle("Task Completed")
    	        .setContentText("Subject")
    	        .setSmallIcon(R.drawable.checkbox_on_background)
    	        .setContentIntent(pIntent)
    	        .getNotification();
    	    
    	  
    	NotificationManager notificationManager = 
    	  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    	// Hide the notification after its selected
    	noti.flags |= Notification.FLAG_AUTO_CANCEL;

    	notificationManager.notify(0, noti); 
    }

}
