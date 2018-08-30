package com.example.kush_mish.nappy;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmNotification extends IntentService {

    public final int alarmNotificationId = 16;
    public AlarmNotification(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        createNotification();
    }


    private void createNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_access_alarm)
                .setContentTitle("Nappy alarm")
                .setContentText("Wake up!!")
                .setPriority(Notification.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(alarmNotificationId, notification);

    }
}
