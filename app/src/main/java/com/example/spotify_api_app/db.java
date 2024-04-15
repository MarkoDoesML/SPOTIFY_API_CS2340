package com.example.spotify_api_app;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Request;

public class db {
    // Database where the spotify data will be stored
    @SuppressLint("StaticFieldLeak")

    // make increase wrapped number
    public static String uid;

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public db(String uid) {
        this.uid = uid;
    }

    public static void createProfile() throws JSONException{
        DocumentReference doc = db.collection(uid).document("number_of_wraps");
        Map<String, Integer> wraps = new HashMap<>();
        wraps.put("total", 0);
        wraps.put("public", 0);
        wraps.put("private", 0);

        doc.set(wraps)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("db", "Document added to collection successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("db", "Error adding document to collection", e);
                    }
                });
    }

    public static void addWrapped(JSONObject wrapped, int total) throws JSONException {
        DocumentReference doc = db.collection(uid).document("wraps");

    }

    public DocumentReference getDocRef(String name) {
        return db.collection(uid).document(name);
    }

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
        // Put uri into map
        userProfile.put("uri", jsonObject.getString("uri"));
        userProfile.put("last_online", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        DocumentReference doc = db.collection(uid).document("profile_info");

        doc.set(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("db", "Document added to collection successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("db", "Error adding document to collection", e);
                    }
                });
    }

    public static void storeWrapped(Map<String, Object> wrapped, Map<String, Integer> stats, boolean view) {
        DocumentReference userWraps = db.collection(uid).document("wraps").collection("wrap_" + stats.get("total")).document(("wrap_" + stats.get("total")));
        DocumentReference userInfo = db.collection(uid).document("number_of_wraps");
        DocumentReference allWraps = db.collection("public_wraps").document(uid + "_wrap_" + stats.get("public"));

        userWraps.set(wrapped)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("db", "Document added to collection successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("db", "Error adding document to collection", e);
                    }
                });

        userInfo.set(stats)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("db", "Document added to collection successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("db", "Error adding document to collection", e);
                    }
                });

        if(view) {
            allWraps.set(wrapped)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("db", "Document added to collection successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("db", "Error adding document to collection", e);
                        }
                    });
        }

    }

//
//    public static void storeTopArtists(JSONObject jsonObject) throws JSONException {
//        // Check if passed in JSONObject is null
//        String username = user.getEmail();
//
//        // Check if passed in JSONObject is null
//        if (jsonObject == null) { Log.d("db", "JSONObject is null when trying to store user profile data."); return; }
//        // Create a map to store user's top artist information
//        Map<String, Object> userTopArtists = new HashMap<>();
//
//        // Iterate through top artists to get top 5 artists
//        for (int i = 0; i < 5 && i < jsonObject.getJSONArray("items").length(); i++) {
//            // Create inner map for specific artist
//            Map<String, Object> topArtist = new HashMap<>();
//
//            // Put artists name in inner map
//            topArtist.put("name", jsonObject.getJSONArray("items").getJSONObject(i).getString("name"));
//
//            // Put artists image (if applicable) in inner map
//            if (jsonObject.getJSONArray("items").getJSONObject(i).getJSONArray("images").length() > 0) {
//                topArtist.put("image", jsonObject.getJSONArray("items").getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url"));
//            } else {
//                topArtist.put("image", null);
//            }
//
//            // Put specific artist into top artist map
//            userTopArtists.put("artist" + (i + 1), topArtist);
//        }
//
//        DocumentReference doc = db.collection(username).document("wraps");
//
//        // Add user top artist information to database
//        doc.set(userTopArtists)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("db", "Document added to collection successfully!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("db", "Error adding document to collection", e);
//                    }
//                });
//    }
//
//
//
//    public static void storeTopTracks(JSONObject jsonObject) throws JSONException {
//        // Check if passed in JSONObject is null
//        if (jsonObject == null) { Log.d("db", "JSONObject is null when trying to store user's top track data."); return; }
//
//        // Create a map to store user's top track information
//        Map<String, Object> userTopTracks = new HashMap<>();
//
//        // Iterate through top tracks to get top 5 tracks
//        for (int i = 0; i < 5 && i < jsonObject.getJSONArray("items").length(); i++) {
//            // Create inner map to store specific track's information
//            Map<String, Object> topTrack = new HashMap<>();
//
//            // Put track's name into inner map
//            topTrack.put("name", jsonObject.getJSONArray("items").getJSONObject(i).getString("name"));
//
//            // Put track's image, if applicable, into inner map
//            if (jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("album").getJSONArray("images").length() > 0) {
//                topTrack.put("image", jsonObject.getJSONArray("items").getJSONObject(i).getJSONObject("album").getJSONArray("images")
//                        .getJSONObject(0).getString("url"));
//            } else {
//                topTrack.put("image", null);
//            }
//
//            // Put track's artists into inner map
//            List<String> trackArtists = new ArrayList<>();
//            for (int j = 0; j < jsonObject.getJSONArray("items").getJSONObject(i).getJSONArray("artists").length(); j++) {
//                trackArtists.add(j, jsonObject.getJSONArray("items").getJSONObject(i).getJSONArray("artists").getJSONObject(j).getString("name"));
//            }
//            topTrack.put("artists", trackArtists);
//
//            // Put specific track's information in the top track information
//            userTopTracks.put("track" + (i + 1), topTrack);
//        }
//
//        // Add user top track information to database
//        db.collection("users")
//                .add(userTopTracks)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("db", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("db", "Error adding document", e);
//                    }
//                });
//    }



}
