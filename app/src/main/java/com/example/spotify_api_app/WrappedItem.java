package com.example.spotify_api_app;

import org.json.JSONObject;

import java.util.HashMap;

public class WrappedItem {
    private String username;
    private String date;
    private HashMap<String, Object> wrapInfo;

    public WrappedItem(String username, String date, HashMap<String, Object> wrapInfo) {
        this.username = username;
        this.date = date;
        this.wrapInfo = wrapInfo;
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

    public HashMap<String, Object> getWrapInfo() {
        return wrapInfo;
    }

    public void setWrapInfo(HashMap<String, Object> wrapInfo) {
        this.wrapInfo = wrapInfo;
    }
}

