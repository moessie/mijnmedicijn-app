package com.example.mustafa.mijnmedicijn.Retrofit;

import com.example.mustafa.mijnmedicijn.Retrofit.models.login.LoginBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.login.LoginResponse;
import com.example.mustafa.mijnmedicijn.Retrofit.models.reminders.RemindersBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.reminders.RemindersResponse;
import com.example.mustafa.mijnmedicijn.Retrofit.models.search.SearchResponse;
import com.example.mustafa.mijnmedicijn.Retrofit.models.signup.SignupBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.signup.SignupResponse;
import com.example.mustafa.mijnmedicijn.Retrofit.models.tracker.TrackerBody;
import com.example.mustafa.mijnmedicijn.Retrofit.models.tracker.TrackerResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://53093db7dd6a.ngrok.io/";

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
        Call<LoginResponse>loginUser(@Body LoginBody authInfo);

        ////////////////// Live Suggestions
        @Headers({"Accept: application/json","Content-Type: application/json"})
        @GET("api/search/{query}")
        Call<SearchResponse>getMedicinesList(@Header("Authorization") String authToken,@Path("query") String query);

        ///////////////// Post newly created reminder to API
        @Headers({"Accept: application/json","Content-Type: application/json"})
        @POST("api/reminder")
        Call<RemindersResponse>postReminderToApi(@Header("Authorization") String authToken, @Body RemindersBody remindersBody);

        @Headers({"Accept: application/json","Content-Type: application/json"})
        @POST("api/tracker")
        Call<TrackerResponse>postMedicineIntake(@Header("Authorization") String authToken, @Body TrackerBody trackerBody);

    }
}