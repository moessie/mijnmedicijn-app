package com.example.mustafa.mijnmedicijn.Broadcasts;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mustafa.mijnmedicijn.R;

import static com.example.mustafa.mijnmedicijn.App.CHANNEL_1_ID;


public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("alarmLogs->","BrooooooooDcast onReceive()");
        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.email_small)
                .setContentTitle("Herinnering")
                .setContentText("Neem je medicatie") // TODO: Get this from DB
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }
}
