package com.example.mustafa.mijnmedicijn.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mustafa.mijnmedicijn.Authentication.AuthActivity;
import com.example.mustafa.mijnmedicijn.R;

public class SettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        final SharedPreferences prefs = requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        final RelativeLayout LogoutBtn = view.findViewById(R.id.LogoutBtn);
        final TextView currentUser = view.findViewById(R.id.settingsID);

        if(prefs.contains("UserId")){
            String uid = prefs.getString("UserId","");
            currentUser.setText(uid);
        }

        LogoutBtn.setOnClickListener(v->{
            AlertDialog alertDialog = new AlertDialog.Builder(requireActivity()).create();
            alertDialog.setTitle("Bevestig");
            alertDialog.setMessage("Weetje zeker dat je wilt uitloggen ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nee", (dialog, which) -> dialog.dismiss());
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ja", (dialog, which) -> {
                prefs.edit().remove("UserId").apply();
                prefs.edit().remove("AuthToken").apply();
                startActivity(new Intent(requireActivity(), AuthActivity.class));
                requireActivity().finish();
            });
            alertDialog.show();
        });

        return view;
    }

}