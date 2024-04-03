package com.example.spotify_api_app;

import com.google.gson.annotations.SerializedName;

public class AccessTokenData {
    @SerializedName("access_code")
    private String accessCode;

    @SerializedName("access_token")
    private String accessToken;

    public AccessTokenData(String accessCode, String accessToken) {
        this.accessCode = accessCode;
        this.accessToken = accessToken;
    }

    // Getters and setters

    public String getAccessCode() {
        return accessCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
