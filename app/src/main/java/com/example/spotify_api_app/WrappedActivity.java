package com.example.spotify_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;

public class WrappedActivity extends AppCompatActivity {
    TextView topArtistName, topSongTitle, topSongArtist, topAlbumTitle, topAlbumArtist;
    ImageView topArtistImage, topSongImage, topAlbumImage;
    String artistName, songTitle, songArtist, albumTitle, albumArtist;
    int artistImageResource, songImageResource, albumImageResource;
    MaterialCardView topArtistCard, topSongCard, topAlbumCard;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapped);

        // Retrieve views from layout
        topArtistName = findViewById(R.id.top_artist_name);
        topArtistImage = findViewById(R.id.top_artist_image);
        topSongTitle = findViewById(R.id.top_song_title);
        topSongArtist = findViewById(R.id.top_song_artist);
        topSongImage = findViewById(R.id.top_song_image);
        topAlbumTitle = findViewById(R.id.top_album_title);
        topAlbumArtist = findViewById(R.id.top_album_artist);
        topAlbumImage = findViewById(R.id.top_album_image);

        // Example input values (replace these with your actual input)
        artistName = "Example Artist";
        artistImageResource = R.drawable.artist_profile_pic; // Replace with actual resource ID
        songTitle = "Example Song";
        songArtist = "Example Artist";
        songImageResource = R.drawable.tpab; // Replace with actual resource ID
        albumTitle = "Example Album";
        albumArtist = "Example Artist";
        albumImageResource = R.drawable.pinkfloyd; // Replace with actual resource ID

        // TODO: use json content to fill ui
        // TODO: get rid of minutes
        // TODO: make titles bigger and centered with image
        // TODO: add a top genres list


        // Set values to the views
        topArtistName.setText(artistName);
        topArtistImage.setImageResource(artistImageResource);
        topSongTitle.setText(songTitle);
        topSongArtist.setText(songArtist);
        topSongImage.setImageResource(songImageResource);
        topAlbumTitle.setText(albumTitle);
        topAlbumArtist.setText(albumArtist);
        topAlbumImage.setImageResource(albumImageResource);

        topArtistCard = findViewById(R.id.top_artist_card);
        topSongCard = findViewById(R.id.top_song_card);
        topAlbumCard = findViewById(R.id.top_album_card);

        topArtistCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WrappedActivity.this, ArtistsActivity.class);
                startActivity(intent);
            }
        });

        topSongCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WrappedActivity.this, SongsActivity.class);
                startActivity(intent);
            }
        });

        topAlbumCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WrappedActivity.this, AlbumsActivity.class);
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
