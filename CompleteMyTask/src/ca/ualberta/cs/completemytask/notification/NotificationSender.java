package ca.ualberta.cs.completemytask.notification;

import ca.ualberta.cs.completemytask.activities.MainMenuActivity;
import android.R;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class NotificationSender extends BroadcastReceiver {
	
	private static final String TAG = "NotificationSender";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Notification");
		
		 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
         wl.acquire();
         
         showNotification(context);
         
         wl.release();
	}
	
	public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, NotificationSender.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 15, pi); // Millisec * Second * Minute
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, NotificationSender.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    
    /**
     * Show a notification while this service is running.
     */
    private void showNotification(Context context) {
    	// Prepare intent which is triggered if the
    	// notification is selected
    	Intent intent = new Intent(context, MainMenuActivity.class);
    	PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

    	// Build notification
    	// Actions are just fake
    	Notification noti = new Notification.Builder(context)
    	        .setContentTitle("Task Completed")
    	        .setContentText("Subject")
    	        .setSmallIcon(R.drawable.checkbox_on_background)
    	        .setContentIntent(pIntent)
    	        .getNotification();
    	    
    	  
    	NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    	// Hide the notification after its selected
    	noti.flags |= Notification.FLAG_AUTO_CANCEL;

    	notificationManager.notify(0, noti); 
    }
}
