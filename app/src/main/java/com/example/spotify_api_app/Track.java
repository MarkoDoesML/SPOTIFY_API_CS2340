package com.example.spotify_api_app;

import java.util.List;

public class Track {
    private String name;
    private String albumCoverUrl;
    private List<Artist> artists;

    public Track() {};

    public String getName() {
        return name;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
