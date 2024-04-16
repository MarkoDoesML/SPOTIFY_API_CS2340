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
import com.google.firebase.firestore.WriteBatch;
import com.google.firestore.v1.WriteResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

    public CollectionReference getWrapDocRef() {
        return db.collection("public_wraps");
    }

    public CollectionReference getProfRef() {
        return db.collection(uid);
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
        DocumentReference userWraps = db.collection(uid).document("wrap_" + stats.get("total"));
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


        // Get a reference to the Firestore database
        public void deleteMultipleDocuments(List<String> documentIds, String collectionPath) {
            // Get a reference to the collection
            CollectionReference collectionRef = db.collection(collectionPath);

            // Create a new batch
            WriteBatch batch = db.batch();

            // Iterate through the list of document IDs and add delete operations to the batch
            for (String documentId : documentIds) {
                DocumentReference documentRef = collectionRef.document(documentId);
                batch.delete(documentRef);
            }

            // Commit the batch
            batch.commit()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Handle successful deletion
                            // (Optional: Display a success message or update UI)
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                            // (Optional: Display an error message or log the error)
                        }
                    });
        }

    public static void deleteUser(String collectionPath, JSONObject feed) {
        List<String> documentIdsToDelete = new ArrayList<>();

        // Iterate over the keys in the "feed" JSON object
        Iterator<String> keys = feed.keys();
        while (keys.hasNext()) {
            String documentId = keys.next();

            // Check if the document ID starts with the specified prefix
            if (documentId.startsWith(prefix)) {
                documentIdsToDelete.add(documentId);
            }
        }

        // Use the FirestoreUtils class to delete the filtered documents
        FirestoreUtils firestoreUtils = new FirestoreUtils();
        firestoreUtils.deleteMultipleDocuments(documentIdsToDelete, collectionPath);
    }




}
