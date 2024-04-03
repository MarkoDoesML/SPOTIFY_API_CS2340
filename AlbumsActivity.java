package com.example.storyview2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AlbumsActivity extends AppCompatActivity {

    TextView num1, num2, num3, num4, num5, album1, album2, album3, album4, album5, artist1, artist2, artist3, artist4, artist5;
    ImageView pic1, pic2, pic3, pic4, pic5;
    String albumName1, albumName2, albumName3, albumName4, albumName5, artistName1, artistName2, artistName3, artistName4, artistName5;
    int artistImageResource1, artistImageResource2, artistImageResource3, artistImageResource4, artistImageResource5;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        // Retrieve views from layout
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

        // Example input values (replace these with your actual input)
        albumName1 = "Song 1";
        artistName1 = "Artist 1";
        artistImageResource1 = R.drawable.pinkfloyd; // Replace with actual resource ID

        albumName2 = "Song 2";
        artistName2 = "Artist 2";
        artistImageResource2 = R.drawable.pinkfloyd; // Replace with actual resource ID

        albumName3 = "Song 3";
        artistName3 = "Artist 3";
        artistImageResource3 = R.drawable.pinkfloyd; // Replace with actual resource ID

        albumName4 = "Song 4";
        artistName4 = "Artist 4";
        artistImageResource4 = R.drawable.pinkfloyd; // Replace with actual resource ID

        albumName5 = "Song 5";
        artistName5 = "Artist 5";
        artistImageResource5 = R.drawable.pinkfloyd; // Replace with actual resource ID

        // Set values to the views
        num1.setText("1");
        pic1.setImageResource(artistImageResource1);
        album1.setText(albumName1);
        artist1.setText(artistName1);

        num2.setText("2");
        pic2.setImageResource(artistImageResource2);
        artist2.setText(artistName2);
        album2.setText(albumName2);

        num3.setText("3");
        pic3.setImageResource(artistImageResource3);
        artist3.setText(artistName3);
        album3.setText(albumName3);

        num4.setText("4");
        pic4.setImageResource(artistImageResource4);
        artist4.setText(artistName4);
        album4.setText(albumName4);

        num5.setText("5");
        pic5.setImageResource(artistImageResource5);
        artist5.setText(artistName5);
        album5.setText(albumName5);

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


