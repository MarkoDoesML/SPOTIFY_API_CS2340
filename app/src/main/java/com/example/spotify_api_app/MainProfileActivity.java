package com.example.spotify_api_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Request;

public class MainProfileActivity extends AppCompatActivity {
    Button btn_wrapped;
    TextView textViewUsername;
    RecyclerView recyclerView;
    WrappedAdapter wrappedAdapter;
    List<WrappedItem> wrappedItemList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;

    private FirebaseFirestore database;
    private DatabaseReference mReference;

    public static AccessTokenData accessTokenData;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);

        btn_wrapped = findViewById(R.id.btn_wrapped_page);
        textViewUsername = findViewById(R.id.username);
        recyclerView = findViewById(R.id.wrappedList);

        database = FirebaseFirestore.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();

        final Request userProfileRequest = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + accessTokenData.getAccessToken())
                .build();

        final Request topArtistsRequest = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists")
                .addHeader("Authorization", "Bearer " + accessTokenData.getAccessToken())
                .build();

        final Request topTracksRequest = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks")
                .addHeader("Authorization", "Bearer " + accessTokenData.getAccessToken())
                .build();

        try {
            JSONObject jsonObject = api.makeRequest(userProfileRequest);
            if (jsonObject != null) {
                textViewUsername.setText(jsonObject.getString("display_name"));
            }
        } catch (JSONException e) {
            Log.d("JSON", "Failed to parse data: " + e);
        }
        wrappedAdapter = new WrappedAdapter(this, wrappedItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wrappedAdapter);



        btn_wrapped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//                String username = "Username";
//                wrappedAdapter.addItem(username, date);
//                wrappedItemList = new ArrayList<>();
                try {
                    JSONObject jsonObject = api.makeRequest(userProfileRequest);
                    if (jsonObject != null) {
                        String username = jsonObject.getString("display_name");
                    }
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    jsonObject = api.makeRequest(topArtistsRequest);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                }
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
}