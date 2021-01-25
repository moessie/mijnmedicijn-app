package com.example.mustafa.mijnmedicijn.ui.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mustafa.mijnmedicijn.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RemindersFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_reminders, container, false);
        FloatingActionButton addReminderFAB = view.findViewById(R.id.addReminderFAB);
        addReminderFAB.setOnClickListener(v-> Navigation.findNavController(view).navigate(R.id.action_navigation_reminders_to_addReminderFragment));


        return view;
    }
}