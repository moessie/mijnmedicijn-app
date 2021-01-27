package com.example.mustafa.mijnmedicijn.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mustafa.mijnmedicijn.HomeActivity;
import com.example.mustafa.mijnmedicijn.R;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String token = null;
        String uid = null;
        final SharedPreferences prefs = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        if(prefs.contains("AuthToken")){
            token = prefs.getString("AuthToken",null);
        }
        if(prefs.contains("UserId")){
            uid = prefs.getString("UserId",null);
        }
        if(uid!=null && token!=null){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}