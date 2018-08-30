package com.example.kush_mish.nappy;

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

    @Override
    public void onReceive(Context context, Intent intent) {


        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "tag?");

        wakeLock.acquire();


        if(alarmUri == null)
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mRingtone = RingtoneManager.getRingtone(context, alarmUri);

        mRingtone.play();



        Toast.makeText(context, "Wake up!!!", Toast.LENGTH_LONG).show();


        wakeLock.release();
    }

    public static void showNotification(Context context,Class<?> cls,String title,String content) {

    }


    public static void stopRingtone() {
        mRingtone.stop();
    }


}

