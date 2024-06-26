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
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.FileOutputStream;

public class WrappedActivity extends AppCompatActivity {
    TextView titleView, topGenresView, topArtistName, topSongTitle, topSongArtist, topAlbumTitle, topAlbumArtist;
    ImageView topArtistImage, topSongImage, topAlbumImage;
    String username, title, topGenres, artistName, artistImageUrl, songTitle, songArtist, songImageUrl, albumTitle, albumArtist, albumImageUrl;

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

        // Get wrapped information
        Intent i = getIntent();
        HashMap<String, Object> wrapped = (HashMap<String, Object>) i.getSerializableExtra("wrapped");

        // Get username for the current user
        JSONObject info = JSONStorageManager.loadData(getApplicationContext(), "profile_info");
        try {
            username = info.getString("display_name");
        } catch (JSONException e) {
            Log.d("WrappedActivityUser", "Could not get username for wrapped: " + e);
        }


        // Set wrapped information to fields
        title = wrapped.get("duration").toString();
        topGenres = StringUtils.joinAndCapitalizeFirstLetters((ArrayList<String>) wrapped.get("genres"));
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
        if (username.equals(wrapped.get("username").toString())) {
            if (title.equals("short_term")) titleView.setText("Your Past 4 Weeks in Music");
            else if (title.equals("medium_term")) titleView.setText("Your Past 6 Months in Music");
            else titleView.setText("Your Past Year in Music");
        } else {
            if (title.equals("short_term")) titleView.setText(wrapped.get("username").toString() + "'s Past 4 Weeks in Music");
            else if (title.equals("medium_term")) titleView.setText(wrapped.get("username").toString() + "'s Past 6 Months in Music");
            else titleView.setText(wrapped.get("username").toString()+ "'s Past Year in Music");
        }
        topGenresView.setText(topGenres);
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

