package com.example.mustafa.mijnmedicijn.ui.reminders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Room.RemindersData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import static com.example.mustafa.mijnmedicijn.HomeActivity.reminderDB;

public class RemindersFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_reminders, container, false);
        FloatingActionButton addReminderFAB = view.findViewById(R.id.addReminderFAB);
        addReminderFAB.setOnClickListener(v-> Navigation.findNavController(view).navigate(R.id.action_navigation_reminders_to_addReminderFragment));

        List<RemindersData> myReminders = reminderDB.getRemindersDao().getMyReminders();

        if(myReminders.isEmpty()){
            Log.d("roomLogs->","Geen Herinneringen toegevoegd");
        }
        else {
            Log.d("roomLogs->"," "+myReminders.size()+" Herinneringen.");
        }

        return view;
    }

}