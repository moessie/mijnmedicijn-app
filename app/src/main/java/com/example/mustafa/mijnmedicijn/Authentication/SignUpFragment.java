package com.example.mustafa.mijnmedicijn.Authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mustafa.mijnmedicijn.HomeActivity;
import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Retrofit.RetrofitClientInstance;
import com.example.mustafa.mijnmedicijn.Retrofit.models.login.LoginBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.login.LoginResponse;
import com.example.mustafa.mijnmedicijn.Retrofit.models.signup.SignupBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.signup.SignupResponse;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    private FrameLayout signUpFrag;
    private LinearLayout signUpPrgrs;
    private RetrofitClientInstance.RetroInterFace service;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        signUpFrag = view.findViewById(R.id.signUpFrag);
        signUpPrgrs = view.findViewById(R.id.signUpPrgrs);
        TextView SignInSwitchTV = view.findViewById(R.id.SignInSwitchTV);
        SignInSwitchTV.setOnClickListener(v-> Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_signInFragment));

        ExtendedFloatingActionButton SignUpFAB = view.findViewById(R.id.SignUpFAB);
        SignUpFAB.setOnClickListener(v-> validateCredentials(view));

        return view;
    }

    private void validateCredentials(View view){
        TextInputEditText UsernameET = view.findViewById(R.id.UsernameET);
        TextInputEditText EmailET = view.findViewById(R.id.SignUpEmailET);
        TextInputEditText PasswordET = view.findViewById(R.id.SignUpPasswordET);
        TextInputEditText ConfirmPasswordET = view.findViewById(R.id.ConfirmPasswordET);

        final String userName = Objects.requireNonNull(UsernameET.getText()).toString().trim();
        if(userName.length()<3){showSnack("Gebruikersnaam moet tenminste 3 karakters zijn"); return;}

        final String email = Objects.requireNonNull(EmailET.getText()).toString().trim();
        if(email.isEmpty()){showSnack("E-mail niet ingevuld"); return;}

        final String password = Objects.requireNonNull(PasswordET.getText()).toString();
        if(password.length()<7){showSnack("Wachtwoord moet tenminste 7 karakters hebben"); return;}

        final String confirm = Objects.requireNonNull(ConfirmPasswordET.getText()).toString();
        if(confirm.isEmpty()){showSnack("Bevestig je wachtwoord"); return;}

        if(!confirm.equals(password)){showSnack("Wachtwoorden komen niet overeen"); return;}

        startSignUp(userName,email,password);
    }

    private void startSignUp(final String name,final String email,final String password){
        signUpPrgrs.setVisibility(View.VISIBLE);
        service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.RetroInterFace.class);
        Call<SignupResponse> signupCall = service.signUpUser(new SignupBody(name,email,password));
        Log.d("authLogs","signupCall->"+signupCall.request());
        signupCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(@NotNull Call<SignupResponse> call, @NotNull Response<SignupResponse> response) {
                if(response.isSuccessful()){
                    if(response.code() == 201){
                        showToast("Succesvol aangemeld.");
                        authenticateNewUser(email, password);
                    }
                    else if(response.code() == 422){
                        showSnack("E-mail al in gebruik");
                        signUpPrgrs.setVisibility(View.GONE);
                    }
                }
                else {
                    showSnack("Er is iets fout gegaan");
                    signUpPrgrs.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(@NotNull Call<SignupResponse> call, @NotNull Throwable t) {
                Log.d("authLogs->","Throwable->"+t.getMessage());
                showSnack("Niet gelukt om aan te melden");
                signUpPrgrs.setVisibility(View.GONE);
            }
        });
    }

    private void authenticateNewUser(final String email,final String password){
        Call<LoginResponse> loginCall = service.loginUser(new LoginBody(email,password));
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    if(response.code()==200){
                        final SharedPreferences prefs = requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
                        if (response.body() != null) {
                            prefs.edit().putString("UserId",email).apply();
                            prefs.edit().putString("AuthToken",response.body().getAccessToken()).apply();
                        }
                        startActivity(new Intent(requireActivity(), HomeActivity.class));
                        requireActivity().finish();
                    }
                }
                else {
                    signUpPrgrs.setVisibility(View.GONE);
                    showSnack("Gebruiker aangemaakt maar niet ingelogd");
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                signUpPrgrs.setVisibility(View.GONE);
                showSnack("Niet gelukt om in te loggen");
            }
        });
    }

    private void showSnack(String msg){
        Snackbar.make(signUpFrag,msg,Snackbar.LENGTH_LONG).show();
    }

    private void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }

}