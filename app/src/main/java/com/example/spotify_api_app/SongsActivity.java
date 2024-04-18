package com.example.spotify_api_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class SongsActivity extends AppCompatActivity {

    TextView num1, num2, num3, num4, num5, song1, song2, song3, song4, song5, artist1, artist2, artist3, artist4, artist5;
    ImageView pic1, pic2, pic3, pic4, pic5;
    String songName1, songName2, songName3, songName4, songName5, artistName1, artistName2, artistName3, artistName4, artistName5;
    String songImage1, songImage2, songImage3, songImage4, songImage5;
    String spotifyUri1, spotifyUri2, spotifyUri3, spotifyUri4, spotifyUri5; // Spotify URIs
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        // Initialize views
        initViews();

        // Get song information from intent
        Intent i = getIntent();
        HashMap<String, Object> tracks = (HashMap<String, Object>) i.getSerializableExtra("tracks");

        // Extract information for each song
        setupSongInfo(tracks);

        // Set listeners for the back button
        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> finish());
    }

    private void initViews() {
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
    }

    private void setupSongInfo(HashMap<String, Object> tracks) {
        // Setting up the first song
        songName1 = ((HashMap<String, Object>) tracks.get("track1")).get("name").toString();
        artistName1 = StringUtils.joinStrings((ArrayList<String>) ((HashMap<String, Object>) tracks.get("track1")).get("artists"));
        songImage1 = ((HashMap<String, Object>) tracks.get("track1")).get("image").toString();
        spotifyUri1 = ((HashMap<String, String>) tracks.get("track1")).get("url");

        Picasso.get().load(songImage1).into(pic1);
        song1.setText(songName1);
        artist1.setText(artistName1);
        song1.setOnClickListener(v -> showConfirmationDialog(songName1, spotifyUri1));

        // Setting up the second song
        songName2 = ((HashMap<String, Object>) tracks.get("track2")).get("name").toString();
        artistName2 = StringUtils.joinStrings((ArrayList<String>) ((HashMap<String, Object>) tracks.get("track2")).get("artists"));
        songImage2 = ((HashMap<String, Object>) tracks.get("track2")).get("image").toString();
        spotifyUri2 = ((HashMap<String, String>) tracks.get("track2")).get("url");

        Picasso.get().load(songImage2).into(pic2);
        song2.setText(songName2);
        artist2.setText(artistName2);
        song2.setOnClickListener(v -> showConfirmationDialog(songName2, spotifyUri2));

        // Setting up the third song
        songName3 = ((HashMap<String, Object>) tracks.get("track3")).get("name").toString();
        artistName3 = StringUtils.joinStrings((ArrayList<String>) ((HashMap<String, Object>) tracks.get("track3")).get("artists"));
        songImage3 = ((HashMap<String, Object>) tracks.get("track3")).get("image").toString();
        spotifyUri3 = ((HashMap<String, String>) tracks.get("track3")).get("url");

        Picasso.get().load(songImage3).into(pic3);
        song3.setText(songName3);
        artist3.setText(artistName3);
        song3.setOnClickListener(v -> showConfirmationDialog(songName3, spotifyUri3));

        // Setting up fourth song
        songName4 = ((HashMap<String, Object>) tracks.get("track4")).get("name").toString();
        artistName4 = StringUtils.joinStrings((ArrayList<String>) ((HashMap<String, Object>) tracks.get("track4")).get("artists"));
        songImage4 = ((HashMap<String, Object>) tracks.get("track4")).get("image").toString();
        spotifyUri4 = ((HashMap<String, String>) tracks.get("track4")).get("url");

        Picasso.get().load(songImage4).into(pic4);
        song4.setText(songName4);
        artist4.setText(artistName4);
        song4.setOnClickListener(v -> showConfirmationDialog(songName4, spotifyUri4));

        // Setting up fifth song
        songName5 = ((HashMap<String, Object>) tracks.get("track5")).get("name").toString();
        artistName5 = StringUtils.joinStrings((ArrayList<String>) ((HashMap<String, Object>) tracks.get("track5")).get("artists"));
        songImage5 = ((HashMap<String, Object>) tracks.get("track5")).get("image").toString();
        spotifyUri5 = ((HashMap<String, String>) tracks.get("track5")).get("url");

        Picasso.get().load(songImage5).into(pic5);
        song5.setText(songName5);
        artist5.setText(artistName5);
        song5.setOnClickListener(v -> showConfirmationDialog(songName5, spotifyUri5));
    }
    private void showConfirmationDialog(String songName, String spotifyUri) {
        new AlertDialog.Builder(this)
                .setTitle("Open Spotify")
                .setMessage("Do you want to go to " + songName + "'s page on Spotify?")
                .setPositiveButton("Yes", (dialog, which) -> openSpotify(spotifyUri))
                .setNegativeButton("No", null)
                .show();
    }

    private void openSpotify(String spotifyUri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUri));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("SongsActivity", "Could not open Spotify URL, make sure the URL is correct: " + spotifyUri);
        }
    }
}
