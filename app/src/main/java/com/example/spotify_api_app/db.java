package com.example.spotify_api_app;


import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class db {
    // Database where the spotify data will be stored
    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void storeUserProfile(JSONObject jsonObject) throws JSONException {
        // Check if passed in JSONObject is null
        if (jsonObject == null) { Log.d("db", "JSONObject is null when trying to store user profile data."); return; }

        // Create map to store user profile information
        Map<String, Object> userProfile = new HashMap<>();

        // Put display name into map
        userProfile.put("display_name", jsonObject.getString("display_name"));

        // Put image url in map if user has profile picture, else put null
        if (jsonObject.getJSONArray("images").length() > 0) {
            userProfile.put("image_url", jsonObject.getJSONArray("images").getJSONObject(0).getString("url"));
        } else {
            userProfile.put("image_url", null);
        }

        // Add user profile information to database
        db.collection("users")
                .add(userProfile)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("db", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db", "Error adding document", e);
                    }
                });
    }

    public static void storeTopArtists(JSONObject jsonObject) throws JSONException {
        // Check if passed in JSONObject is null
        if (jsonObject == null) { Log.d("db", "JSONObject is null when trying to store user's top artist data."); return; }

        // Create a map to store user's top artist information
        Map<String, Object> userTopArtists = new HashMap<>();

        // Iterate through top artists to get top 5 artists
        for (int i = 0; i < 5 && i < jsonObject.getJSONArray("items").length(); i++) {
            // Create inner map for specific artist
            Map<String, Object> topArtist = new HashMap<>();

            // Put artists name in inner map
            topArtist.put("name", jsonObject.getJSONArray("items").getJSONObject(i).getString("name"));

            // Put artists image (if applicable) in inner map
            if (jsonObject.getJSONArray("items").getJSONObject(i).getJSONArray("images").length() > 0) {
                topArtist.put("image", jsonObject.getJSONArray("items").getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url"));
            } else {
                topArtist.put("image", null);
            }

            // Put specific artist into top artist map
            userTopArtists.put("artist" + (i + 1), topArtist);
        }

        // Add user top artist information to database
        db.collection("users")
                .add(userTopArtists)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("db", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db", "Error adding document", e);
                    }
                });
    }

    public static void storeTopTracks(JSONObject jsonObject) throws JSONException {
        // Check if passed in JSONObject is null
        if (jsonObject == null) { Log.d("db", "JSONObject is null when trying to store user's top track data."); return; }

        // Create a map to store user's top track information
        Map<String, Object> userTopTracks = new HashMap<>();

        // Iterate through top tracks to get top 5 tracks
        for (int i = 0; i < 5 && i < jsonObject.getJSONArray("items").length(); i++) {
            // Create inner map to store specific track's information
            Map<String, Object> topTrack = new HashMap<>();

            // Put track's name into inner map
            topTrack.put("name", jsonObject.getJSONArray("items").getJSONObject(i).getString("name"));

            // Put track's image, if applicable, into inner map
            if (jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("album").getJSONArray("images").length() > 0) {
                topTrack.put("image", jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("album").getJSONArray("images")
                        .getJSONObject(0).getString("url"));
            } else {
                topTrack.put("image", null);
            }

            // Put track's artists into inner map
            List<String> trackArtists = new ArrayList<>();
            for (int j = 0; j < jsonObject.getJSONArray("items").getJSONObject(i).getJSONArray("artists").length(); j++) {
                trackArtists.add(j, jsonObject.getJSONArray("items").getJSONObject(i).getJSONArray("artists").getJSONObject(j).getString("name"));
            }
            topTrack.put("artists", trackArtists);

            // Put specific track's information in the top track information
            userTopTracks.put("track" + (i + 1), topTrack);
        }

        // Add user top track information to database
        db.collection("users")
                .add(userTopTracks)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("db", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db", "Error adding document", e);
                    }
                });
    }
}
