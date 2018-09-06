package com.example.kush_mish.nappy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;


public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    public static Ringtone mRingtone;
    public static Uri alarmUri;
    public static int ALARM_NOTIFICATION_REQUEST_CODE = 16;
    public static boolean vibrate;


    @Override
    public void onReceive(Context context, Intent intent) {


        showNotification(context, MainActivity.class, "Nappy alarm", "Wake up ! !", vibrate);


//        Toast.makeText(context, "Wake up!!!", Toast.LENGTH_LONG).show();


    }

    public static void showNotification(Context context, Class<?> cls, String title, String content, boolean vibrate) {

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "tag");

        wakeLock.acquire();


        if (alarmUri == null)
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mRingtone = RingtoneManager.getRingtone(context, alarmUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRingtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
        }  else {
            mRingtone.setStreamType(AudioAttributes.USAGE_ALARM);
        }

        mRingtone.play();

        /*
        TODO : Send ringtone title to mainActivity

        */

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, notificationIntent, 0);

        Intent dismissIntent = new Intent(context, ButtonReceiver.class);
        dismissIntent.putExtra("notificationId", ALARM_NOTIFICATION_REQUEST_CODE);

        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, 0,
                dismissIntent, 0);

        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_access_alarm)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(Notification.PRIORITY_MAX)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
//                .setFullScreenIntent(pendingIntent, false)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_stat_alarm_off, "Dismiss Alarm", dismissPendingIntent);

        if (vibrate) {
            Log.e("Vibration", "vibrate on");
            notificationBuilder.setVibrate(new long[]{200, 300, 500, 300, 500, 1000});
        } else {
            Log.e("Vibration", "vibrate off");
        }

        Notification notification = notificationBuilder.build();

        notification.flags = Notification.FLAG_INSISTENT;


        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ALARM_NOTIFICATION_REQUEST_CODE, notification);

        wakeLock.release();
    }


}

