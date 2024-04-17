package com.example.spotify_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.FileOutputStream;

public class WrappedActivity extends AppCompatActivity {
    TextView titleView, topGenresView, topArtistName, topSongTitle, topSongArtist, topAlbumTitle, topAlbumArtist;
    ImageView topArtistImage, topSongImage, topAlbumImage;
    String title, topGenres, artistName, songTitle, songArtist, albumTitle, albumArtist;
    int artistImageResource, songImageResource, albumImageResource;
    MaterialCardView topArtistCard, topSongCard, topAlbumCard;
    Button backButton;
    Button saveButton;
    private int screenshotCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapped);

        // Retrieve views from layout
        titleView = findViewById(R.id.title);
        topGenresView = findViewById(R.id.top_genres);
        topArtistName = findViewById(R.id.top_artist_name);
        topArtistImage = findViewById(R.id.top_artist_image);
        topSongTitle = findViewById(R.id.top_song_title);
        topSongArtist = findViewById(R.id.top_song_artist);
        topSongImage = findViewById(R.id.top_song_image);
        topAlbumTitle = findViewById(R.id.top_album_title);
        topAlbumArtist = findViewById(R.id.top_album_artist);
        topAlbumImage = findViewById(R.id.top_album_image);

        // Example input values (replace these with your actual input)
        topGenres = "Genre 1, Genre 2, Genre 3, Genre 4, Genre 5"; // Replace with string of top 5 genres
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
        saveButton = findViewById(R.id.save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshotAndSave();
            }
        });
    }
    private void takeScreenshotAndSave() {
        try {
            // Get the root view
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            rootView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            rootView.draw(canvas);

            // Save the bitmap to the gallery using MediaStore
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "screenshot_" + System.currentTimeMillis() + ".jpg");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Screenshots");
            values.put(MediaStore.Images.Media.IS_PENDING, 1);

            // Insert the metadata to the MediaStore
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Write the bitmap to the obtained uri
            if (uri != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();

                // Inform the media scanner of the new image
                values.put(MediaStore.Images.Media.IS_PENDING, 0);
                getContentResolver().update(uri, values, null, null);
            }

            Toast.makeText(this, "Screenshot saved to gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving screenshot: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

