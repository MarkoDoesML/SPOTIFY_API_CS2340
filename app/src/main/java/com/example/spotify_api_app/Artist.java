package com.example.spotify_api_app;

public class Artist {
    private String name;
    private String image_url;

    public Artist() {};

    public String getImage_url() {
        return image_url;
    }

    public String getName() {
        return name;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setName(String name) {
        this.name = name;
    }
}
