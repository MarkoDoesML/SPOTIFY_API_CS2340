package com.example.spotify_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainFeedActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed); // Make sure this is the correct layout file
        Log.d("MainFeedActivity", "MainFeedActivity started");
    }




}