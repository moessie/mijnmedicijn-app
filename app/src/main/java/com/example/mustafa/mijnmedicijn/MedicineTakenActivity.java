package com.example.mustafa.mijnmedicijn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.mijnmedicijn.Retrofit.RetrofitClientInstance;
import com.example.mustafa.mijnmedicijn.Retrofit.models.reminders.RemindersBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.reminders.RemindersResponse;
import com.example.mustafa.mijnmedicijn.Retrofit.models.tracker.TrackerBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.tracker.TrackerResponse;
import com.example.mustafa.mijnmedicijn.Room.MijnmedicijnDB;
import com.example.mustafa.mijnmedicijn.Room.Models.MedicationTrackerModel;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// This activity will open from notification click and add the medicine as a medicine the user took on current(this) time, this will help keep user keep track of the medication he takes

public class MedicineTakenActivity extends AppCompatActivity {

    private MijnmedicijnDB myDB = null;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_taken);
        prefs = getSharedPreferences("AuthPrefs",MODE_PRIVATE);
        final TextView intakeTimeTV = findViewById(R.id.intakeTimeTV);
        final TextView intakeDateTV = findViewById(R.id.intakeDateTV);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        intakeDateTV.setText(df.format(c));
        intakeTimeTV.setText(tf.format(c));

        if (getIntent().getExtras() != null) {
            final String medicineName = getIntent().getStringExtra("medicineName");
            final String dosage = getIntent().getStringExtra("dosage");

            if (medicineName != null && dosage != null) {
                final CardView recordDoseBtn = findViewById(R.id.recordDoseBtn);
                final TextView medicineNameTV = findViewById(R.id.medicineNameTV);
                final TextView dosageTV = findViewById(R.id.dosageTV);

                medicineNameTV.setText(medicineName);
                dosageTV.setText(dosage);

                recordDoseBtn.setOnClickListener(view -> {
                    myDB = MijnmedicijnDB.getInstance(getApplicationContext());
                    myDB.getMedicationTrackerDao().insertMedication(new MedicationTrackerModel((int) System.currentTimeMillis(),getUserID(), System.currentTimeMillis(),df.format(c),tf.format(c), medicineName, dosage));
                    Toast.makeText(MedicineTakenActivity.this,"Medication recorded.",Toast.LENGTH_SHORT).show();
                    if(getAuthToken()!=null){
                        final RetrofitClientInstance.RetroInterFace service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.RetroInterFace.class);
                        Call<TrackerResponse> postMedicineIntakeCall = service.postMedicineIntake("Bearer "+ getAuthToken(),new TrackerBody(df.format(c),dosage,tf.format(c),medicineName));
                        postMedicineIntakeCall.enqueue(new Callback<TrackerResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<TrackerResponse> call, @NotNull Response<TrackerResponse> response) {
                                if(response.code()==201){
                                    Log.d("medicineIntake->","Posted intake to API. Status= "+response.code());
                                }
                                else { Log.d("medicineIntake->","Intake Post failed, Code = "+response.code()); }
                            }

                            @Override
                            public void onFailure(@NotNull Call<TrackerResponse> call, @NotNull Throwable t) {
                                Log.d("medicineIntake->","Failed to Post medicine dosage to API.");
                            }
                        });
                    }
                    goToHome();
                });
            }
        }
    }

    private String getAuthToken(){
        return prefs.getString("AuthToken",null);
    }
    private String getUserID(){
        if(prefs.contains("UserId")){
            return prefs.getString("UserId",null);
        }
        return null;
    }
    private void goToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (myDB != null) {
            myDB.cleanUp();
        }
        super.onDestroy();
    }
}