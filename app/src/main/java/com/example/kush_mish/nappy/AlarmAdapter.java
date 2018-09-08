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
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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
    private Context context;
    private int lastPosition = -1;

    private static final int RQS_RINGTONEPICKER = 1;
    public static int ALARM_NOTIFICATION_REQUEST_CODE = 16;
    final private String RINGTONE_TITLE = "ringtoneTitle";
    public AlarmManager alarmManager;
    public PendingIntent pendingIntent;

    private long napTime;
    private String ringToneTitle;



    public class AlarmViewHolder extends RecyclerView.ViewHolder{

        public TextView alarmNameText;
        public Spinner napTimeSelector;
        public CheckBox vibrateCheckBox;
        public Button selectAlarmTone;
        public ToggleButton setAlarmButton;
        public ImageButton deleteAlarmButton;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            alarmNameText = (TextView) itemView.findViewById(R.id.text_alarm_name);
            napTimeSelector = (Spinner) itemView.findViewById(R.id.spinner_nap_time);
            vibrateCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox_vibrate);
            selectAlarmTone = (Button) itemView.findViewById(R.id.select_alarm);
            setAlarmButton = (ToggleButton) itemView.findViewById(R.id.button_set_alarm);
            deleteAlarmButton = (ImageButton) itemView.findViewById(R.id.button_delete_alarm);

            }
    }

    public AlarmAdapter(ArrayList<Alarm> alarms, Context context) {
        mAlarms = alarms;
        this.context = context;
    }

    @Override
    public AlarmAdapter.AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_alarm, parent, false);
        return new AlarmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlarmViewHolder holder, final int position) {

        final Alarm alarm = mAlarms.get(position);
        holder.alarmNameText.setText(alarm.getmAlarmName());

        holder.deleteAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAlarm(holder.getAdapterPosition());
            }
        });

    }

    private void deleteAlarm(int position) {
        mAlarms.remove(position);
        notifyItemRemoved(position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

}
