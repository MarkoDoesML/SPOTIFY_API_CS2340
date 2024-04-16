package com.example.spotify_api_app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.*;


public class api {
    public static final String CLIENT_ID = "4b436312ac61498ba5f5439995576e4c";
    public static final String CLIENT_SECRET= "f05221d62c9245e1abef8926f681ad4c";
    public static final String REDIRECT_URI = "com.example.spotify-api-app://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    private static Call mCall;

    // Holder for access token
    private static String currentAccessToken;

    /**
     * Get authentication request
     *
     * @param type the type of the request
     * @return the authentication request
     */
   public static AuthorizationRequest getAuth(AuthorizationResponse.Type type) {
    return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
            .setShowDialog(false)
            .setScopes(new String[] { "user-read-email", "user-top-read" }) // Keep these scopes from the recommended_artist branch
            .setCampaign("your-campaign-token")
            .build();
}



    public static void login(Activity activity) {
        final AuthorizationRequest request = getAuth(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(activity, AUTH_CODE_REQUEST_CODE, request);
    }

    public interface TokenCallback {
        void onTokenReceived(String token);
    }

    // method to return the access token without any parameters
    public static String getAccessToken() {
        return currentAccessToken;
    }

    public static void getAccessToken(String code, TokenCallback callback) {
        String url = "https://accounts.spotify.com/api/token";
        String userCredentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes());

        // Request body
        RequestBody requestBody = new FormBody.Builder()
                .add("code", code)
                .add("redirect_uri", REDIRECT_URI)
                .add("grant_type", "authorization_code")
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", basicAuth)
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                callback.onTokenReceived(null); // Notify callback with null token
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    //Variable holder for access token
                    currentAccessToken = jsonObject.getString("access_token");
                    String token = jsonObject.getString("access_token");
                    callback.onTokenReceived(token); // Notify callback with token
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    callback.onTokenReceived(null); // Notify callback with null token
                }
            }
        });
    }

    public static String getToken(String code) {
        // Create a CountDownLatch with initial count 1
        CountDownLatch latch = new CountDownLatch(1);

        // Variable to store the token
        AtomicReference<String> tokenRef = new AtomicReference<>(null);

        // Call getAccessToken() with a callback
        getAccessToken(code, new TokenCallback() {
            @Override
            public void onTokenReceived(String token) {
                // Store the token
                tokenRef.set(token);
                // Decrease the count of the latch to 0, indicating the completion of the asynchronous call
                latch.countDown();
            }
        });

        try {
            // Wait for the asynchronous call to complete
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            // Handle interruption
            return null;
        }

        // Return the token obtained from the callback
        return tokenRef.get();
    }

    private static Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    public static void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    public interface JsonCallback {
        void onJsonResponseReceived(JSONObject response);
    }

    public static void executeRequest(Request request, JsonCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                callback.onJsonResponseReceived(null); // Notify callback with null token
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    callback.onJsonResponseReceived(jsonObject); // Notify callback with token
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    callback.onJsonResponseReceived(null); // Notify callback with null token
                }
            }
        });
    }

    public static JSONObject makeRequest(Request request) {
        // Create a CountDownLatch with initial count 1
        CountDownLatch latch = new CountDownLatch(1);

        // Variable to store the JSON object
        AtomicReference<JSONObject> jsonObjectRef = new AtomicReference<>(null);

        // Call executeRequest() with a callback
        executeRequest(request, new JsonCallback() {
            @Override
            public void onJsonResponseReceived(JSONObject jsonObject) {
                // Store the JSON object
                jsonObjectRef.set(jsonObject);
                // Decrease the count of the latch to 0, indicating the completion of the asynchronous call
                latch.countDown();
            }
        });

        try {
            // Wait for the asynchronous call to complete
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            // Handle interruption
            return null;
        }

        // Return the JSON object obtained from the callback
        return jsonObjectRef.get();
    }

    //Get recommendations method
    public static void getRecommendations(String seedArtists, String seedGenres, String seedTracks, JsonCallback callback) {
        String url = "https://api.spotify.com/v1/recommendations";

        // Prepare URL with query parameters
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (!seedArtists.isEmpty()) urlBuilder.addQueryParameter("seed_artists", seedArtists);
        if (!seedGenres.isEmpty()) urlBuilder.addQueryParameter("seed_genres", seedGenres);
        if (!seedTracks.isEmpty()) urlBuilder.addQueryParameter("seed_tracks", seedTracks);
        String requestUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("Authorization", "Bearer " + getAccessToken()) // Ensure you have a method to get a fresh token
                .build();

        executeRequest(request, callback);
    }

    public static void fetchTopArtists(JsonCallback callback) {
        String url = "https://api.spotify.com/v1/me/top/artists?limit=5"; // Fetch top 5 artists

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + getAccessToken())
                .build();

        executeRequest(request, callback);

    }


    public static Map<String, Object> addToWrapped(JSONObject json, String type, Map<String, Object> wrap) throws JSONException{

        if (type == "artist") {
            Map<String, Object> userTopArtists = new HashMap<>();

            for (int i = 0; i < 5 && i < json.getJSONArray("items").length(); i++) {
                // Create inner map for specific artist
                Map<String, Object> topArtist = new HashMap<>();

                // Put artists name in inner map
                topArtist.put("name", json.getJSONArray("items").getJSONObject(i).getString("name"));

                // Put artists image (if applicable) in inner map
                if (json.getJSONArray("items").getJSONObject(i).getJSONArray("images").length() > 0) {
                    topArtist.put("image", json.getJSONArray("items").getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url"));
                } else {
                    topArtist.put("image", null);
                }

                // Put specific artist into top artist map
                userTopArtists.put("artist" + (i + 1), topArtist);

            }
            wrap.put("artists", userTopArtists);
        } else {
            Map<String, Object> userTopTracks = new HashMap<>();



            // Iterate through top tracks to get top 5 tracks
            for (int i = 0; i < 5 && i < json.getJSONArray("items").length(); i++) {
                // Create inner map to store specific track's information
                Map<String, Object> topTrack = new HashMap<>();

                // Put track's name into inner map
                topTrack.put("name", json.getJSONArray("items").getJSONObject(i).getString("name"));

                // Put track's image, if applicable, into inner map
                if (json.getJSONArray("items").getJSONObject(i).getJSONObject("album").getJSONArray("images").length() > 0) {
                    topTrack.put("image", json.getJSONArray("items").getJSONObject(i).getJSONObject("album").getJSONArray("images")
                            .getJSONObject(0).getString("url"));
                } else {
                    topTrack.put("image", null);
                }

                // Put track's artists into inner map
                List<String> trackArtists = new ArrayList<>();
                for (int j = 0; j < json.getJSONArray("items").getJSONObject(i).getJSONArray("artists").length(); j++) {
                    trackArtists.add(j, json.getJSONArray("items").getJSONObject(i).getJSONArray("artists").getJSONObject(j).getString("name"));
                }
                topTrack.put("artists", trackArtists);

                // Put specific track's information in the top track information
                userTopTracks.put("track" + (i + 1), topTrack);

            }
            Map<String, Integer> albumCounts = new HashMap<>();
            Map<String, Object> userTopAlbums = new HashMap<>();
            for (int i = 0; i < 50 && i < json.getJSONArray("items").length(); i++) {
                // Create inner map to store specific track's information
                Map<String, Object> topTrack = new HashMap<>();
                JSONObject item = json.getJSONArray("items").getJSONObject(i).getJSONObject("album");
                String albumName = item.getString("name");
                String albumImage = item.getJSONArray("images").getJSONObject(0).getString("url");

                topTrack.put("name", albumName);
                topTrack.put("image", albumImage);

                albumCounts.put(albumName, albumCounts.getOrDefault(albumName, 0) + 1);
                userTopAlbums.put(albumName, topTrack);
                // Put track's name into inner map
            }

            List<Map.Entry<String, Integer>> sortedAlbums = new ArrayList<>(albumCounts.entrySet());
            sortedAlbums.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            // Extract top 5 albums
            Map<String, Object> top5Albums = new HashMap<>();
            for (int i = 0; i < Math.min(5, sortedAlbums.size()); i++) {
                String albumName = sortedAlbums.get(i).getKey();
                top5Albums.put("album" + (i + 1), userTopAlbums.get(albumName));
            }

            wrap.put("tracks", userTopTracks);
            wrap.put("albums", top5Albums);
        }
        return wrap;
    }


    public static Map<String, Object> makeWrapped(String token, String term) {
        Map<String, Object> wrapped = new HashMap<>();
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?time_range=" + term + "&limit=5")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        try {
            JSONObject output = makeRequest(request);

            if (output.has("error")) {
                wrapped.put("error", true);
            } else {
                wrapped.put("error", false);
            }

            wrapped = addToWrapped(output, "artist", wrapped);
        } catch (Exception e) {
            Log.d("JSON", "Failed to parse data: " + e);
        }


        final Request r = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?time_range=" + term + "&limit=50")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        try {
            JSONObject output = makeRequest(r);
            wrapped = addToWrapped(output, "tracks", wrapped);
        } catch (Exception e) {
            Log.d("JSON", "Failed to parse data: " + e);
        }
        return wrapped;
    }
}



