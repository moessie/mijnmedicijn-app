package com.example.mustafa.mijnmedicijn.Authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.mijnmedicijn.HomeActivity;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Retrofit.RetrofitClientInstance;
import com.example.mustafa.mijnmedicijn.Retrofit.models.login.LoginBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.login.LoginResponse;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends Fragment {

    private FrameLayout signInFrag;
    private LinearLayout signInPrgrs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        signInPrgrs = view.findViewById(R.id.signInPrgrs);
        TextView SignUpSwitchTV = view.findViewById(R.id.SignUpSwitchTV);
        signInFrag = view.findViewById(R.id.signInFrag);
        SignUpSwitchTV.setOnClickListener(v-> Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment));
        ExtendedFloatingActionButton SignInFAB = view.findViewById(R.id.SignInFAB);
        SignInFAB.setOnClickListener(v-> validateCredentials(view));
        return view;
    }

    private void validateCredentials(View view){
        TextInputEditText emailET = view.findViewById(R.id.LoginEmailET);
        TextInputEditText passwordET = view.findViewById(R.id.LoginPasswordET);

        final String email = Objects.requireNonNull(emailET.getText()).toString().trim();
        if(email.isEmpty()){showSnack("Please enter your email."); return;}

        final String password = Objects.requireNonNull(passwordET.getText()).toString().trim();
        if(password.isEmpty()){showSnack("Please enter your password."); return;}

        loginUser(email,password);

    }

    private void loginUser(final String email,final String password){
        signInPrgrs.setVisibility(View.VISIBLE);
        final RetrofitClientInstance.RetroInterFace service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.RetroInterFace.class);
        Call<LoginResponse> loginCall = service.loginUser(new LoginBody(email,password));
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    if(response.code()==200){
                        final SharedPreferences prefs = requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
                        if (response.body() != null) {
                            prefs.edit().putString("AuthToken",response.body().getAccessToken()).apply();
                        }
                        showToast("Signed In.");
                        startActivity(new Intent(requireActivity(), HomeActivity.class));
                        requireActivity().finish();
                    }
                    else {
                        showSnack("Failed to sign in. Please make sure your credentials are correct.");
                    }
                }
                else {
                    signInPrgrs.setVisibility(View.GONE);
                    showSnack("Failed to sign in. Please make sure your credentials are correct.");
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                signInPrgrs.setVisibility(View.GONE);
                showSnack("Sign In request failed.");
            }
        });
    }


    private void showSnack(String msg){
        Snackbar.make(signInFrag,msg,Snackbar.LENGTH_LONG).show();
    }

    private void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }

}