package com.example.spotify_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import android.content.SharedPreferences;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity {


    int AUTH_CODE_REQUEST_CODE = api.AUTH_CODE_REQUEST_CODE;
    private String mAccessToken, mAccessCode;

    private TextView output;
    private AccessTokenData accessTokenData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button loginBtn = (Button) findViewById(R.id.login);
        Button testBtn = (Button) findViewById(R.id.test);
        output = (TextView) findViewById(R.id.output);

        login();


        // TODO: fix bug with gson, it doesn't always update after login
        // TODO: create a seperate module that has specific api requests built into it

        // gson storage for now, whoever is in charge of firebase needs to integrate
        // _____________________________________________________________
        // Read data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AccessTokenData", MODE_PRIVATE);
        String json = sharedPreferences.getString("accessTokenData", null);

        // Deserialize the JSON string using Gson
        Gson gson = new Gson();
        accessTokenData = gson.fromJson(json, AccessTokenData.class);

        if (accessTokenData != null) {
            mAccessCode = accessTokenData.getAccessCode();
            mAccessToken = accessTokenData.getAccessToken();
            setTextAsync(mAccessToken, output);
            // Use the access code and access token as needed
        } else {
            setTextAsync("Login was incorrect", output);
        }
        // _____________________________________________________________

        loginBtn.setOnClickListener((v) -> {
            login();
        });

        testBtn.setOnClickListener((v) -> {

            final Request request = new Request.Builder()
                    .url("https://api.spotify.com/v1/me")
                    .addHeader("Authorization", "Bearer " + mAccessToken)
                    .build();

            try {
                JSONObject jsonResponse = api.makeRequest(request);
                setTextAsync(jsonResponse.toString(4), output);
            } catch (JSONException e) {
                Log.d("JSON", "Failed to parse data: " + e);
            }
        });
    }

    public void login() {
        final AuthorizationRequest request = api.getAuth(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(MainActivity.this, AUTH_CODE_REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);
        String accessCode = response.getCode();
        String accessToken = api.getToken(accessCode);

        setTextAsync(accessToken + "\n\n" + accessCode, output);

        // save the accessCode and accessToken
        // accessCode can be used to generate another token if it is expired
        // _____________________________________________________________
        if (accessCode != null && accessToken != null) {
            AccessTokenData accessTokenData = new AccessTokenData(accessCode, accessToken);

            Gson gson = new Gson();
            String json = gson.toJson(accessTokenData);

            SharedPreferences sharedPreferences = getSharedPreferences("AccessTokenData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("accessTokenData", json);
            editor.apply();
        } else {
            Log.e("AccessToken", "Access code or access token is null");
        }
        // _____________________________________________________________

    }

    private void setTextAsync(final String text, TextView textView) {
        textView.setText(text);
    }

    @Override
    protected void onDestroy() {
        api.cancelCall();
        super.onDestroy();
    }
}