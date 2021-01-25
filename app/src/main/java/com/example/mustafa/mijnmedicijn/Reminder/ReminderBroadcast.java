package com.example.mustafa.mijnmedicijn.Reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("alarmLogs->","BrooooooooDcast onReceive()");
    }
}
