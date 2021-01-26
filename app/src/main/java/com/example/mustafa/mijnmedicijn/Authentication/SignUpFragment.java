package com.example.mustafa.mijnmedicijn.Authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.mijnmedicijn.R;
import com.example.mustafa.mijnmedicijn.Retrofit.RetrofitClientInstance;
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
        SignUpFAB.setOnClickListener(v->validateCredentials(view));

        return view;
    }

    private void validateCredentials(View view){
        TextInputEditText UsernameET = view.findViewById(R.id.UsernameET);
        TextInputEditText EmailET = view.findViewById(R.id.SignUpEmailET);
        TextInputEditText PasswordET = view.findViewById(R.id.SignUpPasswordET);
        TextInputEditText ConfirmPasswordET = view.findViewById(R.id.ConfirmPasswordET);

        final String userName = Objects.requireNonNull(UsernameET.getText()).toString();
        if(userName.length()<3){showSnack("Username must contain at least 3 characters."); return;}

        final String email = Objects.requireNonNull(EmailET.getText()).toString();
        if(email.isEmpty()){showSnack("Email required."); return;}

        final String password = Objects.requireNonNull(PasswordET.getText()).toString();
        if(password.length()<7){showSnack("Password must contain at least 7 characters."); return;}

        final String confirm = Objects.requireNonNull(ConfirmPasswordET.getText()).toString();
        if(confirm.isEmpty()){showSnack("Please confirm your password."); return;}

        if(!confirm.equals(password)){showSnack("Passwords do not match."); return;}

        startSignUp(userName,email,password);
    }

    private void startSignUp(final String name,final String email,final String password){
        signUpPrgrs.setVisibility(View.VISIBLE);
        RetrofitClientInstance.RetroInterFace service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.RetroInterFace.class);
        Call<SignupResponse> signupCall = service.signUpUser(new SignupBody(name,email,password));

        signupCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(@NotNull Call<SignupResponse> call, @NotNull Response<SignupResponse> response) {
                if(response.isSuccessful()){
                    if(response.code() == 201){
                        showSnack("Signup successful.");
                    }
                    else if(response.code() == 422){
                        showSnack("Signup failed. Email already taken.");
                    }
                }
                else {
                    showSnack("Something went wrong.");
                }
                signUpPrgrs.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<SignupResponse> call, @NotNull Throwable t) {
                showSnack("Failed to Sign Up.");
                signUpPrgrs.setVisibility(View.GONE);
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