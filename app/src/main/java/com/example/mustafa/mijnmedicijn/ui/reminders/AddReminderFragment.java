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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mustafa.mijnmedicijn.DataHelper;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Recycler.SuggestionsAdapter;
import com.example.mustafa.mijnmedicijn.Reminder.ReminderBroadcast;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class AddReminderFragment extends Fragment {

    private SuggestionsAdapter suggestionsAdapter;
    private ScrollView addReminderFrag;
    private final Calendar reminderTime = Calendar.getInstance();
    private final List<String> suggestionsList = new ArrayList<>();
    private final List<String> medicinesList = DataHelper.getTestMedicines(); // TODO : Get this through API
    private String selectedUnit = "pill(s)"; // set a default selectedUnit, currently hardcoded but can be changed to first item of spinner
    private int frequency = 1; // 0 = everyday, 1 = X alternate days , 2 = Selected WeekDays
    private TextInputEditText medicineNameET, medicineQuantityET;
    private LinearLayout repeatAfterXDays, daysOfWeek;
    private RelativeLayout addReminderLayout;
    private EditText repeatDaysET;
    private TextView ReminderTimeTV;
    private CheckBox mondayCB, tuesdayCB, wednesdayCB, thursdayCB, fridayCB, saturdayCB, sundayCB;
    private boolean timeSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        findViews(view);
        setUpSpinners(view);
        initializeSuggestionsRV(view);
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
        addReminderLayout.setOnClickListener(v -> showTimePicker());
        ExtendedFloatingActionButton saveReminderFAB = view.findViewById(R.id.saveReminderFAB);
        saveReminderFAB.setOnClickListener(v->setReminder());
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
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
            final String selectedTime = hourStr + ":" + minStr + " " + am_pm;
            ReminderTimeTV.setText(selectedTime);
            timeSelected = true;
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setReminder() {
        if (Objects.requireNonNull(medicineNameET.getText()).toString().isEmpty()) {
            makeSnack("Vul medicijn naam in");
            return;
        }
        final String quantity = Objects.requireNonNull(medicineQuantityET.getText()).toString();
        if (quantity.isEmpty() || quantity.equals("0")) {
            makeSnack("Vul hoeveelheid in");
            return;
        }
        if (!timeSelected) {
            makeSnack("De tijd is niet ingesteld");
            return;
        }
        if (getActivity() != null) {
            final int _id = (int) System.currentTimeMillis(); // Alarm ID, to be used for cancellation
            Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), _id, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            switch (frequency) {
                case 0: // Everyday
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    makeToast("Reminder set.");
                    saveReminderInRoom();
                    break;
                case 1:
                    String repeatDays = repeatDaysET.getText().toString();
                    if (repeatDays.isEmpty() || repeatDays.equals("0")) {
                        makeSnack("Repeat days should be greater than 0");
                        return;
                    }
                    int multiplier = Integer.parseInt(repeatDays);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), (AlarmManager.INTERVAL_DAY * multiplier), pendingIntent);
                    makeToast("Je krijgt een herinnering elke " + repeatDays + " dagen");
                    saveReminderInRoom();
                    break;
                case 2:
                    String msg = "Herinnering voor ";
                    boolean selection = false;
                    if (mondayCB.isChecked()) {
                        setWeeklyReminder(2); msg = msg + " Maandag,"; selection = true;
                    } if (tuesdayCB.isChecked()) {
                    setWeeklyReminder(3); msg = msg + " Dinsdag,";selection = true;
                } if (wednesdayCB.isChecked()) {
                    setWeeklyReminder(4); msg = msg + " Woensdag,";selection = true;
                } if (thursdayCB.isChecked()) {
                    setWeeklyReminder(5); msg = msg + " Donderdag,";selection = true;
                } if (fridayCB.isChecked()) {
                    setWeeklyReminder(6); msg = msg + " Vrijdag,";selection = true;
                } if (saturdayCB.isChecked()) {
                    setWeeklyReminder(7); msg = msg + " Zaterdag,";selection = true;
                } if (sundayCB.isChecked()) {
                    setWeeklyReminder(1); msg = msg + " Zondag,";selection = true;
                }
                    if(!selection){makeSnack("Selecteer tenminste 1 dag");}
                    else {makeToast(msg);saveReminderInRoom();}
                    break;
            }
        } else {
            Log.d("alarmLogs->", "Fun Failed");
        }
    }

    private void setWeeklyReminder(int DayOfWeek) {
//        if (getActivity() != null) {
//            final int _id = (int) System.currentTimeMillis(); // Alarm ID, to be used for cancellation
//            reminderTime.set(Calendar.DAY_OF_WEEK, DayOfWeek);
//            Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), _id, intent, 0);
//            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        }
    }
    private void saveReminderInRoom(){
        // TODO :
    }
    private void makeSnack(String msg) {
        Snackbar.make(addReminderFrag, msg, Snackbar.LENGTH_LONG).show();
    }

    private void makeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}