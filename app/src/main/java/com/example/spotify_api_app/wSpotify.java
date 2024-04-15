package com.example.spotify_api_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class wSpotify extends AppCompatActivity{
    private SharedPreferences sharedPreferences;
    int AUTH_CODE_REQUEST_CODE = api.AUTH_CODE_REQUEST_CODE;
    private String mAccessToken, mAccessCode;

    public TextView output, textView;
    private AccessTokenData accessTokenData;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);
        textView = findViewById(R.id.textView);
        textView.setText("getting your info...");
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
        // updates spotify info

        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        JSONObject jsonResponse = api.makeRequest(request);
        JSONStorageManager.saveData(getApplicationContext(), "profile_info", jsonResponse);

        SharedPreferences auths = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String uid = auths.getString("uid", "junk");

        db db = new db(uid);
        try {
            db.storeUserProfile(jsonResponse);
        } catch (JSONException e) {
            Log.d("JSON", "Failed to parse data: " + e);
        }



//        JSONObject short_term = new JSONObject(api.makeWrapped(accessToken, "short_term"));
//        JSONObject medium_term = new JSONObject(api.makeWrapped(accessToken, "medium_term"));
//        JSONObject long_term = new JSONObject(api.makeWrapped(accessToken, "long_term"));

        // get user wrap info

        DocumentReference documentReference = db.getDocRef("number_of_wraps");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                try {
//                    Thread.sleep(30);
//                } catch (InterruptedException e) {
//                    error.printStackTrace();
//                }
                JSONStorageManager.saveData(getApplicationContext(), "number_of_wraps", new JSONObject(value.getData()));
                Log.d("tag", value.getData().toString());
            }
        });

        // get user wraps
        // document "wraps" -> store all wraps

        // get all public wraps

        // get collection public wraps


//        JSONStorageManager.saveData(getApplicationContext(), "short_term", short_term);
//        JSONStorageManager.saveData(getApplicationContext(), "medium_term", medium_term);
//        JSONStorageManager.saveData(getApplicationContext(), "long_term", long_term);

        navigateToMainFeedActivity();
        // _____________________________________________________________

    }

    private void navigateToMainFeedActivity() {
        Intent intent = new Intent(wSpotify.this, MainProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }


}
