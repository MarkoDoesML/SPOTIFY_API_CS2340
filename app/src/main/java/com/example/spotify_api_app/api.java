package com.example.spotify_api_app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;
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

    /**
     * Get authentication request
     *
     * @param type the type of the request
     * @return the authentication request
     */
    public static AuthorizationRequest getAuth(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-read-email" }) // <--- Change the scope of your requested token here
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


}
