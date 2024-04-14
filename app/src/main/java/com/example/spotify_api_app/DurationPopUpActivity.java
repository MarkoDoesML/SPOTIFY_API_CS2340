package com.example.spotify_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DurationPopUpActivity extends AppCompatActivity {
    private RadioGroup durationButtons, publicButtons;
    private Button generateButton, cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up_activity_duration);
        // RadioGroup for duration buttons
        durationButtons = findViewById(R.id.DurationButtons);
        // RadioGroup for visibility buttons
        publicButtons = findViewById(R.id.PublicButtons);
        // Button for generating wrap
        generateButton = findViewById(R.id.GenerateButton);
        // Button for canceling generation
        cancelButton = findViewById(R.id.CancelButton);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the checked duration and visibility buttons' IDs
                int checkedDurationButtonId = durationButtons.getCheckedRadioButtonId();
                int checkedVisibilityButtonId = publicButtons.getCheckedRadioButtonId();

                // If either group has no option selected, the returned ID is -1 and will be prompted to select an
                // option for both fields
                if (checkedDurationButtonId == -1) {
                    Toast.makeText(DurationPopUpActivity.this, "Please check a duration.", Toast.LENGTH_SHORT).show();
                } else if (checkedVisibilityButtonId == -1) {
                    Toast.makeText(DurationPopUpActivity.this, "Please check a visibility.", Toast.LENGTH_SHORT).show();
                } else {
                    // Make an intent to send data back to MainProfileActivity.java
                    Intent i = new Intent();

                    // Get the checked duration and visibility buttons
                    RadioButton checkedDurationButton = findViewById(checkedDurationButtonId);
                    RadioButton checkedVisibilityButton = findViewById(checkedVisibilityButtonId);

                    // Return duration according to checked duration button
                    if (checkedDurationButton.getText().toString().equals("3 Months")) {
                        i.putExtra("duration", "3Months");
                    } else if (checkedDurationButton.getText().toString().equals("6 Months")) {
                        i.putExtra("duration", "6Months");
                    } else {
                        i.putExtra("duration", "1Year");
                    }

                    // Return visibility according to checked visibility button
                    // Is private by default
                    if (checkedVisibilityButton.getText().toString().equals("Public")) {
                        i.putExtra("public", true);
                    }

                    // Let MainProfileActivity know that there is data to read
                    setResult(Activity.RESULT_OK, i);

                    // Finish this activity
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make an intent to let MainProfileActivity.java know that there will be
                // no data sent back
                Intent i = new Intent();

                // Let MainProfileActivity.java know that they result has been canceled
                setResult(Activity.RESULT_CANCELED, i);

                // Finish this activity
                finish();
            }
        });

        // Get the metric for our current display
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Get the width and height from display metrics
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // Set width and height of popup window
        getWindow().setLayout((int)(width*.8),(int)(height*.7));

        // Get and edit attributes so that positioning of popup is better
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        // Set attributes of popup window
        getWindow().setAttributes(params);
    }
}