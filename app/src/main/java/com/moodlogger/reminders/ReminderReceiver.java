package com.moodlogger.reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationService = new Intent(context, ReminderNotificationService.class);
        notificationService.putExtras(intent.getExtras());
        context.startService(notificationService);
    }
}