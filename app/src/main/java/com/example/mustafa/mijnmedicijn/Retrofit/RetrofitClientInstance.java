package com.example.mustafa.mijnmedicijn.Retrofit;

import com.example.mustafa.mijnmedicijn.Retrofit.models.LoginBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.signup.SignupBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.signup.SignupResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://55dccfd888bc.ngrok.io/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public interface RetroInterFace {
        @Headers({"Accept: application/json","Content-Type: application/json"})
        @POST("api/auth/signup")
        Call<SignupResponse>signUpUser(@Body SignupBody newUser);

        @Headers({"Accept: application/json","Content-Type: application/json"})
        @POST("api/auth/login")
        Call<SignupBody>loginUser(@Body LoginBody authInfo);
    }

}