package com.example.spotify_api_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class SongsActivity extends AppCompatActivity {

    TextView num1, num2, num3, num4, num5, song1, song2, song3, song4, song5, artist1, artist2, artist3, artist4, artist5;
    ImageView pic1, pic2, pic3, pic4, pic5;
    String songName1, songName2, songName3, songName4, songName5, artistName1, artistName2, artistName3, artistName4, artistName5, songImage1, songImage2, songImage3, songImage4, songImage5;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        // Retrieve views from layout
        num1 = findViewById(R.id.num1);
        pic1 = findViewById(R.id.pic1);
        song1 = findViewById(R.id.songName1);
        artist1 = findViewById(R.id.artistName1);

        num2 = findViewById(R.id.num2);
        pic2 = findViewById(R.id.pic2);
        song2 = findViewById(R.id.songName2);
        artist2 = findViewById(R.id.artistName2);

        num3 = findViewById(R.id.num3);
        pic3 = findViewById(R.id.pic3);
        song3 = findViewById(R.id.songName3);
        artist3 = findViewById(R.id.artistName3);

        num4 = findViewById(R.id.num4);
        pic4 = findViewById(R.id.pic4);
        song4 = findViewById(R.id.songName4);
        artist4 = findViewById(R.id.artistName4);

        num5 = findViewById(R.id.num5);
        pic5 = findViewById(R.id.pic5);
        song5 = findViewById(R.id.songName5);
        artist5 = findViewById(R.id.artistName5);

        // Get song information from wrapped
        Intent i = getIntent();
        HashMap<String, Object> tracks = (HashMap<String, Object>) i.getSerializableExtra("tracks");

        // Example input values (replace these with your actual input)
        songName1 = ((HashMap<String, Object>) tracks.get("track1")).get("name").toString();
        artistName1 = ((ArrayList<String>) ((HashMap<String, Object>) tracks.get("track1")).get("artists")).get(0);
        songImage1 = ((HashMap<String, Object>) tracks.get("track1")).get("image").toString();

        songName2 = ((HashMap<String, Object>) tracks.get("track2")).get("name").toString();
        artistName2 = ((ArrayList<String>) ((HashMap<String, Object>) tracks.get("track2")).get("artists")).get(0);
        songImage2 = ((HashMap<String, Object>) tracks.get("track2")).get("image").toString();

        songName3 = ((HashMap<String, Object>) tracks.get("track3")).get("name").toString();
        artistName3 = ((ArrayList<String>) ((HashMap<String, Object>) tracks.get("track3")).get("artists")).get(0);
        songImage3 = ((HashMap<String, Object>) tracks.get("track3")).get("image").toString();

        songName4 = ((HashMap<String, Object>) tracks.get("track4")).get("name").toString();
        artistName4 = ((ArrayList<String>) ((HashMap<String, Object>) tracks.get("track4")).get("artists")).get(0);
        songImage4 = ((HashMap<String, Object>) tracks.get("track4")).get("image").toString();

        songName5 = ((HashMap<String, Object>) tracks.get("track5")).get("name").toString();
        artistName5 = ((ArrayList<String>) ((HashMap<String, Object>) tracks.get("track5")).get("artists")).get(0);
        songImage5 = ((HashMap<String, Object>) tracks.get("track5")).get("image").toString();

        // Set values to the views
        num1.setText("1");
        Picasso.get().load(songImage1).into(pic1);
        song1.setText(songName1);
        artist1.setText(artistName1);

        num2.setText("2");
        Picasso.get().load(songImage2).into(pic2);
        artist2.setText(artistName2);
        song2.setText(songName2);

        num3.setText("3");
        Picasso.get().load(songImage3).into(pic3);
        artist3.setText(artistName3);
        song3.setText(songName3);

        num4.setText("4");
        Picasso.get().load(songImage4).into(pic4);
        artist4.setText(artistName4);
        song4.setText(songName4);

        num5.setText("5");
        Picasso.get().load(songImage5).into(pic5);
        artist5.setText(artistName5);
        song5.setText(songName5);

        // Back button
        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
