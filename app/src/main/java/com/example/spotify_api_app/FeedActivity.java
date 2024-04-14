package com.example.spotify_api_app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WrappedAdapter adapter;
    private List<WrappedItem> wrappedItemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        recyclerView = findViewById(R.id.recyclerView_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        wrappedItemList = new ArrayList<>();
        adapter = new WrappedAdapter(this, wrappedItemList);
        recyclerView.setAdapter(adapter);

        // Setup FloatingActionButton to show recommendations dialog
        FloatingActionButton fabRecommend = findViewById(R.id.recommend_artist); // Ensure this ID matches your layout
        fabRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecommendationsDialog();
            }
        });

        // Logic for Bottom Nav Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_feed);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), MainProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
    }

    private List<Artist> parseArtistsFromResponse(JSONObject jsonResponse) {
        List<Artist> artists = new ArrayList<>();
        try {
            JSONArray tracks = jsonResponse.getJSONArray("tracks");
            for (int i = 0; i < tracks.length(); i++) {
                JSONObject track = tracks.getJSONObject(i);
                JSONArray artistsArray = track.getJSONArray("artists");
                for (int j = 0; j < artistsArray.length(); j++) {
                    JSONObject artistJson = artistsArray.getJSONObject(j);
                    String name = artistJson.getString("name");
                    String spotifyUri = artistJson.optString("uri", "");  // Ensure this key matches the JSON response
                    artists.add(new Artist(name, spotifyUri));
                }
            }
        } catch (JSONException e) {
            Log.e("JSON Parsing", "Error parsing Spotify recommendations", e);
        }
        return artists;
    }

    private void showRecommendationsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.recommendations); // Make sure this layout has a RecyclerView

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView_recommendations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArtistsAdapter adapter = new ArtistsAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Fetch top artists and then fetch recommendations based on these artists
        api.fetchTopArtists(new api.JsonCallback() {
            @Override
            public void onJsonResponseReceived(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    String seedArtists = "";
                    for (int i = 0; i < items.length(); i++) {
                        if (i > 0) seedArtists += ",";
                        seedArtists += items.getJSONObject(i).getString("id");
                    }
                    fetchRecommendations(seedArtists, "", "", adapter);
                } catch (JSONException e) {
                    Log.e("Spotify API", "Failed to parse top artists", e);
                }
            }
        });

        dialog.show();
    }

    private void fetchRecommendations(String seedArtists, String seedGenres, String seedTracks, ArtistsAdapter adapter) {
        api.getRecommendations(seedArtists, seedGenres, seedTracks, new api.JsonCallback() {
            @Override
            public void onJsonResponseReceived(JSONObject response) {
                if (response != null) {
                    List<Artist> recommendedArtists = parseArtistsFromResponse(response);
                    runOnUiThread(() -> adapter.updateData(recommendedArtists));
                } else {
                    Log.e("Spotify API", "No recommendations found.");
                }
            }
        });
    }


}
