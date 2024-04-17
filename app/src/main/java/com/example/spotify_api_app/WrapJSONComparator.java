package com.example.spotify_api_app;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class WrapJSONComparator implements Comparator<JSONObject> {
    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        try {
            String d1 = o1.get("date").toString();
            String d2 = o2.get("date").toString();
            return d1.compareTo(d2);
        } catch (JSONException e) {
            Log.d("JSONComparator", "Something went wrong whilst comparing JSONObjects: " + e);
        }
        return 0;
    }
}
