package com.example.kush_mish.nappy;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.LauncherActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {


    private static ArrayList<Alarm> mAlarms;
    private static Context mContext;
    private AdapterCallback mAdapterCallback;
    private static Alarm alarm;
    private long napTime;



    public interface AdapterCallback {
        void onMethodCallback(Button selectAlarmTone, Uri RingtoneUri, Ringtone ringtone);
        void resetRingtoneCallback();
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener,
            CompoundButton.OnCheckedChangeListener{

        public TextView alarmNameText;
        public Spinner napTimeSelector;
        public CheckBox vibrateCheckBox;
        public Button selectAlarmTone;
        public ToggleButton setAlarmButton;
        public ImageButton deleteAlarmButton;
        final Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(mContext,
                RingtoneManager.TYPE_ALARM);

        public AlarmViewHolder(View itemView) {
            super(itemView);
            alarmNameText = (TextView) itemView.findViewById(R.id.text_alarm_name);
            napTimeSelector = (Spinner) itemView.findViewById(R.id.spinner_nap_time);
            vibrateCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_vibrate);
            selectAlarmTone = (Button) itemView.findViewById(R.id.select_alarm);
            setAlarmButton = (ToggleButton) itemView.findViewById(R.id.button_set_alarm);
            deleteAlarmButton = (ImageButton) itemView.findViewById(R.id.button_delete_alarm);

            /*
             * napTimeSelector spinner gets the nap time as a user input
             */
            final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                    R.array.nap_time, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            napTimeSelector.setAdapter(adapter);
            napTimeSelector.setOnItemSelectedListener(this);


            /*
             * selectAlarmTone button opens up a Ringtone Picker dialog
             *
             * */
            final Ringtone ringtone = RingtoneManager.getRingtone(mContext, defaultRingtoneUri);
            selectAlarmTone.setText(ringtone.getTitle(mContext));

            selectAlarmTone.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    mAdapterCallback.onMethodCallback(selectAlarmTone, defaultRingtoneUri, ringtone);
                }
            });

            vibrateCheckBox.setOnCheckedChangeListener(this);

        }


        /*
         * Nap time input from the napTimeSelector spinner
         */
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

            switch (pos) {
                case 0:
                    // 7 minutes
                    napTime = 1000 * 10;
                    break;
                case 1:
                    // 10 minutes
                    napTime = 1000 * 15;
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

        /*
         * onCheckedChange method is called by setOnCheckedChangeListener to decide vibrate or not
         * with vibrateCheckBox
         */
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                AlarmManagerBroadcastReceiver.vibrate = true;
            } else {
                AlarmManagerBroadcastReceiver.vibrate = false;
            }
        }

    }

    public AlarmAdapter(ArrayList<Alarm> alarms, Context context, AdapterCallback adapterCallback) {
        mAlarms = alarms;
        mContext = context;
        this.mAdapterCallback = adapterCallback;
    }

    @Override
    public AlarmAdapter.AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_alarm, parent, false);
        itemView.findViewById(R.id.select_alarm).requestFocusFromTouch();

        return new AlarmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlarmViewHolder holder, final int position) {
        final Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(mContext,
                RingtoneManager.TYPE_ALARM);

        alarm = mAlarms.get(position);
        holder.alarmNameText.setText(alarm.getmAlarmName());

        // Set selection of the dropdown menu to the first item
        holder.napTimeSelector.setSelection(0);

        // Set vibrateCheckbox to false state
        holder.vibrateCheckBox.setChecked(false);

        holder.setAlarmButton.setChecked(false);
        final Ringtone ringtone = RingtoneManager.getRingtone(mContext, defaultRingtoneUri);
        holder.selectAlarmTone.setText(ringtone.getTitle(mContext));

        holder.deleteAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAlarm(holder.getAdapterPosition(), holder);
            }
        });

        /*
         * The toggleButton selectAlarmTone sends broadcast to AlarmManagerBroadcastReceiver class
         * to start playing ringtone after the specified napTime
         *
         * */
        holder.setAlarmButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    holder.alarmNameText.clearFocus();
                    hideKeyboardFrom(mContext, holder.alarmNameText.getRootView());
                    alarm.setAlarmName(holder.alarmNameText.getText() + "");
                    Intent intent = new Intent(mContext,
                            AlarmManagerBroadcastReceiver.class);
                    intent.putExtra("alarmId", alarm.getmAlarmId());
                    intent.putExtra("alarmName", alarm.getmAlarmName());
                    Log.e("Alarm ID =>", alarm.getmAlarmId() + "");
                    Log.e("Alarm Name =>", alarm.getmAlarmName() + "");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                            alarm.getmAlarmId(), intent, 0);
                    Toast.makeText(mContext, R.string.alarm_on,
                            Toast.LENGTH_SHORT).show();
                    AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + napTime,
                            pendingIntent);
                    mContext.startService(intent);
                    holder.setAlarmButton.setBackground(mContext.getResources().getDrawable(R.drawable.gradient));

                } else {
                    // The toggle is disabled
                    setAlarmOff(holder);
                }
            }
        });

        /*
         * BroadcastReceiver to receive broadcast from the AlarmManagerBroadcastReceiver class
         * sets setAlarmButton to false when Dismiss alarm button is pressed in notification
         */
        BroadcastReceiver receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                holder.setAlarmButton.setChecked(false);
            }
        };

        /* Dismiss alarm button intent which toggles setAlarmButton to OFF*/
        IntentFilter filter = new IntentFilter();

        filter.addAction("com.hello.action");
        mContext.registerReceiver(receiver, filter);

    }

    /*
    *
    * After inputting in the alarmNameText, when the user presses the setAlarmButton the keyboard
    * should hide automatically. Achieved with the following method.
    * */
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setAlarmOff(AlarmViewHolder holder) {
        Intent intent = new Intent(mContext,
                AlarmManagerBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                alarm.getmAlarmId(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        if (AlarmManagerBroadcastReceiver.mRingtone != null)
            stopRingtone();
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(alarm.getmAlarmId());
        holder.setAlarmButton.setBackgroundColor(mContext.getResources().getColor(R.color.primary_material_dark));
    }


    /* Stop ringtone */
    public static void stopRingtone() {
        AlarmManagerBroadcastReceiver.mRingtone.stop();
    }

    /* Delete the alarm */
    private void deleteAlarm(int position, AlarmViewHolder holder) {
        mAlarms.remove(position);
        notifyItemRemoved(position);
        setAlarmOff(holder);
        mAdapterCallback.resetRingtoneCallback();
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }



}
