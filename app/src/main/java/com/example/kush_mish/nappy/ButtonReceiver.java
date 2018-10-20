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
//        if (AlarmManagerBroadcastReceiver.mRingtone != null)
//            AlarmAdapter.stopRingtone();

        // Finish the MainActivity
        Intent local = new Intent();
        local.putExtra("alarmId", notificationId);
        local.setAction("com.hello.action");
        context.sendBroadcast(local);

        // Cancel the notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }
}
