package com.example.kush_mish.nappy;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ButtonReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getIntExtra("notificationId", 0);

        // Stop the ringtone
        if(AlarmManagerBroadcastReceiver.mRingtone != null)
            AlarmManagerBroadcastReceiver.stopRingtone();

        // Uncheck the toggleButton


        // Cancel the notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }
}
