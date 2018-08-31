package com.example.kush_mish.nappy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.widget.Toast;



public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    public static Ringtone mRingtone;
    public static Uri alarmUri;
    public static int ALARM_NOTIFICATION_REQUEST_CODE = 16;


    @Override
    public void onReceive(Context context, Intent intent) {

        showNotification(context, MainActivity.class, "Nappy alarm", "Wake up ! !");

//        Toast.makeText(context, "Wake up!!!", Toast.LENGTH_LONG).show();



    }

    public static void showNotification(Context context,Class<?> cls,String title,String content) {

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "tag?");

        wakeLock.acquire();


        if(alarmUri == null)
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mRingtone = RingtoneManager.getRingtone(context, alarmUri);

        mRingtone.play();

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(ALARM_NOTIFICATION_REQUEST_CODE,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent dismissIntent = new Intent(context, ButtonReceiver.class);
        dismissIntent.putExtra("notificationId", ALARM_NOTIFICATION_REQUEST_CODE);

        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, 0,
                dismissIntent, 0);

        Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_stat_access_alarm)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(Notification.PRIORITY_MAX)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setFullScreenIntent(pendingIntent, true)
                    .setAutoCancel(true)
                .addAction(R.drawable.ic_stat_access_alarm, "Dismiss Alarm", dismissPendingIntent)
                    .build();



        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ALARM_NOTIFICATION_REQUEST_CODE, notification);

        wakeLock.release();
    }


    public static void stopRingtone() {
        mRingtone.stop();
    }


}

