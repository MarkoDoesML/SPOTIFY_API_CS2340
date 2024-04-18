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

public class AlbumsActivity extends AppCompatActivity {

    TextView num1, num2, num3, num4, num5, album1, album2, album3, album4, album5, artist1, artist2, artist3, artist4, artist5;
    ImageView pic1, pic2, pic3, pic4, pic5;
    String albumName1, albumName2, albumName3, albumName4, albumName5, artistName1, artistName2, artistName3, artistName4, artistName5;
    String albumImage1, albumImage2, albumImage3, albumImage4, albumImage5;
    String spotifyUri1, spotifyUri2, spotifyUri3, spotifyUri4, spotifyUri5; // Spotify URIs
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        // Retrieve views from layout
        initViews();

        // Get album information from intent
        Intent i = getIntent();
        HashMap<String, Object> albums = (HashMap<String, Object>) i.getSerializableExtra("albums");

        // Set up album information
        setupAlbumInfo(albums);

        // Back button
        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> finish());
    }

    private void initViews() {
        num1 = findViewById(R.id.num1);
        pic1 = findViewById(R.id.pic1);
        album1 = findViewById(R.id.album1);
        artist1 = findViewById(R.id.artistName1);

        num2 = findViewById(R.id.num2);
        pic2 = findViewById(R.id.pic2);
        album2 = findViewById(R.id.album2);
        artist2 = findViewById(R.id.artistName2);

        num3 = findViewById(R.id.num3);
        pic3 = findViewById(R.id.pic3);
        album3 = findViewById(R.id.album3);
        artist3 = findViewById(R.id.artistName3);

        num4 = findViewById(R.id.num4);
        pic4 = findViewById(R.id.pic4);
        album4 = findViewById(R.id.album4);
        artist4 = findViewById(R.id.artistName4);

        num5 = findViewById(R.id.num5);
        pic5 = findViewById(R.id.pic5);
        album5 = findViewById(R.id.album5);
        artist5 = findViewById(R.id.artistName5);
    }

    private void setupAlbumInfo(HashMap<String, Object> albums) {

        // Setting up first album
        albumName1 = ((HashMap<String, Object>) albums.get("album1")).get("name").toString();
        artistName1 = StringUtils.joinStrings((ArrayList<String>) ((HashMap<String, Object>) albums.get("album1")).get("artist"));
        albumImage1 = ((HashMap<String, Object>) albums.get("album1")).get("image").toString();
        spotifyUri1 = ((HashMap<String, String>) albums.get("album1")).get("url");

        Picasso.get().load(albumImage1).into(pic1);
        album1.setText(albumName1);
        artist1.setText(artistName1);
        album1.setOnClickListener(v -> showConfirmationDialog(albumName1, spotifyUri1));

        // Setting up second album
        albumName2 = ((HashMap<String, Object>) albums.get("album2")).get("name").toString();
        artistName2 = StringUtils.joinStrings((ArrayList<String>) ((HashMap<String, Object>) albums.get("album2")).get("artist"));
        albumImage2 = ((HashMap<String, Object>) albums.get("album2")).get("image").toString();
        spotifyUri2 = ((HashMap<String, String>) albums.get("album2")).get("url");

        Picasso.get().load(albumImage2).into(pic2);
        album2.setText(albumName2);
        artist2.setText(artistName2);
        album2.setOnClickListener(v -> showConfirmationDialog(albumName2, spotifyUri2));

        // Setting up third album
        albumName3 = ((HashMap<String, Object>) albums.get("album3")).get("name").toString();
        artistName3 = StringUtils.joinStrings((ArrayList<String>) ((HashMap<String, Object>) albums.get("album3")).get("artist"));
        albumImage3 = ((HashMap<String, Object>) albums.get("album3")).get("image").toString();
        spotifyUri3 = ((HashMap<String, String>) albums.get("album3")).get("url");

        Picasso.get().load(albumImage3).into(pic3);
        album3.setText(albumName3);
        artist3.setText(artistName3);
        album3.setOnClickListener(v -> showConfirmationDialog(albumName3, spotifyUri3));

        // Setting up fourth album
        albumName4 = ((HashMap<String, Object>) albums.get("album4")).get("name").toString();
        artistName4 = StringUtils.joinStrings((ArrayList<String>) ((HashMap<String, Object>) albums.get("album4")).get("artist"));
        albumImage4 = ((HashMap<String, Object>) albums.get("album4")).get("image").toString();
        spotifyUri4 = ((HashMap<String, String>) albums.get("album4")).get("url");

        Picasso.get().load(albumImage4).into(pic4);
        album4.setText(albumName4);
        artist4.setText(artistName4);
        album4.setOnClickListener(v -> showConfirmationDialog(albumName4, spotifyUri4));

        // Setting up fifth album
        albumName5 = ((HashMap<String, Object>) albums.get("album5")).get("name").toString();
        artistName5 = StringUtils.joinStrings((ArrayList<String>) ((HashMap<String, Object>) albums.get("album5")).get("artist"));
        albumImage5 = ((HashMap<String, Object>) albums.get("album5")).get("image").toString();
        spotifyUri5 = ((HashMap<String, String>) albums.get("album5")).get("url");

        Picasso.get().load(albumImage5).into(pic5);
        album5.setText(albumName5);
        artist5.setText(artistName5);
        album5.setOnClickListener(v -> showConfirmationDialog(albumName5, spotifyUri5));
    }

    private void showConfirmationDialog(String albumName, String spotifyUri) {
        new AlertDialog.Builder(this)
                .setTitle("Open Spotify")
                .setMessage("Do you want to go to " + albumName + "'s page on Spotify?")
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
            Log.e("AlbumsActivity", "Could not open Spotify URL, make sure the URL is correct: " + spotifyUri);
        }
    }
}
