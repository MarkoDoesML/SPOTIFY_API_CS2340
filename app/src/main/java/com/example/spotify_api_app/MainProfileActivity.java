package com.example.spotify_api_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import com.squareup.picasso.Picasso;


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

public class MainProfileActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    Button btn_wrapped;
    Button btn_logout;
    RecyclerView recyclerView;
    WrappedAdapter wrappedAdapter;
    List<WrappedItem> wrappedItemList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private AccessTokenData accessTokenData;
    private String mAccessToken, mAccessCode;
    TextView usernameTextView;
    String username;
    CompletableFuture<DocumentSnapshot> output;
    private ImageView profileImage;
    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("AccessTokenData", MODE_PRIVATE);
        String json = sharedPreferences.getString("accessTokenData", null);
        TextView usernameTextView = findViewById(R.id.username);

        // Deserialize the JSON string using Gson
        Gson gson = new Gson();
        accessTokenData = gson.fromJson(json, AccessTokenData.class);

        mAccessCode = accessTokenData.getAccessCode();
        mAccessToken = accessTokenData.getAccessToken();

        btn_wrapped = findViewById(R.id.btn_wrapped_page);
        recyclerView = findViewById(R.id.wrappedList);
        btn_logout = findViewById(R.id.logout);
        profileImage = findViewById(R.id.profileImage);

        wrappedAdapter = new WrappedAdapter(this, wrappedItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wrappedAdapter);


//        username = db.get
//        usernameTextView.setText(ussername);

//        output = db.makeRequest("profile_info");

        String img_url = "https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da8463bcdace67f79859e30a17fa";
        JSONObject info = JSONStorageManager.loadData(getApplicationContext(), "profile_info");
        try {
            usernameTextView.setText(info.getString("display_name"));
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle the exception, such as setting a default text or showing an error message
        }

        try {
            if (info.getJSONArray("images").length() > 0) {
                img_url = info.getJSONArray("images").getJSONObject(1).getString("url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle the exception, such as setting a default value for img_url or showing an error message
        }


        Picasso.get().load(img_url).into(profileImage);


        btn_wrapped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> wrapped = api.makeWrapped(mAccessToken);
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String username = "Username";
                wrappedAdapter.addItem(username, date);
                wrappedItemList = new ArrayList<>();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Logic for Bottom Nav Bar
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_feed) {
                    startActivity(new Intent(getApplicationContext(), FeedActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
    }

    private void logoutUser() {
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        mAuth.signOut(); // Sign out the user from Firebase
        Intent intent = new Intent(MainProfileActivity.this, LoginActivity.class);
        startActivity(intent); // Navigate to LoginActivity
        finish(); // Close the current activity
    }
}