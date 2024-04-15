package com.example.spotify_api_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import android.content.SharedPreferences;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.CompletableFuture;

public class wSpotify extends AppCompatActivity{
    private SharedPreferences sharedPreferences;
    int AUTH_CODE_REQUEST_CODE = api.AUTH_CODE_REQUEST_CODE;
    private String mAccessToken, mAccessCode;

    public TextView output, textView;
    private AccessTokenData accessTokenData;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);
        login();
    }

    private void login() {
        final AuthorizationRequest request = api.getAuth(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(wSpotify.this, AUTH_CODE_REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);
        String accessCode = response.getCode();
        String accessToken = api.getToken(accessCode);

        // save the accessCode and accessToken
        // accessCode can be used to generate another token if it is expired
        // _____________________________________________________________

        AccessTokenData accessTokenData = new AccessTokenData(accessCode, accessToken);

        Gson gson = new Gson();
        String json = gson.toJson(accessTokenData);

        SharedPreferences sharedPreferences = getSharedPreferences("AccessTokenData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessTokenData", json);
        editor.apply();
        textView = findViewById(R.id.textView);
        // updates spotify info
        textView.setText("getting latest spotify info...");
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        try {
            JSONObject jsonResponse = api.makeRequest(request);
            JSONStorageManager.saveData(getApplicationContext(), "profile_info", jsonResponse);
            db.storeUserProfile(jsonResponse);
        } catch (JSONException e) {
            Log.d("JSON", "Failed to parse data: " + e);
        }

        CompletableFuture<DocumentSnapshot> number_of_wraps = db.makeRequest("number_of_wraps");


        // get user wraps
        textView.setText("getting your previous wraps...");


        // get all public wraps
        textView.setText("getting other public wraps...");

        navigateToMainFeedActivity();
        // _____________________________________________________________

    }

    private void navigateToMainFeedActivity() {
        Intent intent = new Intent(wSpotify.this, MainProfileActivity.class);
        startActivity(intent);
        finish();
    }

}
