package com.moodlogger.reminders;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.moodlogger.R;
import com.moodlogger.activities.views.impl.AddMoodLogActivity;

public class ReminderNotificationService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    Notification notification;

    // for AndroidManifest (default constructor required)
    public ReminderNotificationService() {
        this("Default");
    }

    public ReminderNotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = this.getApplicationContext();
        Intent addMoodIntent = new Intent(this, AddMoodLogActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, addMoodIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String username = PreferenceManager.getDefaultSharedPreferences(context).getString("user_name", "");
        String notificationMsg = String.format(getString(R.string.reminder_text), username);

        Resources res = this.getResources();
        notification = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setPriority(8)
                .setContentTitle(getString(R.string.reminder_title))
                .setContentText(notificationMsg).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        notification.ledARGB = Color.argb(1, 255, 153, 0);
        notification.ledOnMS = 800;
        notification.ledOffMS = 1000;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
