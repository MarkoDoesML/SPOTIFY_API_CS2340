package com.example.spotify_api_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ArtistsActivity extends AppCompatActivity {
    TextView num1, num2, num3, num4, num5, name1, name2, name3, name4, name5, minutes1, minutes2, minutes3, minutes4, minutes5;
    ImageView pic1, pic2, pic3, pic4, pic5;
    String artistName1, artistName2, artistName3, artistName4, artistName5, artistImage1, artistImage2, artistImage3, artistImage4, artistImage5;
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

        // Get artists info from wrapped
        Intent i = getIntent();
        HashMap<String, Object> artists = (HashMap<String, Object>) i.getSerializableExtra("artists");

        // Example input values (replace these with your actual input)
        artistName1 = ((HashMap<String, Object>) artists.get("artist1")).get("name").toString();
        artistImage1 = ((HashMap<String, Object>) artists.get("artist1")).get("image").toString();
        artistName2 = ((HashMap<String, Object>) artists.get("artist2")).get("name").toString();
        artistImage2 = ((HashMap<String, Object>) artists.get("artist2")).get("image").toString();
        artistName3 = ((HashMap<String, Object>) artists.get("artist3")).get("name").toString();;
        artistImage3 = ((HashMap<String, Object>) artists.get("artist3")).get("image").toString();
        artistName4 = ((HashMap<String, Object>) artists.get("artist4")).get("name").toString();
        artistImage4 = ((HashMap<String, Object>) artists.get("artist4")).get("image").toString();
        artistName5 = ((HashMap<String, Object>) artists.get("artist5")).get("name").toString();
        artistImage5 = ((HashMap<String, Object>) artists.get("artist5")).get("image").toString();

        // Set values to the views
        num1.setText("1");
        Picasso.get().load(artistImage1).into(pic1);
        name1.setText(artistName1);
        minutes1.setText("Minutes");

        num2.setText("2");
        Picasso.get().load(artistImage2).into(pic2);
        name2.setText(artistName2);
        minutes2.setText("Minutes");

        num3.setText("3");
        Picasso.get().load(artistImage3).into(pic3);
        name3.setText(artistName3);
        minutes3.setText("Minutes");

        num4.setText("4");
        Picasso.get().load(artistImage4).into(pic4);
        name4.setText(artistName4);
        minutes4.setText("Minutes");

        num5.setText("5");
        Picasso.get().load(artistImage5).into(pic5);
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
