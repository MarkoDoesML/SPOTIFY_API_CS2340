package com.example.spotify_api_app;

import android.content.DialogInterface;
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

import java.util.HashMap;

public class ArtistsActivity extends AppCompatActivity {
    TextView num1, num2, num3, num4, num5, name1, name2, name3, name4, name5;
    ImageView pic1, pic2, pic3, pic4, pic5;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);

        // Retrieve views from layout
        num1 = findViewById(R.id.num1);
        pic1 = findViewById(R.id.pic1);
        name1 = findViewById(R.id.name1);

        num2 = findViewById(R.id.num2);
        pic2 = findViewById(R.id.pic2);
        name2 = findViewById(R.id.name2);

        num3 = findViewById(R.id.num3);
        pic3 = findViewById(R.id.pic3);
        name3 = findViewById(R.id.name3);

        num4 = findViewById(R.id.num4);
        pic4 = findViewById(R.id.pic4);
        name4 = findViewById(R.id.name4);

        num5 = findViewById(R.id.num5);
        pic5 = findViewById(R.id.pic5);
        name5 = findViewById(R.id.name5);

        // Get artists info from intent
        Intent i = getIntent();
        HashMap<String, Object> artists = (HashMap<String, Object>) i.getSerializableExtra("artists");

        // Set values to the views and make the artist names clickable
        setArtistInfo(num1, pic1, name1, artists, "artist1");
        setArtistInfo(num2, pic2, name2, artists, "artist2");
        setArtistInfo(num3, pic3, name3, artists, "artist3");
        setArtistInfo(num4, pic4, name4, artists, "artist4");
        setArtistInfo(num5, pic5, name5, artists, "artist5");

        // Back button listener
        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> finish());
    }

    private void setArtistInfo(TextView numView, ImageView picView, TextView nameView, HashMap<String, Object> artists, String artistKey) {
        HashMap<String, String> artistInfo = (HashMap<String, String>) artists.get(artistKey);
        String artistName = artistInfo.get("name");
        String artistImage = artistInfo.get("image");
        String artistUri = artistInfo.get("url");

        numView.setText(artistKey.replace("artist", ""));
        Picasso.get().load(artistImage).into(picView);
        nameView.setText(artistName);
        nameView.setClickable(true);
        nameView.setOnClickListener(v -> showConfirmationDialog(artistName, artistUri));
    }

    private void showConfirmationDialog(String artistName, String spotifyUri) {
        new AlertDialog.Builder(this)
                .setTitle("Open Spotify")
                .setMessage("Do you want to go to " + artistName + "'s Spotify page?")
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
            Log.e("ArtistsActivity", "Could not open Spotify URL, make sure the URL is correct: " + spotifyUri);
        }
    }
}