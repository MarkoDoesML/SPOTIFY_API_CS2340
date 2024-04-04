package com.example.spotify_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainFeedActivity extends AppCompatActivity {
    Button btn_wrapped;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed); // Make sure this is the correct layout file
        Log.d("MainFeedActivity", "MainFeedActivity started");

        btn_wrapped = findViewById(R.id.btn_wrapped_page);

        btn_wrapped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainFeedActivity.this, WrappedActivity.class);
                startActivity(intent);
            }
        });
    }


}