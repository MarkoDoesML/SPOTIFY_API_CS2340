package com.example.spotify_api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DurationPopUpActivity extends AppCompatActivity {
    private RadioGroup durationButtons, publicButtons;
    private Button generateButton, cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up_activity_duration);
        durationButtons = findViewById(R.id.DurationButtons);
        publicButtons = findViewById(R.id.PublicButtons);
        generateButton = findViewById(R.id.GenerateButton);
        cancelButton = findViewById(R.id.CancelButton);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedButton = durationButtons.getCheckedRadioButtonId();
                int publicButton = publicButtons.getCheckedRadioButtonId();
                Log.d("VisibilityButtons", "The visibility button has the ID " + publicButton);
                if (checkedButton == -1) {
                    Toast.makeText(DurationPopUpActivity.this, "Please check a duration.", Toast.LENGTH_SHORT).show();
                } else if (publicButton == -1) {
                    Toast.makeText(DurationPopUpActivity.this, "Please check a visibility.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent();
                    if (checkedButton == 2131296280) {
                        i.putExtra("duration", "3Months");
                    } else if (checkedButton == 2131296277) {
                        i.putExtra("duration", "6Months");
                    } else {
                        i.putExtra("duration", "1Year");
                    }
                    if (publicButton == 2131296269) {
                        i.putExtra("public", true);
                    }
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(Activity.RESULT_CANCELED, i);
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