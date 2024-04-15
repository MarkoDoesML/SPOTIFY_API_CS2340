package com.example.spotify_api_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    int AUTH_CODE_REQUEST_CODE = api.AUTH_CODE_REQUEST_CODE;
    private String mAccessToken, mAccessCode;

    private TextView output;
    private AccessTokenData accessTokenData;


    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin, btnSignUp;

    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private ImageView artistImage1, artistImage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        artistImage1 = findViewById(R.id.artistImage1);
        artistImage2 = findViewById(R.id.artistImage2);

        List<Integer> imageResources = new ArrayList<>();
        imageResources.add(R.drawable.bob_marley);
        imageResources.add(R.drawable.ed_sheeran);
        imageResources.add(R.drawable.jimi_hendrix);
        imageResources.add(R.drawable.johnny_cash);
        imageResources.add(R.drawable.eminem);
        imageResources.add(R.drawable.j_cole);
        imageResources.add(R.drawable.michael_jackson);
        imageResources.add(R.drawable.taylor_swift);

        Collections.shuffle(imageResources);

        artistImage1.setImageResource(imageResources.get(0));
        artistImage2.setImageResource(imageResources.get(1));

        // Ensure that the images are different
        while (imageResources.get(0).equals(imageResources.get(1))) {
            Collections.shuffle(imageResources);
            artistImage1.setImageResource(imageResources.get(0));
            artistImage2.setImageResource(imageResources.get(1));
        }

        mAuth = FirebaseAuth.getInstance();

        if (getIntent().getBooleanExtra("logout", false)) {
            mAuth.signOut();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
        // Check if user already logged in
        String initemail = sharedPreferences.getString("username", "");
        if (!initemail.isEmpty()) {
            navigateToMainFeedActivity();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserWithFirebase();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUserWithFirebase();
            }
        });
    }

    private void loginUserWithFirebase() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("Login", "signInWithEmail:success");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", email);
                        editor.putString("password", password);
                        editor.putString("uid", mAuth.getCurrentUser().getUid());
                        editor.apply();
                        navigateToMainFeedActivity();
                    } else {
                        String errorMessage = "Authentication failed.";
                        try {
                            throw task.getException();
                        } catch(Exception e) {
                            errorMessage = e.getMessage();
                        }
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void registerUserWithFirebase() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter an email and password to register.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignUp", "createUserWithEmail:success");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", email);
                        editor.putString("password", password);
                        String uid = mAuth.getCurrentUser().getUid();
                        editor.putString("uid",uid);
                        editor.apply();

                        try {
                            db db = new db(uid);
                            db.createProfile();
                            navigateToMainFeedActivity();
                        } catch (JSONException e) {
                            System.err.println("JSON Exception occurred: " + e.getMessage());
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    } else {
                        String errorMessage = "Registration failed.";
                        try {
                            throw task.getException();
                        } catch(Exception e) {
                            errorMessage = e.getMessage();  // Firebase specific error message
                        }
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }



    public void login() {
        final AuthorizationRequest request = api.getAuth(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(LoginActivity.this, AUTH_CODE_REQUEST_CODE, request);
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
        navigateToMainFeedActivity();
        // _____________________________________________________________

    }

    private void lastPart() {
        login();
    }

    private void navigateToMainFeedActivity() {
        Intent intent = new Intent(this, wSpotify.class);
        startActivity(intent);
        finish();
    }
}
