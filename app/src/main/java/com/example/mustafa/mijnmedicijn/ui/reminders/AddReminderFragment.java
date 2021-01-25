package com.example.mustafa.mijnmedicijn.ui.reminders;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mustafa.mijnmedicijn.DataHelper;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Recycler.SuggestionsAdapter;
import com.example.mustafa.mijnmedicijn.Reminder.ReminderBroadcast;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AddReminderFragment extends Fragment {

    private SuggestionsAdapter suggestionsAdapter;
    private final Calendar reminderTime = Calendar.getInstance();
    private final List<String> suggestionsList = new ArrayList<>();
    private final List<String> medicinesList = DataHelper.getTestMedicines(); // TODO : Get this through API
    private String selectedUnit = "pill(s)"; // set a default selectedUnit, currently hardcoded but can be changed to first item of spinner
    private TextInputEditText medicineNameET;
    private TextView ReminderTimeTV;
    private boolean timeSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        RelativeLayout addReminderLayout = view.findViewById(R.id.addReminderLayout);
        addReminderLayout.setOnClickListener(v->{
            showTimePicker();
        });
        ReminderTimeTV = view.findViewById(R.id.ReminderTimeTV);
        medicineNameET = view.findViewById(R.id.medicineNameET);
        setUpSpinners(view);
        initializeSuggestionsRV(view);
        return view;
    }
    private void initializeSuggestionsRV(View view) {
        RecyclerView suggestionRV = view.findViewById(R.id.searchSuggestionsRV);
        suggestionsAdapter = new SuggestionsAdapter(suggestionsList, medicineNameET);
        suggestionRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        suggestionRV.setAdapter(suggestionsAdapter);

        medicineNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i0, int i1, int i2) {
                final String text = charSequence.toString().toLowerCase();
                suggestionsList.clear();
                suggestionsAdapter.notifyDataSetChanged();
                if (!text.isEmpty()) {
                    for (int i = 0; i < medicinesList.size(); i++) { // TODO : Get medicinesList via API
                        if (medicinesList.get(i).contains(text)) {
                            suggestionsList.add(medicinesList.get(i));
                        }
                    }
                }
                suggestionsAdapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void setUpSpinners(View view) {
        final Spinner unitSpinner = view.findViewById(R.id.unitSpinner);
        final Spinner intakeSpinner = view.findViewById(R.id.intakeSpinner);

        ArrayAdapter unitAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, DataHelper.getUnits());
        ArrayAdapter intakeAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, DataHelper.getIntakeFrequency());

        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intakeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unitSpinner.setAdapter(unitAdapter);
        intakeSpinner.setAdapter(intakeAdapter);

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedUnit = DataHelper.getUnits().get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void showTimePicker(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.time_picker_dialogue);

        Button doneBtn = dialog.findViewById(R.id.doneBtn);
        TimePicker picker = dialog.findViewById(R.id.alarmTimePicker);

        doneBtn.setOnClickListener(v -> {
            int hour, minute;
            String am_pm,hourStr="",minStr="0";
            if (Build.VERSION.SDK_INT >= 23 ){
                hour = picker.getHour();
                minute = picker.getMinute();
            }
            else{
                hour = picker.getCurrentHour();
                minute = picker.getCurrentMinute();
            }

            //reminderTime.set(Calendar.DAY_OF_WEEK, dayOfWeek);
            reminderTime.set(Calendar.HOUR_OF_DAY, hour);
            reminderTime.set(Calendar.MINUTE, minute);
            reminderTime.set(Calendar.SECOND, 0);

            if(hour >= 12) {
                am_pm = "PM";
                hour = hour - 12;
            }
            else { am_pm="AM"; }
            if(hour==0){ hourStr="12"; }
            else { hourStr = String.valueOf(hour); }
            minStr = String.valueOf(minute);
            if(minStr.length()==1){minStr = "0"+minStr;}
            final String selectedTime = hourStr +":"+ minStr+" "+am_pm;
            ReminderTimeTV.setText(selectedTime);
            timeSelected = true;
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setReminder(){
        if(getActivity()!=null && timeSelected){
            Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
            PendingIntent pendingIntent =  PendingIntent.getBroadcast(getActivity(),0,intent,0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,reminderTime.getTimeInMillis(),pendingIntent); // TODO : alarmManager.setInexactRepeating()
        }
        else {
            Log.d("alarmLogs->","Fun Failed");
        }
    }
}