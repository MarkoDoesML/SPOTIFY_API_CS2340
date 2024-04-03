package com.example.storyview2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ArtistsActivity extends AppCompatActivity {
    TextView num1, num2, num3, num4, num5, name1, name2, name3, name4, name5, minutes1, minutes2, minutes3, minutes4, minutes5;
    ImageView pic1, pic2, pic3, pic4, pic5;
    String artistName1, artistName2, artistName3, artistName4, artistName5;
    int artistImageResource1, artistImageResource2, artistImageResource3, artistImageResource4, artistImageResource5;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);

        // Retrieve views from layout
        num1 = findViewById(R.id.num1);
        pic1 = findViewById(R.id.pic1);
        name1 = findViewById(R.id.name1);
        minutes1 = findViewById(R.id.minutes1);

        num2 = findViewById(R.id.num2);
        pic2 = findViewById(R.id.pic2);
        name2 = findViewById(R.id.name2);
        minutes2 = findViewById(R.id.minutes2);

        num3 = findViewById(R.id.num3);
        pic3 = findViewById(R.id.pic3);
        name3 = findViewById(R.id.name3);
        minutes3 = findViewById(R.id.minutes3);

        num4 = findViewById(R.id.num4);
        pic4 = findViewById(R.id.pic4);
        name4 = findViewById(R.id.name4);
        minutes4 = findViewById(R.id.minutes4);

        num5 = findViewById(R.id.num5);
        pic5 = findViewById(R.id.pic5);
        name5 = findViewById(R.id.name5);
        minutes5 = findViewById(R.id.minutes5);

        // Example input values (replace these with your actual input)
        artistName1 = "Artist 1";
        artistImageResource1 = R.drawable.artist_profile_pic; // Replace with actual resource ID
        artistName2 = "Artist 2";
        artistImageResource2 = R.drawable.artist_profile_pic; // Replace with actual resource ID
        artistName3 = "Artist 3";
        artistImageResource3 = R.drawable.artist_profile_pic; // Replace with actual resource ID
        artistName4 = "Artist 4";
        artistImageResource4 = R.drawable.artist_profile_pic; // Replace with actual resource ID
        artistName5 = "Artist 5";
        artistImageResource5 = R.drawable.artist_profile_pic; // Replace with actual resource ID

        // Set values to the views
        num1.setText("1");
        pic1.setImageResource(artistImageResource1);
        name1.setText(artistName1);
        minutes1.setText("Minutes");

        num2.setText("2");
        pic2.setImageResource(artistImageResource2);
        name2.setText(artistName2);
        minutes2.setText("Minutes");

        num3.setText("3");
        pic3.setImageResource(artistImageResource3);
        name3.setText(artistName3);
        minutes3.setText("Minutes");

        num4.setText("4");
        pic4.setImageResource(artistImageResource4);
        name4.setText(artistName4);
        minutes4.setText("Minutes");

        num5.setText("5");
        pic5.setImageResource(artistImageResource5);
        name5.setText(artistName5);
        minutes5.setText("Minutes");

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

