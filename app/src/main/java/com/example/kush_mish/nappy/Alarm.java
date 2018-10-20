package com.example.kush_mish.nappy;

import android.support.v7.widget.RecyclerView;

public class Alarm{
    private int mAlarmId;
    private String mAlarmName;

    public Alarm(int alarmId, String alarmName) {
        mAlarmId = alarmId;
        mAlarmName = alarmName;
    }

    public int getmAlarmId() {
        return mAlarmId;
    }

    public String getmAlarmName() {
        return mAlarmName;
    }

    public void setAlarmName(String alarmName) {
        mAlarmName = alarmName;
    }
}
