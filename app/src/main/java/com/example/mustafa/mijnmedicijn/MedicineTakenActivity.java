package com.example.mustafa.mijnmedicijn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.mijnmedicijn.Room.MijnmedicijnDB;
import com.example.mustafa.mijnmedicijn.Room.Models.MedicationTrackerModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


// This activity will open from notification click and add the medicine as a medicine the user took on current(this) time, this will help keep user keep track of the medication he takes

public class MedicineTakenActivity extends AppCompatActivity {

    private MijnmedicijnDB myDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_taken);

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
                    myDB.getMedicationTrackerDao().insertMedication(new MedicationTrackerModel((int) System.currentTimeMillis(), System.currentTimeMillis(),df.format(c),tf.format(c), medicineName, dosage));
                    Toast.makeText(MedicineTakenActivity.this,"Medication recorded.",Toast.LENGTH_SHORT).show();
                    goToHome();
                });
            }
        }
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