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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        // Check if user already logged in
        String savedUsername = sharedPreferences.getString("username", "");
        if (!savedUsername.isEmpty()) {
            // If username is saved, directly move to next activity
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
                        editor.apply();
                        navigateToMainFeedActivity();
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
