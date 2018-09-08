package com.example.kush_mish.nappy;

import android.app.AlarmManager;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity /*implements AdapterView.OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener*/{

    private ArrayList<Alarm> mAlarms = new ArrayList<>();
    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;

    private static final int REQUEST_RINGTONE_PICKER = 1;
    public static int ALARM_NOTIFICATION_REQUEST_CODE = 16;
    final private String RINGTONE_TITLE = "ringtoneTitle";
    private long napTime;
    public AlarmManager alarmManager;
    public PendingIntent pendingIntent;
    public ToggleButton setAlarmButton;
    public Button selectAlarmTone;
    public CheckBox vibrateCheckBox;
    private String ringToneTitle;

    private void addAlarm(Alarm newAlarm) {
        mAlarms.add(newAlarm);
        alarmAdapter.notifyItemInserted(mAlarms.size() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        alarmAdapter = new AlarmAdapter(mAlarms, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(alarmAdapter);

        /*
         * Add first alarm */
        addAlarm(new Alarm(1, "Nappy Alarm 1"));

        FloatingActionButton fab = findViewById(R.id.fab_add_alarm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int noOfAlarms = mAlarms.size();
                if(noOfAlarms != 5) {
                    int newAlarmId = mAlarms.size() + 1;
                    addAlarm(new Alarm(newAlarmId, "Nappy Alarm " + newAlarmId));
                } else {
                    Snackbar.make(view, "How much do you wanna sleep?", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
//
//        /*
//         * napTimeSelector spinner gets the nap time as a user input
//         */
//        Spinner napTimeSelector = (Spinner) findViewById(R.id.spinner_nap_time);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
//                R.array.nap_time, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        napTimeSelector.setAdapter(adapter);
//        napTimeSelector.setOnItemSelectedListener(this);
//
//        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        setAlarmButton = (ToggleButton) findViewById(R.id.button_set_alarm);
//
//        vibrateCheckBox = (CheckBox) findViewById(R.id.checkbox_vibrate);
//        vibrateCheckBox.setOnCheckedChangeListener(this);
//
//        /*
//         * The toggleButton selectAlarmTone sends broadcast to AlarmManagerBroadcastReceiver class
//         * to start playing ringtone after the specified napTime
//         *
//         * */
//        selectAlarmTone = (Button) findViewById(R.id.select_alarm);
//        setAlarmButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // The toggle is enabled
//                    Intent intent = new Intent(getApplicationContext(),
//                            AlarmManagerBroadcastReceiver.class);
//                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
//                            0, intent, 0);
//                    Toast.makeText(getApplicationContext(), R.string.alarm_on,
//                            Toast.LENGTH_SHORT).show();
//                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                            SystemClock.elapsedRealtime() + napTime, pendingIntent);
//                    startService(intent);
//                    setAlarmButton.setBackground(getApplication().getResources().getDrawable(R.drawable.gradient));
//
//                } else {
//                    // The toggle is disabled
//                    alarmManager.cancel(pendingIntent);
//                    Toast.makeText(getApplicationContext(), R.string.alarm_off,
//                            Toast.LENGTH_SHORT).show();
//                    if (AlarmManagerBroadcastReceiver.mRingtone != null)
//                        stopRingtone();
//                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    manager.cancel(ALARM_NOTIFICATION_REQUEST_CODE);
//                    setAlarmButton.setBackgroundColor(getResources().getColor(R.color.primary_material_dark));
//                }
//            }
//        });
//
//        /*
//         * selectAlarmTone button opens up a Ringtone Picker dialog
//         *
//         * */
//        final Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(),
//                RingtoneManager.TYPE_ALARM);
//        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), defaultRingtoneUri);
//        selectAlarmTone.setText(ringtone.getTitle(getApplicationContext()));
//
//        selectAlarmTone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
//                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, defaultRingtoneUri);
//                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
//                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone");
//                startActivityForResult(intent, REQUEST_RINGTONE_PICKER);
//            }
//        });
//
//        /* Dismiss alarm button intent which toggles setAlarmButton to OFF*/
//        IntentFilter filter = new IntentFilter();
//
//        filter.addAction("com.hello.action");
//        registerReceiver(receiver, filter);
//
//        /* Ringtone name receiver which sets ringtone name on the selectAlarmTone button*/
//        IntentFilter filter1 = new IntentFilter();
//
//        filter.addAction("com.ringtone-name.action");
//        registerReceiver(ringtoneNameReceiver, filter1);
//
//

    }

//    @Override
//    protected void onSaveInstanceState(Bundle saveInstanceState) {
//        ringToneTitle = selectAlarmTone.getText().toString();
//        saveInstanceState.putString(RINGTONE_TITLE, ringToneTitle);
//        super.onSaveInstanceState(saveInstanceState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        ringToneTitle = savedInstanceState.getString(RINGTONE_TITLE);
//        selectAlarmTone.setText(ringToneTitle);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case 1:
//                    AlarmManagerBroadcastReceiver.alarmUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
//                    Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), AlarmManagerBroadcastReceiver.alarmUri);
//                    selectAlarmTone.setText(ringtone.getTitle(getApplicationContext()));
//                    break;
//
//                default:
//                    break;
//            }
//        }
//    }
//
//    /*
//     * BroadcastReceiver to receive broadcast from the AlarmManagerBroadcastReceiver class
//     * sets setAlarmButton to false when Dismiss alarm button is pressed in notification
//     */
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            setAlarmButton.setChecked(false);
//        }
//    };
//
//    /*
//     * Set ringToneTitle from the BroadCast from AlarmManagerBroadcastReceiver class
//     */
//    BroadcastReceiver ringtoneNameReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            ringToneTitle = intent.getExtras().toString();
//            selectAlarmTone.setText(ringToneTitle);
//        }
//    };
//
//    /* Stop ringtone */
//    public static void stopRingtone() {
//        AlarmManagerBroadcastReceiver.mRingtone.stop();
//    }
//
//    /*
//     * onCheckedChange method is called by setOnCheckedChangeListener to decide vibrate or not
//     * with vibrateCheckBox
//     */
//    @Override
//    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//        if (isChecked) {
//            AlarmManagerBroadcastReceiver.vibrate = true;
//        } else {
//            AlarmManagerBroadcastReceiver.vibrate = false;
//        }
//    }
//
//    /*
//     * Nap time input from the napTimeSelector spinner
//     */
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
//
//        switch (pos) {
//            case 0:
//                // 7 minutes
//                napTime = 1000 * 10;
//                break;
//            case 1:
//                // 10 minutes
//                napTime = 1000 * 60 * 10;
//                break;
//            case 2:
//                // 20 minutes
//                napTime = 1000 * 60 * 20;
//                break;
//            case 3:
//                // 30 minutes
//                napTime = 1000 * 60 * 30;
//                break;
//            case 4:
//                // 45 minutes
//                napTime = 1000 * 60 * 45;
//                break;
//            case 5:
//                // 1 hour
//                napTime = 1000 * 60;
//
//        }
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//        napTime = 10000;
//    }
//
}
