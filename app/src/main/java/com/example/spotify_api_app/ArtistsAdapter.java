package com.example.spotify_api_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotify_api_app.Artist;

import java.util.List;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder> {

    private List<Artist> artists;
    private Context context;  // Context is needed to start activities and show dialogs

    public ArtistsAdapter(List<Artist> artists, Context context) {
        this.artists = artists;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artists.get(position);
        holder.artistName.setText(artist.getName());
        holder.itemView.setOnClickListener(v -> showConfirmationDialog(artist));
    }

    private void showConfirmationDialog(Artist artist) {
        new AlertDialog.Builder(context)
                .setTitle("Open Spotify")
                .setMessage("Do you want to go to " + artist.getName() + "'s Spotify page?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openSpotify(artist.getSpotifyUri());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void openSpotify(String spotifyUri) {
        if (spotifyUri != null && !spotifyUri.isEmpty()) {
            // Check if the URI is actually a Spotify URI that needs conversion to a web URL
            if (spotifyUri.startsWith("spotify:artist:")) {
                spotifyUri = "https://open.spotify.com/artist/" + spotifyUri.substring("spotify:artist:".length());
            } else if (spotifyUri.startsWith("https///")) {
                // Correct the URL if it starts incorrectly
                spotifyUri = "https://" + spotifyUri.substring(8);
            }

            Log.d("ArtistsAdapter", "Attempting to open Spotify URL: " + spotifyUri);  // Log the URL here

            Uri uri = Uri.parse(spotifyUri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            // Attempt to direct this intent specifically to a web browser
            intent.addCategory(Intent.CATEGORY_BROWSABLE);

            // Check if there is an application that can handle the web intent
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Log.e("ArtistsAdapter", "Please install a web browser or check the URL.");
            }
        } else {
            Log.e("ArtistsAdapter", "Spotify URL is null or empty");
        }
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }
    public void updateData(List<Artist> newArtists) {
        artists.clear();
        artists.addAll(newArtists);
        notifyDataSetChanged();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            artistName = itemView.findViewById(android.R.id.text1);
        }
    }
}
