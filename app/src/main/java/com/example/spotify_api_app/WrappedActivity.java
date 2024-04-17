package com.example.spotify_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WrappedActivity extends AppCompatActivity {
    TextView durationTextView, topArtistName, topSongTitle, topSongArtist, topAlbumTitle, topAlbumArtist;
    ImageView topArtistImage, topSongImage, topAlbumImage;
    String durationText, artistName, artistImageUrl, songTitle, songArtist, songImageUrl, albumTitle, albumArtist, albumImageUrl;

    MaterialCardView topArtistCard, topSongCard, topAlbumCard;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapped);

        // Retrieve views from layout
        durationTextView = findViewById(R.id.textView3);
        topArtistName = findViewById(R.id.top_artist_name);
        topArtistImage = findViewById(R.id.top_artist_image);
        topSongTitle = findViewById(R.id.top_song_title);
        topSongArtist = findViewById(R.id.top_song_artist);
        topSongImage = findViewById(R.id.top_song_image);
        topAlbumTitle = findViewById(R.id.top_album_title);
        topAlbumArtist = findViewById(R.id.top_album_artist);
        topAlbumImage = findViewById(R.id.top_album_image);

        // Get wrapped information
        Intent i = getIntent();
        HashMap<String, Object> wrapped = (HashMap<String, Object>) i.getSerializableExtra("wrapped");


        // Set wrapped information to fields
        durationText = wrapped.get("duration").toString();
        artistName = ((HashMap<String, Object>) ((HashMap<String, Object>) wrapped.get("artists")).get("artist1")).get("name").toString();
        artistImageUrl = ((HashMap<String, Object>) ((HashMap<String, Object>) wrapped.get("artists")).get("artist1")).get("image").toString(); // Replace with actual resource ID
        songTitle = ((HashMap<String, Object>) ((HashMap<String, Object>) wrapped.get("tracks")).get("track1")).get("name").toString();
        songArtist = ((ArrayList<String>) (((HashMap<String, Object>) ((HashMap<String, Object>) wrapped.get("tracks")).get("track1")).get("artists"))).get(0);
        songImageUrl = ((HashMap<String, Object>) ((HashMap<String, Object>) wrapped.get("tracks")).get("track1")).get("image").toString(); // Replace with actual resource ID
        albumTitle = ((HashMap<String, Object>) ((HashMap<String, Object>) wrapped.get("albums")).get("album1")).get("name").toString();;
        albumArtist = ((ArrayList<String>) (((HashMap<String, Object>) ((HashMap<String, Object>) wrapped.get("albums")).get("album1")).get("artist"))).get(0);
        albumImageUrl = ((HashMap<String, Object>) ((HashMap<String, Object>) wrapped.get("albums")).get("album1")).get("image").toString(); // Replace with actual resource ID

        // TODO: use json content to fill ui
        // TODO: get rid of minutes
        // TODO: make titles bigger and centered with image
        // TODO: add a top genres list


        // Set values to the views
        if (durationText.equals("short_term")) durationTextView.setText("Your Past 4 Weeks in Music");
        else if (durationText.equals("medium_term")) durationTextView.setText("Your Past 6 Months in Music");
        else durationTextView.setText("Your Past Year in Music");
        topArtistName.setText(artistName);
        Picasso.get().load(artistImageUrl).into(topArtistImage);
        topSongTitle.setText(songTitle);
        topSongArtist.setText(songArtist);
        Picasso.get().load(songImageUrl).into(topSongImage);
        topAlbumTitle.setText(albumTitle);
        topAlbumArtist.setText(albumArtist);
        Picasso.get().load(albumImageUrl).into(topAlbumImage);

        topArtistCard = findViewById(R.id.top_artist_card);
        topSongCard = findViewById(R.id.top_song_card);
        topAlbumCard = findViewById(R.id.top_album_card);

        topArtistCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WrappedActivity.this, ArtistsActivity.class);
                intent.putExtra("artists", (HashMap<String, Object>) wrapped.get("artists"));
                startActivity(intent);
            }
        });

        topSongCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WrappedActivity.this, SongsActivity.class);
                intent.putExtra("tracks", (HashMap<String, Object>) wrapped.get("tracks"));
                startActivity(intent);
            }
        });

        topAlbumCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WrappedActivity.this, AlbumsActivity.class);
                intent.putExtra("albums", (HashMap<String, Object>) wrapped.get("albums"));
                startActivity(intent);
            }
        });

        // Back button
        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
