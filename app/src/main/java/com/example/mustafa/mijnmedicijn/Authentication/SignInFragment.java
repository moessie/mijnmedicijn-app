package com.example.mustafa.mijnmedicijn.Authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mustafa.mijnmedicijn.R;

public class SignInFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        TextView SignUpSwitchTV = view.findViewById(R.id.SignUpSwitchTV);

        SignUpSwitchTV.setOnClickListener(v-> Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment));

        return view;
    }
}