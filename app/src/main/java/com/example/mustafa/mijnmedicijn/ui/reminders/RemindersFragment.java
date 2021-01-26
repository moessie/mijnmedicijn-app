package com.example.mustafa.mijnmedicijn.ui.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Recycler.adapters.RemindersListAdapter;
import com.example.mustafa.mijnmedicijn.Room.Models.RemindersModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import static com.example.mustafa.mijnmedicijn.HomeActivity.reminderDB;

public class RemindersFragment extends Fragment {
    private RemindersListAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_reminders, container, false);
        FloatingActionButton addReminderFAB = view.findViewById(R.id.addReminderFAB);
        LinearLayout noRemindersMsg = view.findViewById(R.id.noRemindersMsg);
        addReminderFAB.setOnClickListener(v-> Navigation.findNavController(view).navigate(R.id.action_navigation_reminders_to_addReminderFragment));

        final List<RemindersModel> remindersList = reminderDB.getRemindersDao().getMyReminders();

        if(remindersList.isEmpty()){
            noRemindersMsg.setVisibility(View.VISIBLE); // No reminders found
        }
        else {
            noRemindersMsg.setVisibility(View.GONE);
            setRemindersRV(view,remindersList);
        }
        return view;
    }

    private void setRemindersRV(View view,List<RemindersModel> remindersList) {
        RecyclerView remindersRV = view.findViewById(R.id.remindersRV);
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            remindersRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new RemindersListAdapter(remindersList,navController);
            remindersRV.setAdapter(adapter);

        }
    }


}