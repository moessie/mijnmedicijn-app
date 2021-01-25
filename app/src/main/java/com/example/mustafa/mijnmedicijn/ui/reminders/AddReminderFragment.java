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
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mustafa.mijnmedicijn.DataHelper;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Recycler.SuggestionsAdapter;
import com.example.mustafa.mijnmedicijn.Reminder.ReminderBroadcast;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class AddReminderFragment extends Fragment {

    private SuggestionsAdapter suggestionsAdapter;
    private List<String> suggestionsList = new ArrayList<>();
    private List<String> medicinesList = DataHelper.getTestMedicines(); // TODO : Get this through API
    private String selectedUnit = "pill(s)"; // set a default selectedUnit, currently hardcoded but can be changed to first item of spinner
    private TextInputEditText medicineNameET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        LinearLayout addReminderLayout = view.findViewById(R.id.addReminderLayout);
        addReminderLayout.setOnClickListener(v->{
            showTimePicker(view);
        });
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

    private void showTimePicker(View view){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.time_picker_dialogue);

        Button doneBtn = dialog.findViewById(R.id.doneBtn);
        TimePicker picker = dialog.findViewById(R.id.alarmTimePicker);

        doneBtn.setOnClickListener(v -> {
            //TODO: Save Time Picker Result
            int hour, minute;
            String am_pm;
            if (Build.VERSION.SDK_INT >= 23 ){
                hour = picker.getHour();
                minute = picker.getMinute();
            }
            else{
                hour = picker.getCurrentHour();
                minute = picker.getCurrentMinute();
            }
            if(hour > 12) {
                am_pm = "PM";
                hour = hour - 12;
            }
            else {
                am_pm="AM";
            }
            Toast.makeText(getActivity(),"Selected Time: "+ hour +":"+ minute+" "+am_pm,Toast.LENGTH_LONG).show();
            testFun();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void testFun(){
        if(getActivity()!=null){
            Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
            PendingIntent pendingIntent =  PendingIntent.getBroadcast(getActivity(),0,intent,0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            final long interval = System.currentTimeMillis() + (5000);
            alarmManager.set(AlarmManager.RTC_WAKEUP,interval,pendingIntent);
            Log.d("alarmLogs->","Here");
        }
        else {
            Log.d("alarmLogs->","Fun Failed");
        }
    }
}