package com.example.mustafa.mijnmedicijn.ui.reminders;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
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
import com.example.mustafa.mijnmedicijn.Recycler.adapters.SuggestionsAdapter;
import com.example.mustafa.mijnmedicijn.Broadcasts.ReminderBroadcast;
import com.example.mustafa.mijnmedicijn.Retrofit.RetrofitClientInstance;
import com.example.mustafa.mijnmedicijn.Retrofit.models.login.LoginBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.login.LoginResponse;
import com.example.mustafa.mijnmedicijn.Retrofit.models.search.DataItem;
import com.example.mustafa.mijnmedicijn.Retrofit.models.search.SearchResponse;
import com.example.mustafa.mijnmedicijn.Room.Models.MedsSuggestionsModel;
import com.example.mustafa.mijnmedicijn.Room.Models.RemindersModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.mijnmedicijn.HomeActivity.reminderDB;

public class AddReminderFragment extends Fragment {

    private SuggestionsAdapter suggestionsAdapter;
    private ScrollView addReminderFrag;
    private final Calendar reminderTime = Calendar.getInstance();
    private final List<String> suggestionsList = new ArrayList<>();
    private final List<String> medicinesList = DataHelper.getTestMedicines(); // TODO : Get this through API
    private String selectedUnit = "pill(s)"; // set a default selectedUnit, currently hardcoded but can be changed to first item of spinner
    private String alarmTime = "";
    private int frequency = 0; // 0 = everyday, 1 = X alternate days , 2 = Selected WeekDays
    private TextInputEditText medicineNameET, medicineQuantityET;
    private LinearLayout repeatAfterXDays, daysOfWeek;
    private RelativeLayout addReminderLayout;
    private EditText repeatDaysET;
    private TextView ReminderTimeTV;
    private CheckBox mondayCB, tuesdayCB, wednesdayCB, thursdayCB, fridayCB, saturdayCB, sundayCB;
    private boolean timeSelected = false;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private RetrofitClientInstance.RetroInterFace service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.RetroInterFace.class);
        findViews(view);
        setUpSpinners(view);
        initializeSuggestionsRV(view);
        return view;
    }

    private void findViews(View view) {
        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();
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
        saveReminderFAB.setOnClickListener(v -> setReminder());
    }

    private void initializeSuggestionsRV(View view) {
        RecyclerView suggestionRV = view.findViewById(R.id.searchSuggestionsRV);
        suggestionsAdapter = new SuggestionsAdapter(suggestionsList, medicineNameET);
        suggestionRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        suggestionRV.setAdapter(suggestionsAdapter);

        medicineNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i0, int i1, int i2) {
                final String queryText = charSequence.toString().toLowerCase();
                suggestionsList.clear();
                suggestionsAdapter.notifyDataSetChanged();
                if (!queryText.isEmpty()) {
                    try {
                        if (isNetworkConnected() && isInternetWorking()) {
                            Call<SearchResponse> searchCall = service.getMedicinesList(queryText);
                            Log.d("retroLogs->","Call = "+searchCall.request());
                            searchCall.enqueue(new Callback<SearchResponse>() {
                                @Override
                                public void onResponse(@NotNull Call<SearchResponse> call, @NotNull Response<SearchResponse> response) {
                                    if (response.code() == 200 && response.body() != null && response.body().getData() != null) {
                                        final List<DataItem> medicinesList = response.body().getData();
                                        if (!medicinesList.isEmpty()) {
                                            suggestionsList.clear();
                                            for(DataItem med : medicinesList){
                                                suggestionsList.add(med.getName());
                                                reminderDB.getMedsSuggestionDao().addSuggestion(new MedsSuggestionsModel(med.getId(),med.getName())); // Save item for offline use
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(@NotNull Call<SearchResponse> call, @NotNull Throwable t) {
                                }
                            });
                        }
                        else {
                            final List<MedsSuggestionsModel>offlineSuggestions = reminderDB.getMedsSuggestionDao().getSuggestionsList();
                            suggestionsList.clear();
                            for(MedsSuggestionsModel suggestion : offlineSuggestions){
                                if(suggestion.getMedicineName().contains(queryText)){
                                    suggestionsList.add(suggestion.getMedicineName());
                                }
                            }
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetWorking() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
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
            reminderTime.set(Calendar.MILLISECOND, 0);

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

    private void setReminder() {
        if (Objects.requireNonNull(medicineNameET.getText()).toString().isEmpty()) {
            makeSnack("Enter Medicine Name");
            return;
        }
        final String quantity = Objects.requireNonNull(medicineQuantityET.getText()).toString();
        if (quantity.isEmpty() || quantity.equals("0")) {
            makeSnack("Enter Medicine Quantity");
            return;
        }
        if (!timeSelected) {
            makeSnack("Reminder time not set");
            return;
        }
        if (getActivity() != null) {
            final int _id = (int) System.currentTimeMillis(); // Alarm ID, to be used for cancellation
            Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
            intent.putExtra("medicineName", medicineNameET.getText().toString());
            intent.putExtra("dosage", medicineQuantityET.getText().toString() + " " + selectedUnit);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), _id, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            switch (frequency) {
                case 0: // Everyday
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    makeToast("Reminder set for everyday.");
                    saveReminderInRoom(_id, "Everyday");
                    break;
                case 1:
                    String repeatDays = repeatDaysET.getText().toString();
                    if (repeatDays.isEmpty() || repeatDays.equals("0")) {
                        makeSnack("Repeat days should be greater than 0");
                        return;
                    }
                    int multiplier = Integer.parseInt(repeatDays);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), (AlarmManager.INTERVAL_DAY * multiplier), pendingIntent);
                    makeToast("You will be reminded after every " + repeatDays + " days");
                    saveReminderInRoom(_id, "Repeat after every " + repeatDays + " days");
                    break;
                case 2:
                    String msg = "Reminder set for ";
                    boolean selection = false;
                    if (mondayCB.isChecked()) {
                        setWeeklyReminder(_id, 2);
                        msg = msg + " Monday,";
                        selection = true;
                    }
                    if (tuesdayCB.isChecked()) {
                        setWeeklyReminder(_id, 3);
                        msg = msg + " Tuesday,";
                        selection = true;
                    }
                    if (wednesdayCB.isChecked()) {
                        setWeeklyReminder(_id, 4);
                        msg = msg + " Wednesday,";
                        selection = true;
                    }
                    if (thursdayCB.isChecked()) {
                        setWeeklyReminder(_id, 5);
                        msg = msg + " Thursday,";
                        selection = true;
                    }
                    if (fridayCB.isChecked()) {
                        setWeeklyReminder(_id, 6);
                        msg = msg + " Friday,";
                        selection = true;
                    }
                    if (saturdayCB.isChecked()) {
                        setWeeklyReminder(_id, 7);
                        msg = msg + " Saturday,";
                        selection = true;
                    }
                    if (sundayCB.isChecked()) {
                        setWeeklyReminder(_id, 1);
                        msg = msg + " Sunday,";
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
            intent.putExtra("medicineName", medicineNameET.getText().toString());
            intent.putExtra("dosage", medicineQuantityET.getText().toString() + " " + selectedUnit);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), _id, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void saveReminderInRoom(int _id, String repeatInfo) {
        final String name = Objects.requireNonNull(medicineNameET.getText()).toString();
        final String quantity = Objects.requireNonNull(medicineQuantityET.getText()).toString();
        RemindersModel reminder = new RemindersModel(_id, name, selectedUnit, quantity, alarmTime, repeatInfo);
        reminderDB.getRemindersDao().insertReminder(reminder);
        navController.popBackStack();
    }

    private void makeSnack(String msg) {
        Snackbar.make(addReminderFrag, msg, Snackbar.LENGTH_LONG).show();
    }

    private void makeToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}