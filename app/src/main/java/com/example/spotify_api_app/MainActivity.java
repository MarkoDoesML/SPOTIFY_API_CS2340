package com.example.spotify_api_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import android.content.SharedPreferences;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

        loginBtn.setOnClickListener((v) -> {
            // Read data from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AccessTokenData", MODE_PRIVATE);
            String json = sharedPreferences.getString("accessTokenData", null);

            // Deserialize the JSON string using Gson
            Gson gson = new Gson();
            accessTokenData = gson.fromJson(json, AccessTokenData.class);

            if (accessTokenData != null) {
                mAccessCode = accessTokenData.getAccessCode();
                mAccessToken = accessTokenData.getAccessToken();
                // Use the access code and access token as needed
            } else {
                setTextAsync("Wrong!", output);
            }

        });

        testBtn.setOnClickListener((v) -> {
  

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

        if (accessCode != null && accessToken != null) {
            AccessTokenData accessTokenData = new AccessTokenData(accessCode, accessToken);

            Gson gson = new Gson();
            String json = gson.toJson(accessTokenData);

            // Store the serialized data in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AccessTokenData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("accessTokenData", json);
            editor.apply();
        } else {
            // Handle error or incomplete data
            Log.e("AccessToken", "Access code or access token is null");
        }
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