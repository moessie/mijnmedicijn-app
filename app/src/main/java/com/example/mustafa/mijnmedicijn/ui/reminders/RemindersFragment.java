package com.example.mustafa.mijnmedicijn.ui.reminders;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mustafa.mijnmedicijn.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.mustafa.mijnmedicijn.App.CHANNEL_1_ID;

public class RemindersFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_reminders, container, false);
        FloatingActionButton addReminderFAB = view.findViewById(R.id.addReminderFAB);
        addReminderFAB.setOnClickListener(v-> Navigation.findNavController(view).navigate(R.id.action_navigation_reminders_to_addReminderFragment));

        return view;
    }

//    private void showNotification() {
//        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(getActivity());
//        Notification notification = new NotificationCompat.Builder(getActivity(), CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.email_small)
//                .setContentTitle("Reminder")
//                .setContentText("Take your medication") // TODO: Get this from DB
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .build();
//        notificationManager.notify(1, notification);
//    }
}