package com.example.mustafa.mijnmedicijn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mustafa.mijnmedicijn.Room.RemindersDB;


// This activity will open from notification click and add the medicine as a medicine the user took on current(this) time, this will help keep user keep track of the medication he takes

public class MedicineTakenActivity extends AppCompatActivity {

    private RemindersDB myDB = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_taken);
        if(getIntent().getExtras()!=null){
            final String medicineName = getIntent().getStringExtra("medicineName");
            final String dosage = getIntent().getStringExtra("dosage");

            myDB = RemindersDB.getInstance(getApplicationContext());
        }
    }

    private void goToHome(){
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if(myDB!=null){
            myDB.cleanUp();
        }
        super.onDestroy();
    }
}