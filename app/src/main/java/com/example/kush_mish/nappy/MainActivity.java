package com.example.kush_mish.nappy;

import android.app.Activity;
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
import android.os.Parcelable;
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

public class MainActivity extends AppCompatActivity {

    private ArrayList<Alarm> mAlarms = new ArrayList<>();
    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;


    private static final int REQUEST_RINGTONE_PICKER = 1;
    public Button mSelectAlarmTone;
    public Uri currentRingtoneUri;

    private void addAlarm(Alarm newAlarm) {
        mAlarms.add(newAlarm);
        alarmAdapter.notifyItemInserted(mAlarms.size() - 1);
    }

    AlarmAdapter.AdapterCallback callback = new AlarmAdapter.AdapterCallback() {
        @Override
        public void onMethodCallback(Button selectAlarmTone, Uri defaultRingtoneUri, Ringtone ringtone) {

            mSelectAlarmTone = selectAlarmTone;
            /*
             * selectAlarmTone button opens up a Ringtone Picker dialog
             *
             * */

            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            if (currentRingtoneUri == null)
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, defaultRingtoneUri);
            else intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentRingtoneUri);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone");
            startActivityForResult(intent, REQUEST_RINGTONE_PICKER);
        }

        @Override
        public void resetRingtoneCallback() {
            currentRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(),
                    RingtoneManager.TYPE_ALARM);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        alarmAdapter = new AlarmAdapter(mAlarms, getApplicationContext(), callback);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(alarmAdapter);

        /*
         * Add first alarm */
        addAlarm(new Alarm(1, "NapP Alarm 1"));

        FloatingActionButton fab = findViewById(R.id.fab_add_alarm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int noOfAlarms = mAlarms.size();
                if(noOfAlarms != 5) {
                    int newAlarmId = mAlarms.size() + 1;
                    addAlarm(new Alarm(newAlarmId, "NapP Alarm " + newAlarmId));
                    recyclerView.scrollToPosition(newAlarmId - 1);
                } else {
                    Snackbar.make(view, "How much do you wanna sleep?", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });

//
//        /* Ringtone name receiver which sets ringtone name on the selectAlarmTone button*/
//        IntentFilter filter = new IntentFilter();
//
//        filter.addAction("com.ringtone-name.action");
//        registerReceiver(ringtoneNameReceiver, filter);
//
//

    }

//    @Override
//    protected void onSaveInstanceState(Bundle saveInstanceState) {
//        ringToneTitle = selectAlarmTone.getText().toString();
//        saveInstanceState.putString(RINGTONE_TITLE, ringToneTitle);
//        super.onSaveInstanceState(saveInstanceState);
//    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        ringToneTitle = savedInstanceState.getString(RINGTONE_TITLE);
//        selectAlarmTone.setText(ringToneTitle);
//    }


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
                    mSelectAlarmTone.setText(ringtone.getTitle(getApplicationContext()));
                    currentRingtoneUri = AlarmManagerBroadcastReceiver.alarmUri;
                    break;

                default:
                    break;
            }
        }
    }

//    /*
//     * Set ringToneTitle from the BroadCast from AlarmManagerBroadcastReceiver class
//     */
//    BroadcastReceiver ringtoneNameReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            ringToneTitle = intent.getExtras().toString();
//
//            mSelectAlarmTone.setText(ringToneTitle);
//        }
//    };




}
