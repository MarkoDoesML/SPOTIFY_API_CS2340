package com.example.spotify_api_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SongsActivity extends AppCompatActivity {

    TextView num1, num2, num3, num4, num5, song1, song2, song3, song4, song5, artist1, artist2, artist3, artist4, artist5;
    ImageView pic1, pic2, pic3, pic4, pic5;
    String songName1, songName2, songName3, songName4, songName5, artistName1, artistName2, artistName3, artistName4, artistName5;
    int artistImageResource1, artistImageResource2, artistImageResource3, artistImageResource4, artistImageResource5;
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

        // Example input values (replace these with your actual input)
        songName1 = "Song 1";
        artistName1 = "Artist 1";
        artistImageResource1 = R.drawable.tpab; // Replace with actual resource ID

        songName2 = "Song 2";
        artistName2 = "Artist 2";
        artistImageResource2 = R.drawable.tpab; // Replace with actual resource ID

        songName3 = "Song 3";
        artistName3 = "Artist 3";
        artistImageResource3 = R.drawable.tpab; // Replace with actual resource ID

        songName4 = "Song 4";
        artistName4 = "Artist 4";
        artistImageResource4 = R.drawable.tpab; // Replace with actual resource ID

        songName5 = "Song 5";
        artistName5 = "Artist 5";
        artistImageResource5 = R.drawable.tpab; // Replace with actual resource ID

        // Set values to the views
        num1.setText("1");
        pic1.setImageResource(artistImageResource1);
        song1.setText(songName1);
        artist1.setText(artistName1);

        num2.setText("2");
        pic2.setImageResource(artistImageResource2);
        artist2.setText(artistName2);
        song2.setText(songName2);

        num3.setText("3");
        pic3.setImageResource(artistImageResource3);
        artist3.setText(artistName3);
        song3.setText(songName3);

        num4.setText("4");
        pic4.setImageResource(artistImageResource4);
        artist4.setText(artistName4);
        song4.setText(songName4);

        num5.setText("5");
        pic5.setImageResource(artistImageResource5);
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
