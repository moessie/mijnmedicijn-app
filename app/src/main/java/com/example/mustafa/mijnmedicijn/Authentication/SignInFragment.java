package com.example.mustafa.mijnmedicijn.Authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.mijnmedicijn.HomeActivity;
import com.example.mustafa.mijnmedicijn.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class SignInFragment extends Fragment {

    private FrameLayout signInFrag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        TextView SignUpSwitchTV = view.findViewById(R.id.SignUpSwitchTV);
        signInFrag = view.findViewById(R.id.signInFrag);
        SignUpSwitchTV.setOnClickListener(v-> Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment));
        ExtendedFloatingActionButton SignInFAB = view.findViewById(R.id.SignInFAB);
        SignInFAB.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), HomeActivity.class));
            requireActivity().finish();
        });
        return view;
    }


    private void showSnack(String msg){
        Snackbar.make(signInFrag,msg,Snackbar.LENGTH_LONG).show();
    }

    private void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }

}