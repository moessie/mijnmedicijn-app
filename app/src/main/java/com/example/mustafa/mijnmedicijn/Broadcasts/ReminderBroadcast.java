package com.example.mustafa.mijnmedicijn.Broadcasts;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mustafa.mijnmedicijn.MedicineTakenActivity;
import com.example.mustafa.mijnmedicijn.R;

import static com.example.mustafa.mijnmedicijn.App.CHANNEL_1_ID;


public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("broadcastLogs->", "Reminder time");
        final String medicineName = intent.getStringExtra("medicineName");
        final String dosage = intent.getStringExtra("dosage");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification;

        // Create an Intent for the activity you want to start when notification is clicked
        Intent onClickIntent = new Intent(context, MedicineTakenActivity.class);
        onClickIntent.putExtra("medicineName",medicineName);
        onClickIntent.putExtra("dosage",dosage);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(onClickIntent);
        PendingIntent onClickPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        if (medicineName != null && dosage != null) {
            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
            contentView.setTextViewText(R.id.medicineNameTV, medicineName);
            contentView.setTextViewText(R.id.medicineQuantityTV, dosage);

            notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.notif_ico_small)
                    .setContent(contentView)
                    .setAutoCancel(true)
                    .setContentIntent(onClickPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
            notificationManager.notify(1, notification);
        }
    }
}
