package ca.ualberta.cs.completemytask.notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.ualberta.cs.completemytask.activities.MainMenuActivity;
import ca.ualberta.cs.completemytask.background.BackgroundTask;
import ca.ualberta.cs.completemytask.background.HandleInBackground;
import ca.ualberta.cs.completemytask.database.DatabaseManager;
import ca.ualberta.cs.completemytask.settings.Settings;
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
	private String username;
	private JSONObject json;

	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.i(TAG, "Notification");
		
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		final PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "");
		
		BackgroundTask bg = new BackgroundTask();
    	bg.runInBackGround(new HandleInBackground() {
    		public void onPreExecute() {
    			wl.acquire();
    			
    			if(!Settings.getInstance().hasUser()) {
    				Settings.getInstance().load(context);
    			}
    			
    			username = Settings.getInstance().getUserName();
    		}
    		
    		public void onPostExecute(int response) {
    			wl.release();
    		}
    		
    		public void onUpdate(int response) {
    			
    		}
    		
    		public boolean handleInBackground(Object o) {
    			json = DatabaseManager.getInstance().getNotifications(username);
    			
    			if (json == null) {
    				return true;
    			}
    			
    			try {
    				if (json.getString(DatabaseManager.KEY_SUCCESS) != null) {
    					String res = json.getString(DatabaseManager.KEY_SUCCESS);
    					if (Integer.parseInt(res) == 1) {
    						JSONArray notificationsArray = json.getJSONArray("notifications");

    						for (int i = 0; i < notificationsArray.length(); i++) {
    							JSONObject notification = notificationsArray.getJSONObject(i);

    							String id = notification.getString("id");
    							int type = notification.getInt("type");
    							String from = notification.getString("from_user");
    							String message = notification.getString("message");
    							
    							String title = "Notification";
    							
    							switch (type) {
    							case 1:
    								title = "Task Complete By: " + from;
    								break;
    							}
    							
    							showNotification(context, title, message);
    							
    							DatabaseManager.getInstance().deleteNotifications(id);
    						}
    						
    					} else {

    						if (json.getString(DatabaseManager.KEY_ERROR) != null) {
    							String res_error = json
    									.getString(DatabaseManager.KEY_ERROR);
    							if (Integer.parseInt(res_error) == 1) {
    								String error = json
    										.getString(DatabaseManager.KEY_ERROR_MSG);
    								Log.e(TAG, error);
    							}
    						}
    					}
    				}
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
    			
				return true;
    		}
    	});
		
	}

	public void SetAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, NotificationSender.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				1000 * 30, pi); // Millisec * Second * Minute
	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, NotificationSender.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification(Context context, String title, String subject) {
		// Prepare intent which is triggered if the
		// notification is selected
		Intent intent = new Intent(context, MainMenuActivity.class);
		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);

		// Build notification
		// Actions are just fake
		Notification noti = new Notification.Builder(context)
				.setContentTitle(title).setContentText(subject)
				.setSmallIcon(R.drawable.checkbox_on_background)
				.setContentIntent(pIntent).getNotification();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, noti);
	}
}
