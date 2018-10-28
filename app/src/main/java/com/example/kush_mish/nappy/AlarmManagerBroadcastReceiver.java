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

import java.io.Serializable;


public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    public static Ringtone mRingtone;
    public static Uri alarmUri;
    public static boolean vibrate;
    private static int alarmId;
    private static String alarmName;
    private static Alarm mAlarm;



    @Override
    public void onReceive(Context context, Intent intent) {

        alarmName = intent.getStringExtra("alarmName");
        alarmId = intent.getIntExtra("alarmId", 1);
        alarmUri = Uri.parse(intent.getStringExtra("alarmUri"));
        showNotification(context, MainActivity.class, alarmName, "Wake up ! !", vibrate);
    }

    public  void showNotification(Context context, Class<?> cls, String title, String content, boolean vibrate) {

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "tag:");

        wakeLock.acquire();

        mRingtone = RingtoneManager.getRingtone(context, alarmUri);
        AlarmAdapter.setRingtone(mRingtone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRingtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
        }  else {
            mRingtone.setStreamType(AudioAttributes.USAGE_ALARM);
        }

        mRingtone.play();


        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, alarmId, notificationIntent, 0);
//        Log.e("Alarm ID: ", alarmId + "");
//        Log.e("Alarm Name: ", alarmName + "");

        Intent dismissIntent = new Intent(context, ButtonReceiver.class);
        dismissIntent.putExtra("notificationId", alarmId);

        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, alarmId,
                dismissIntent, 0);

        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_access_alarm)
                .setContentTitle(title)
                .setContentText(mRingtone.getTitle(context))
                .setPriority(Notification.PRIORITY_MAX)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, false)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_stat_alarm_off, "Dismiss Alarm", dismissPendingIntent);

        if (vibrate) {
            Log.e("Vibration", "vibrate on");
            notificationBuilder.setVibrate(new long[]{0, 1000, 1000, 1000, 1000, 0});
        } else {
            Log.e("Vibration", "vibrate off");
        }

        Notification notification = notificationBuilder.build();

        notification.flags = Notification.FLAG_INSISTENT;


        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(alarmId, notification);

        wakeLock.release();
    }


}

