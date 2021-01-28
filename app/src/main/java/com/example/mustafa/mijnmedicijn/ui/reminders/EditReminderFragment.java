package com.example.mustafa.mijnmedicijn.ui.reminders;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mustafa.mijnmedicijn.Broadcasts.ReminderBroadcast;
import com.example.mustafa.mijnmedicijn.DataHelper;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Retrofit.RetrofitClientInstance;
import com.example.mustafa.mijnmedicijn.Retrofit.models.reminders.RemindersBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.reminders.RemindersResponse;
import com.example.mustafa.mijnmedicijn.Room.Models.RemindersModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.mijnmedicijn.HomeActivity.reminderDB;

public class EditReminderFragment extends Fragment {

    private static final String ARG_PARAM1 = "reminderID";
    private static final String ARG_PARAM2 = "frequency";
    private int reminderID;
    private int frequency = 0; // 0 = everyday, 1 = X alternate days , 2 = Selected WeekDays
    /////////////////////////////////////////////////////////
    private FrameLayout editReminderLayout;
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
    private NavHostFragment navHostFragment;
    private NavController navController;
    private SharedPreferences prefs;
    private RetrofitClientInstance.RetroInterFace service;

    public EditReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reminderID = getArguments().getInt(ARG_PARAM1, 0);
            frequency = getArguments().getInt(ARG_PARAM2, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_edit_reminder, container, false);
        prefs = requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.RetroInterFace.class);
        if (reminderID == 0) {
            Navigation.findNavController(view).popBackStack();
        } else {
            final RemindersModel reminder = reminderDB.getRemindersDao().loadSingle(reminderID);
            if (reminder != null) {
                findViews(view);
                setUpSpinners(view, reminder);
                medicineNameET.setText(reminder.getMedicineName());
                ReminderTimeTV.setText(reminder.getReminderTime());
                medicineQuantityET.setText(reminder.getDoseQuantity());
                final ExtendedFloatingActionButton editReminderFAB = view.findViewById(R.id.editReminderFAB);
                final ExtendedFloatingActionButton deleteReminderFAB = view.findViewById(R.id.deleteReminderFAB);
                editReminderFAB.setOnClickListener(v -> setReminder(reminder));
                deleteReminderFAB.setOnClickListener(v -> deleteReminder(reminder));
            } else {
                Toast.makeText(getActivity(), "Failed to retrieve reminder info.", Toast.LENGTH_LONG).show();
                Navigation.findNavController(view).popBackStack();
            }
        }
        return view;
    }

    private void findViews(View view) {
        navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        addReminderLayout = view.findViewById(R.id.addReminderLayout);
        editReminderLayout = view.findViewById(R.id.editReminderLayout);
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
    }

    private void setUpSpinners(View view, RemindersModel reminder) {
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
        if (selection >= 0 && selection < DataHelper.getUnits().size()) {
            unitSpinner.setSelection(selection);
        }
        intakeSpinner.setSelection(frequency);
        String repeatInfo = reminder.getReminderRepeatInfo();
        if (frequency == 1) { // set repeat after x days state
            repeatInfo = repeatInfo.replace(" ", "");
            repeatInfo = repeatInfo.replace("Repeatafterevery", "");
            repeatInfo = repeatInfo.replace("days", ""); // now repeatInfo will only have the number of repeat days
            repeatDaysET.setText(repeatInfo.trim());
        } else if (frequency == 2) { // set weekdays check boxes
            if (repeatInfo.contains("Monday")) {
                mondayCB.setChecked(true);
            }
            if (repeatInfo.contains("Tuesday")) {
                tuesdayCB.setChecked(true);
            }
            if (repeatInfo.contains("Wednesday")) {
                wednesdayCB.setChecked(true);
            }
            if (repeatInfo.contains("Thursday")) {
                thursdayCB.setChecked(true);
            }
            if (repeatInfo.contains("Friday")) {
                fridayCB.setChecked(true);
            }
            if (repeatInfo.contains("Saturday")) {
                saturdayCB.setChecked(true);
            }
            if (repeatInfo.contains("Sunday")) {
                sundayCB.setChecked(true);
            }
        }
    }

    private void setReminder(RemindersModel reminder) {
        if (Objects.requireNonNull(medicineNameET.getText()).toString().isEmpty()) {
            makeSnack("Vul medicijn naam in");
            return;
        }
        final String quantity = Objects.requireNonNull(medicineQuantityET.getText()).toString();
        if (quantity.isEmpty() || quantity.equals("0")) {
            makeSnack("Vu hoeveelheid in");
            return;
        }
        if (!timeSelected) {
            makeSnack("Tijd is niet ingesteld");
            return;
        }
        if (getActivity() != null) {
            final int _id = reminder.get_id(); // Alarm ID, to be used for cancellation
            Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
            intent.putExtra("medicineName",medicineNameET.getText().toString());
            intent.putExtra("dosage",medicineQuantityET.getText().toString()+" "+selectedUnit);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), _id, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent); // First cancel existing id alarm
            switch (frequency) {
                case 0: // Everyday
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    makeToast("Herinnering opgeslagen voor elkedag");
                    saveReminderInRoom(_id, "Everyday");
                    break;
                case 1:
                    String repeatDays = repeatDaysET.getText().toString();
                    if (repeatDays.isEmpty() || repeatDays.equals("0")) {
                        makeSnack("Dagen moet groter zijn dan 0");
                        return;
                    }
                    int multiplier = Integer.parseInt(repeatDays);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), (AlarmManager.INTERVAL_DAY * multiplier), pendingIntent);
                    makeToast("Je krijgt elke " + repeatDays + " dagen een herinnering");
                    saveReminderInRoom(_id, "Repeat after every " + repeatDays + " days");
                    break;
                case 2:
                    String msg = "Herinnering voor ";
                    boolean selection = false;
                    if (mondayCB.isChecked()) {
                        setWeeklyReminder(_id, 2);
                        msg = msg + " Maandag,";
                        selection = true;
                    }
                    if (tuesdayCB.isChecked()) {
                        setWeeklyReminder(_id, 3);
                        msg = msg + " Dinsdag,";
                        selection = true;
                    }
                    if (wednesdayCB.isChecked()) {
                        setWeeklyReminder(_id, 4);
                        msg = msg + " Woensdag,";
                        selection = true;
                    }
                    if (thursdayCB.isChecked()) {
                        setWeeklyReminder(_id, 5);
                        msg = msg + " Donderdag,";
                        selection = true;
                    }
                    if (fridayCB.isChecked()) {
                        setWeeklyReminder(_id, 6);
                        msg = msg + " Vrijdag,";
                        selection = true;
                    }
                    if (saturdayCB.isChecked()) {
                        setWeeklyReminder(_id, 7);
                        msg = msg + " Zaterdag,";
                        selection = true;
                    }
                    if (sundayCB.isChecked()) {
                        setWeeklyReminder(_id, 1);
                        msg = msg + " Zondag,";
                        selection = true;
                    }
                    if (!selection) {
                        makeSnack("Select at least 1 day.");
                    } else {
                        makeToast(msg);
                        saveReminderInRoom(_id, msg);
                    }
                    break;
            }
        }
    }

    private void setWeeklyReminder(int _id, int DayOfWeek) {
        if (getActivity() != null) {
            reminderTime.set(Calendar.DAY_OF_WEEK, DayOfWeek);
            Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
            intent.putExtra("medicineName",medicineNameET.getText().toString());
            intent.putExtra("dosage",medicineQuantityET.getText().toString()+" "+selectedUnit);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), _id, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void saveReminderInRoom(int _id, String repeatInfo) {
        final String name = Objects.requireNonNull(medicineNameET.getText()).toString();
        final String quantity = Objects.requireNonNull(medicineQuantityET.getText()).toString();
        RemindersModel reminder = new RemindersModel(_id, Objects.requireNonNull(getUserID()), name, selectedUnit, quantity, alarmTime, repeatInfo);
        reminderDB.getRemindersDao().insertReminder(reminder);

        if(getAuthToken()!=null){
            Call<RemindersResponse> postReminderCall = service.postReminderToApi("Bearer "+ getAuthToken(),new RemindersBody(alarmTime,selectedUnit,quantity,repeatInfo,name));
            postReminderCall.enqueue(new Callback<RemindersResponse>() {
                @Override
                public void onResponse(@NotNull Call<RemindersResponse> call, @NotNull Response<RemindersResponse> response) {
                    if(response.code()==201){
                        Log.d("remindersLog->","Posted reminder to API. Status= "+response.code());
                    }
                    else { Log.d("remindersLog->","Post failed, Code = "+response.code()); }
                }
                @Override
                public void onFailure(@NotNull Call<RemindersResponse> call, @NotNull Throwable t) {
                    Log.d("remindersLog->","Failed to Post reminder to API.");
                }
            });
        }

        navController.popBackStack();
    }

    private String getAuthToken(){
        final SharedPreferences preferences = requireActivity().getSharedPreferences("AuthPrefs",Context.MODE_PRIVATE);
        return preferences.getString("AuthToken",null);
    }

    private void deleteReminder(RemindersModel reminder) {
        Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), reminder.get_id(), intent, 0);
        AlarmManager  alarmManager  = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent); // First cancel existing id alarm
        AlertDialog alertDialog = new AlertDialog.Builder(requireActivity()).create();
        alertDialog.setTitle("Confirm action");
        alertDialog.setMessage("Are you sure you want to delete this medicine reminder?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", (dialog, which) -> {
            reminderDB.getRemindersDao().deleteReminder(reminder);
            dialog.dismiss();
            navController.popBackStack();
        });
        alertDialog.show();
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

    private String getUserID(){
        if(prefs.contains("UserId")){
            return prefs.getString("UserId",null);
        }
        return null;
    }

    private void makeSnack(String msg) {
        Snackbar.make(editReminderLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    private void makeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}