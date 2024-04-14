package com.example.spotify_api_app;

public class Artist {
    private String name;
    private String spotifyUri;

    public Artist(String name, String spotifyUri) {
        this.name = name;
        this.spotifyUri = spotifyUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpotifyUri() {
        return spotifyUri;
    }

    public void setSpotifyUri(String spotifyUri) {
        this.spotifyUri = spotifyUri;
    }
}
