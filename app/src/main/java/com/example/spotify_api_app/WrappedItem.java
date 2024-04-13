package com.example.spotify_api_app;

import java.util.List;

public class WrappedItem {
    private String username;
    private String date;

    private List<Artist> artists;
    private List<Track> tracks;

    public WrappedItem(String username, String date, List<Artist> artists, List<Track> tracks) {
        this.username = username;
        this.date = date;
        this.artists = artists;
        this.tracks = tracks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}

