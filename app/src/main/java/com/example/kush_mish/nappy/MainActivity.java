package com.example.kush_mish.nappy;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private static final int RQS_RINGTONEPICKER = 1;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private long napTime;
    boolean buttonState;
    ToggleButton setAlarmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(AlarmManagerBroadcastReceiver.mRingtone != null)
            AlarmManagerBroadcastReceiver.stopRingtone();

        Spinner napTimeSelector = (Spinner) findViewById(R.id.spinner_nap_time);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nap_time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        napTimeSelector.setAdapter(adapter);
        napTimeSelector.setOnItemSelectedListener(this);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        setAlarmButton = (ToggleButton) findViewById(R.id.button_set_alarm);
        setAlarmButton.setChecked(buttonState);
        setAlarmButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Intent intent = new Intent(MainActivity.this,
                            AlarmManagerBroadcastReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            0, intent, 0);
                    Toast.makeText(getApplicationContext(), "Alarm set",
                            Toast.LENGTH_SHORT).show();
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + napTime, pendingIntent);
                    startService(intent);

                } else {
                    // The toggle is disabled
                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(getApplicationContext(), "ALARM OFF",
                            Toast.LENGTH_SHORT).show();
                    if(AlarmManagerBroadcastReceiver.mRingtone != null)
                        AlarmManagerBroadcastReceiver.stopRingtone();
                }
            }
        });

        Button selectAlarm = (Button) findViewById(R.id.select_alarm);
        selectAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notificationIntent = new Intent(getApplicationContext(), AlarmNotification.class);
                startService(notificationIntent);

                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                startActivityForResult(intent, RQS_RINGTONEPICKER);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("buttonState", setAlarmButton.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getBoolean("buttonState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        buttonState = setAlarmButton.isChecked();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAlarmButton.setChecked(buttonState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    AlarmManagerBroadcastReceiver.alarmUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    break;

                default:
                    break;
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

        switch(pos) {
            case 0 :
                // 7 minutes
                napTime = 1000 * 10;
                break;
            case 1 :
                // 10 minutes
                napTime = 1000 * 60 * 10;
                break;
            case 2 :
                // 20 minutes
                napTime = 1000 * 60 * 20;
                break;
            case 3 :
                // 30 minutes
                napTime = 1000 * 60 * 30;
                break;
            case 4 :
                // 45 minutes
                napTime = 1000 * 60 * 45;
                break;
            case 5 :
                // 1 hour
                napTime = 1000 * 60;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        napTime = 10000;
    }

}
