package com.example.kush_mish.nappy;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,CompoundButton.OnCheckedChangeListener{

    private static final int RQS_RINGTONEPICKER = 1;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private long napTime;
    public static int ALARM_NOTIFICATION_REQUEST_CODE = 16;
    ToggleButton setAlarmButton;
    Button selectAlarmTone;
    CheckBox vibrateCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner napTimeSelector = (Spinner) findViewById(R.id.spinner_nap_time);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nap_time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        napTimeSelector.setAdapter(adapter);
        napTimeSelector.setOnItemSelectedListener(this);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        setAlarmButton = (ToggleButton) findViewById(R.id.button_set_alarm);

        vibrateCheckBox = (CheckBox) findViewById(R.id.checkbox_vibrate);
        vibrateCheckBox.setOnCheckedChangeListener(this);

        setAlarmButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Intent intent = new Intent(MainActivity.this,
                            AlarmManagerBroadcastReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            0, intent, 0);
                    Toast.makeText(getApplicationContext(), R.string.alarm_on,
                            Toast.LENGTH_SHORT).show();
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + napTime, pendingIntent);
                    startService(intent);

                } else {
                    // The toggle is disabled
                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(getApplicationContext(), R.string.alarm_off,
                            Toast.LENGTH_SHORT).show();
                    if (AlarmManagerBroadcastReceiver.mRingtone != null)
                        stopRingtone();
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(ALARM_NOTIFICATION_REQUEST_CODE);
                }
            }
        });


        selectAlarmTone = (Button) findViewById(R.id.select_alarm);
        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), defaultRingtoneUri);
        selectAlarmTone.setText(ringtone.getTitle(getApplicationContext()));
        selectAlarmTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, AlarmManagerBroadcastReceiver.alarmUri);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, AlarmManagerBroadcastReceiver.alarmUri);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone");
                startActivityForResult(intent, RQS_RINGTONEPICKER);
            }
        });

        IntentFilter filter = new IntentFilter();

        filter.addAction("com.hello.action");
        registerReceiver(receiver, filter);

        IntentFilter filter1 = new IntentFilter();

        filter.addAction("com.ringtone-name.action");
        registerReceiver(ringtoneNameReceiver, filter1);

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            setAlarmButton.setChecked(false);
        }
    };

    BroadcastReceiver ringtoneNameReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String ringToneTitle = intent.getExtras().toString();
            selectAlarmTone.setText(ringToneTitle);
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked) {
            AlarmManagerBroadcastReceiver.vibrate = true;
        } else {
            AlarmManagerBroadcastReceiver.vibrate = false;
        }
    }

    public static void stopRingtone() {
        AlarmManagerBroadcastReceiver.mRingtone.stop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean("buttonState", setAlarmButton.isChecked());
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    AlarmManagerBroadcastReceiver.alarmUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), AlarmManagerBroadcastReceiver.alarmUri);
                    selectAlarmTone.setText(ringtone.getTitle(getApplicationContext()));
                    break;

                default:
                    break;
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

        switch (pos) {
            case 0:
                // 7 minutes
                napTime = 1000 * 10;
                break;
            case 1:
                // 10 minutes
                napTime = 1000 * 60 * 10;
                break;
            case 2:
                // 20 minutes
                napTime = 1000 * 60 * 20;
                break;
            case 3:
                // 30 minutes
                napTime = 1000 * 60 * 30;
                break;
            case 4:
                // 45 minutes
                napTime = 1000 * 60 * 45;
                break;
            case 5:
                // 1 hour
                napTime = 1000 * 60;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        napTime = 10000;
    }

}
