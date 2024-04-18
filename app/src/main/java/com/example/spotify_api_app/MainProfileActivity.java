package com.example.spotify_api_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.google.firebase.auth.AuthResult;
import com.squareup.picasso.Picasso;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.auth.FirebaseUser;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import android.content.SharedPreferences;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

import com.google.firebase.auth.FirebaseAuth;
import android.content.DialogInterface;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MainProfileActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    String duration;
    boolean isPublic;
    int START_POPUP_ACTIVITY = 1;
    Button btn_wrapped;
    ImageButton btn_logout;
  
    RecyclerView recyclerView;
    WrappedAdapter wrappedAdapter;
    List<WrappedItem> wrappedItemList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private AccessTokenData accessTokenData;
    private String mAccessToken, mAccessCode;
    TextView usernameTextView;
    String username, link, uri, uid;
    int private_wraps, public_wraps;
    CompletableFuture<DocumentSnapshot> output;
    Map<String, Integer> stats;
    private ImageView profileImage;
    JSONObject my_feed, feed;
    public db database;
    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);

        SharedPreferences auths = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        uid = auths.getString("uid", "junk");

        sharedPreferences = getSharedPreferences("AccessTokenData", MODE_PRIVATE);
        String json = sharedPreferences.getString("accessTokenData", null);
        TextView usernameTextView = findViewById(R.id.username);

        // Deserialize the JSON string using Gson
        Gson gson = new Gson();
        accessTokenData = gson.fromJson(json, AccessTokenData.class);

        mAccessCode = accessTokenData.getAccessCode();
        mAccessToken = accessTokenData.getAccessToken();

        btn_wrapped = findViewById(R.id.btn_wrapped_page);
        recyclerView = findViewById(R.id.wrappedList);
        btn_logout = findViewById(R.id.logout);

        profileImage = findViewById(R.id.profileImage);

        Button btn_change_login_info = findViewById(R.id.btn_change_login_info);
        ImageButton btn_delete_account = findViewById(R.id.btn_delete_account);


        wrappedAdapter = new WrappedAdapter(this, wrappedItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wrappedAdapter);

        database = new db(uid);

        String img_url = "https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da8463bcdace67f79859e30a17fa";
        JSONObject info = JSONStorageManager.loadData(getApplicationContext(), "profile_info");
        try {
//            wrapped_number = info.getInt("wraps_created");
            username = info.getString("display_name");
            link = info.getJSONObject("external_urls").getString("spotify");
            String htmlLink = "<a href=\"" + link + "\">" + username + "</a>";
            usernameTextView.setText(Html.fromHtml(htmlLink));
            uri = info.getString("uri");
            usernameTextView.setMovementMethod(LinkMovementMethod.getInstance()); // Make links clickable

// Set OnClickListener to handle link click
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle the exception, such as setting a default text or showing an error message
        }

        try {
            if (info.getJSONArray("images").length() > 0) {
                img_url = info.getJSONArray("images").getJSONObject(1).getString("url");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle the exception, such as setting a default value for img_url or showing an error message
        }


        Picasso.get().load(img_url).into(profileImage);

        JSONObject num = JSONStorageManager.loadData(getApplicationContext(), "number_of_wraps");
        my_feed = JSONStorageManager.loadData(getApplicationContext(), "my_feed");
        feed = JSONStorageManager.loadData(getApplicationContext(), "feed");

        ArrayList<JSONObject> wraps = new ArrayList<>();
        Iterator<String> wrapKeys = my_feed.keys();

        while(wrapKeys.hasNext()) {
            try {
                String key = wrapKeys.next();
                if (my_feed.get(key) instanceof JSONObject) {
                    wraps.add((JSONObject) my_feed.get(key));
                }
            } catch (JSONException e) {
                Log.d("JSONIterator", "Couldn't iterator through personal wraps: " + e);
            }
        }

        wraps.sort(new WrapJSONComparator());

        for (JSONObject wrap : wraps) {
            try {
                wrappedAdapter.addItem(wrap.get("username").toString(), wrap.get("date").toString(), new Gson().fromJson(wrap.toString(), HashMap.class));
            } catch (JSONException e) {
                Log.d("MyFeedAdding", "Could not add wrap to my feed: " + e);
            }
        }


// Initialize a Map to store the converted values
        stats = new HashMap<>();

// Iterate through the keys of the JSONObject
        Iterator<String> keys = num.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            // Extract the value as Object
            Object value = num.opt(key);
            if (value instanceof Integer) {
                // If the value is already an Integer, add it directly to the map
                stats.put(key, (Integer) value);
            } else if (value instanceof Double) {
                // If the value is a Double, cast it to int and add it to the map
                stats.put(key, ((Double) value).intValue());
            } else {
                // Handle other types if necessary
            }
        }


        usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpotify(uri);
            }
        });

        btn_wrapped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainProfileActivity.this, DurationPopUpActivity.class);
                startActivityForResult(i, 1);

            }
        });
        // Set onClickListener for Change Login Information
        btn_change_login_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeLoginIntent = new Intent(MainProfileActivity.this, changeLoginInfoActivity.class);
                startActivity(changeLoginIntent);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout(null);
            }
        });
        //test to commit again
        // Set up onClickListener for the Delete Account button
        btn_delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });

        // Logic for Bottom Nav Bar
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_feed) {
                    startActivity(new Intent(getApplicationContext(), FeedActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
    }

    public void openSpotify(String spotifyUri) {
        if (spotifyUri != null && !spotifyUri.isEmpty()) {
            // Check if the URI is actually a Spotify URI that needs conversion to a web URL
            if (spotifyUri.startsWith("spotify:artist:")) {
                spotifyUri = "https://open.spotify.com/artist/" + spotifyUri.substring("spotify:artist:".length());
            } else if (spotifyUri.startsWith("https///")) {
                // Correct the URL if it starts incorrectly
                spotifyUri = "https://" + spotifyUri.substring(8);
            }

            Log.d("ArtistsAdapter", "Attempting to open Spotify URL: " + spotifyUri);  // Log the URL here

            Uri uri = Uri.parse(spotifyUri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            // Attempt to direct this intent specifically to a web browser
            intent.addCategory(Intent.CATEGORY_BROWSABLE);

            // Check if there is an application that can handle the web intent
            if (intent.resolveActivity(this.getPackageManager()) != null) {
                this.startActivity(intent);
            } else {
                Log.e("ArtistsAdapter", "Please install a web browser or check the URL.");
            }
        } else {
            Log.e("ArtistsAdapter", "Spotify URL is null or empty");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == START_POPUP_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK){
                String time = data.getStringExtra("duration");
                boolean view = data.getBooleanExtra("public", false);
                Log.d("Duration", "Duration: " + duration);
                Log.d("Visibility", "Public: " + isPublic);
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

//                JSONObject wrapped_json = JSONStorageManager.loadData(getApplicationContext(), time);
//
//                Map<String, Object> wrapped = new Gson().fromJson(wrapped_json.toString(), Map.class);


                Map<String, Object> wrapped = api.makeWrapped(mAccessToken, time);

                if ((Boolean) wrapped.get("error")) {
                    navigateToLoginRefresh();
                    return;
                }

                wrapped.remove("error");

                wrapped.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                wrapped.put("public", view);


                int currentTotal = stats.get("total");
                stats.put("total", currentTotal + 1);

                if (view) {
                    currentTotal = stats.get("public");
                    stats.put("public", currentTotal + 1);
                } else {
                    currentTotal = stats.get("private");
                    stats.put("private", currentTotal + 1);
                }

                JSONStorageManager.saveData(getApplicationContext(), "number_of_wraps", new JSONObject(stats));

                wrapped.put("uri", uri);
                wrapped.put("username", username);
                wrapped.put("duration", time);
                wrapped.put("number", stats.get("total"));

                my_feed = JSONStorageManager.loadData(getApplicationContext(), "my_feed");
                feed = JSONStorageManager.loadData(getApplicationContext(), "feed");

                try {
                    my_feed.put("wrap_" + stats.get("total"), new JSONObject(wrapped));
                    if (view) {
                        feed.put(database.getUid() + "_wrap_" + stats.get("public"), new JSONObject(wrapped));
                    }
                } catch (JSONException e) {
                    System.out.println("error");
                }

                JSONStorageManager.saveData(getApplicationContext(), "my_feed", my_feed);
                JSONStorageManager.saveData(getApplicationContext(), "feed", feed);

                //TODO: use json content to fill ui

                wrappedAdapter.addItem(username, wrapped.get("date").toString(), (HashMap<String, Object>) wrapped);
                recyclerView.scrollToPosition(0);
                db.storeWrapped(wrapped, stats, view);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Do nothing
            }
        }
    }

    public void logoutUser(View view) {
        performLogout(view);
    }

    private void navigateToLoginRefresh() {
        Intent intent = new Intent(this, wSpotify.class);
        startActivity(intent);
        finish();
    }

    public void clearAllSharedPreferences(Context context) {
        // Get list of all SharedPreferences files
        File sharedPrefsDir = new File(context.getApplicationInfo().dataDir, "shared_prefs");
        File[] sharedPrefsFiles = sharedPrefsDir.listFiles();

        if (sharedPrefsFiles != null) {
            // Iterate over each file and clear SharedPreferences
            for (File file : sharedPrefsFiles) {
                String fileName = file.getName().replace(".xml", "");
                SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
            }
        }
    }

    private void performLogout(View view) {
//        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();

        clearAllSharedPreferences(this);
        JSONStorageManager.clearData(this,"profile_info");
        JSONStorageManager.clearData(this, "number_of_wraps");
        JSONStorageManager.clearData(this, "my_feed");
        JSONStorageManager.clearData(this, "feed");

        Intent intent = new Intent(MainProfileActivity.this, LoginActivity.class);
        intent.putExtra("logout", true);
        startActivity(intent);
        finish();
    }

   private void deleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Account Deletion")
                .setMessage("Are you sure you want to delete your account? This cannot be undone.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences auths = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                        String email = auths.getString("username", "junk");
                        String password = auths.getString("password", "junk");
                        FirebaseAuth mAuth= FirebaseAuth.getInstance();

                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        mAuth.getCurrentUser().delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("deleteUser()", "User account deleted.");
                                                            feed = JSONStorageManager.loadData(getApplicationContext(), "feed");
                                                            database.deleteUser("public_wraps", feed);
                                                            performLogout(null);
                                                            finish();
                                                        }
                                                    }
                                                });
                                    }
                                });



                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}