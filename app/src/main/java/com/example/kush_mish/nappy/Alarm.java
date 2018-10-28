package com.example.kush_mish.nappy;

import android.app.AlarmManager;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.io.Serializable;

public class Alarm {
    private int mAlarmId;
    private String mAlarmName;
    private Uri mRingtoneUri;
    private Button mButton;
    private ToggleButton mSetButton;
    public boolean isOn;
    private Spinner mSpinner;
    private Ringtone ringtone;

    public Alarm(int alarmId, String alarmName) {
        mAlarmId = alarmId;
        mAlarmName = alarmName;
        isOn = false;
    }

    public int getAlarmId() {
        return mAlarmId;
    }

    public String getAlarmName() {
        return mAlarmName;
    }

    public Uri getRingtoneUri() {
        return mRingtoneUri;
    }

    public Button getButton() {
        return mButton;
    }

    public ToggleButton getSetButton() {
        return mSetButton;
    }


    public void setRingtoneUri(Uri ringtoneUri) {
        mRingtoneUri = ringtoneUri;
    }

    public void setButton(Button button) {
        mButton = button;
    }

    public void setSetButton(ToggleButton setButton) {
        mSetButton = setButton;
    }


    public void setAlarmName(String alarmName) {
        mAlarmName = alarmName;
    }

    public long getNapTime() {
        int napTime = 10000, pos = mSpinner.getSelectedItemPosition();
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
        return napTime;
    }

    public void setSpinner(Spinner spinner) {
        mSpinner = spinner;
    }

    public Ringtone getRingtone() {
        return ringtone;
    }

    public void setRingtone(Ringtone ringtone) {
        this.ringtone = ringtone;
    }
}
