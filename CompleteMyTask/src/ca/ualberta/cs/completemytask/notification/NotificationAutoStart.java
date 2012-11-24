package ca.ualberta.cs.completemytask.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationAutoStart extends BroadcastReceiver {

	private static final String TAG = "NotificationAutoStart";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Starting Notification Service");
		NotificationSender alarm = new NotificationSender();
		alarm.SetAlarm(context);
	}
	
}
