package com.example.mustafa.mijnmedicijn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mustafa.mijnmedicijn.Authentication.AuthActivity;
import com.example.mustafa.mijnmedicijn.Room.MijnmedicijnDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity {

    public static MijnmedicijnDB reminderDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_reminders, R.id.navigation_medication, R.id.navigation_settings).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        reminderDB = MijnmedicijnDB.getInstance(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        String token = null;
        final SharedPreferences prefs = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        if(prefs.contains("AuthToken")){
            token = prefs.getString("AuthToken",null);
        }
        if(token == null){
            Toast.makeText(this,"Failed to authenticate. Please Sign In.",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Both navigation bar back press and title bar back press will trigger this method
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if(reminderDB!=null){
            reminderDB.cleanUp();
        }
        super.onDestroy();
    }
}