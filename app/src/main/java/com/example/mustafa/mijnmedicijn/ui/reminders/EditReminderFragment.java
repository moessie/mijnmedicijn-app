package com.example.mustafa.mijnmedicijn.ui.reminders;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mustafa.mijnmedicijn.DataHelper;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Room.Models.RemindersModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import static com.example.mustafa.mijnmedicijn.HomeActivity.reminderDB;

public class EditReminderFragment extends Fragment {

    private static final String ARG_PARAM1 = "reminderID";
    private static final String ARG_PARAM2 = "frequency";
    private int reminderID;
    private int frequency = 0; // 0 = everyday, 1 = X alternate days , 2 = Selected WeekDays
    /////////////////////////////////////////////////////////
    private ScrollView addReminderFrag;
    private final Calendar reminderTime = Calendar.getInstance();
    private String selectedUnit = "pill(s)"; // set a default selectedUnit, currently hardcoded but can be changed to first item of spinner
    private String alarmTime = "";
    private TextInputEditText medicineNameET, medicineQuantityET;
    private LinearLayout repeatAfterXDays, daysOfWeek;
    private RelativeLayout addReminderLayout;
    private EditText repeatDaysET;
    private TextView ReminderTimeTV;
    private CheckBox mondayCB, tuesdayCB, wednesdayCB, thursdayCB, fridayCB, saturdayCB, sundayCB;
    private boolean timeSelected = false;

    public EditReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reminderID = getArguments().getInt(ARG_PARAM1,0);
            frequency = getArguments().getInt(ARG_PARAM2,0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_edit_reminder, container, false);
        if(reminderID==0){ Navigation.findNavController(view).popBackStack(); }
        else {
            final RemindersModel reminder = reminderDB.getRemindersDao().loadSingle(reminderID);
            if(reminder!=null){
                findViews(view);
                setUpSpinners(view,reminder);
                medicineNameET.setText(reminder.getMedicineName());
                ReminderTimeTV.setText(reminder.getReminderTime());
                medicineQuantityET.setText(reminder.getDoseQuantity());
                //TODO : Set reminderTimeInstance
            }
            else {
                Log.d("roomLogs->","Single Reminder null");
            }
        }
        return view;
    }

    private void findViews(View view) {
        addReminderLayout = view.findViewById(R.id.addReminderLayout);
        addReminderFrag = view.findViewById(R.id.addReminderFrag);
        daysOfWeek = view.findViewById(R.id.daysOfWeek);
        repeatDaysET = view.findViewById(R.id.repeatDays);
        ReminderTimeTV = view.findViewById(R.id.ReminderTimeTV);
        repeatAfterXDays = view.findViewById(R.id.repeatAfterXDays);
        medicineNameET = view.findViewById(R.id.medicineNameET);
        medicineQuantityET = view.findViewById(R.id.medicineQuantityET);
        mondayCB = view.findViewById(R.id.mondayCB);
        tuesdayCB = view.findViewById(R.id.tuesdayCB);
        wednesdayCB = view.findViewById(R.id.wednesdayCB);
        thursdayCB = view.findViewById(R.id.thursdayCB);
        fridayCB = view.findViewById(R.id.fridayCB);
        saturdayCB = view.findViewById(R.id.saturdayCB);
        sundayCB = view.findViewById(R.id.sundayCB);
        ExtendedFloatingActionButton saveReminderFAB = view.findViewById(R.id.saveReminderFAB);
        addReminderLayout.setOnClickListener(v -> showTimePicker());
//        saveReminderFAB.setOnClickListener(v->setReminder());
    }

    private void setUpSpinners(View view,RemindersModel reminder) {
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

        intakeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedPos, long l) {
                frequency = selectedPos;
                switch (frequency) {
                    case 0:
                        repeatAfterXDays.setVisibility(View.GONE);
                        daysOfWeek.setVisibility(View.GONE);
                        break;
                    case 1:
                        repeatAfterXDays.setVisibility(View.VISIBLE);
                        daysOfWeek.setVisibility(View.GONE);
                        break;
                    case 2:
                        repeatAfterXDays.setVisibility(View.GONE);
                        daysOfWeek.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        selectedUnit = reminder.getDoseUnit();
        int selection = DataHelper.getUnits().indexOf(selectedUnit);
        if(selection>=0 && selection<DataHelper.getUnits().size()){
            unitSpinner.setSelection(selection);
        }
        intakeSpinner.setSelection(frequency);
    }


    private void showTimePicker() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.time_picker_dialogue);

        Button doneBtn = dialog.findViewById(R.id.doneBtn);
        TimePicker picker = dialog.findViewById(R.id.alarmTimePicker);

        doneBtn.setOnClickListener(v -> {
            int hour, minute;
            String am_pm, hourStr, minStr;
            if (Build.VERSION.SDK_INT >= 23) {
                hour = picker.getHour();
                minute = picker.getMinute();
            } else {
                hour = picker.getCurrentHour();
                minute = picker.getCurrentMinute();
            }

            //reminderTime.set(Calendar.DAY_OF_WEEK, dayOfWeek);
            reminderTime.set(Calendar.HOUR_OF_DAY, hour);
            reminderTime.set(Calendar.MINUTE, minute);
            reminderTime.set(Calendar.SECOND, 0);

            if (hour >= 12) {
                am_pm = "PM";
                hour = hour - 12;
            } else {
                am_pm = "AM";
            }
            if (hour == 0) {
                hourStr = "12";
            } else {
                hourStr = String.valueOf(hour);
            }
            minStr = String.valueOf(minute);
            if (minStr.length() == 1) {
                minStr = "0" + minStr;
            }
            alarmTime = hourStr + ":" + minStr + " " + am_pm;
            ReminderTimeTV.setText(alarmTime);
            timeSelected = true;
            dialog.dismiss();
        });

        dialog.show();
    }
}