package com.example.spotify_api_app;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class JSONStorageManager {

    private static final String PREF_NAME = "MyPrefs";

    public static void saveData(Context context, String key, JSONObject data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data.toString());
        editor.apply();
    }

    public static JSONObject loadData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(key, null);
        if (jsonString != null) {
            try {
                return new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject(); // Return empty JSONObject if data not found
    }

    public static void clearData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    private static void deleteAssociatedFiles(Context context, String key) {
        // Get the directory where SharedPreferences files are stored
        File sharedPrefsDir = new File(context.getApplicationInfo().dataDir, "shared_prefs");

        // Get list of all SharedPreferences files
        File[] sharedPrefsFiles = sharedPrefsDir.listFiles();

        if (sharedPrefsFiles != null) {
            // Iterate over each file and delete if associated with the given key
            for (File file : sharedPrefsFiles) {
                if (file.getName().startsWith(PREF_NAME + key)) {
                    file.delete();
                }
            }
        }
    }
}
