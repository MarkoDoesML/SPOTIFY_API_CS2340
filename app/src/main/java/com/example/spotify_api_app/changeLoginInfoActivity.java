package com.example.spotify_api_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class changeLoginInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_activity_manage_account);
        EditText updateEmailEditText = findViewById(R.id.UpdateEmailEditText);
        EditText updatePasswordEditText = findViewById(R.id.UpdatePasswordEditText);

        Button updateEmailButton = findViewById(R.id.UpdateEmailButton);
        Button updatePasswordButton = findViewById(R.id.UpdatePasswordButton);
        Button returnButton = findViewById(R.id.ReturnButton);

        //TODO: also update local prefs

        updateEmailButton.setOnClickListener(v -> {
            String newEmail = updateEmailEditText.getText().toString().trim();
            if (!newEmail.isEmpty()) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    user.updateEmail(newEmail).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.sendEmailVerification().addOnCompleteListener(verificationTask -> {
                                if (verificationTask.isSuccessful()) {
                                    Log.d("EmailUpdate", "Verification email sent.");
                                    Toast.makeText(changeLoginInfoActivity.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("EmailUpdateError", "Failed to send verification email.", verificationTask.getException());
                                    Toast.makeText(changeLoginInfoActivity.this, "Failed to send verification email: " + verificationTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Log.e("EmailUpdateError", "Failed to update email.", task.getException());
                            Toast.makeText(changeLoginInfoActivity.this, "Failed to update email: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(changeLoginInfoActivity.this, "No user logged in.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(changeLoginInfoActivity.this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
            }
        });

        //TODO: also update local prefs

        updatePasswordButton.setOnClickListener(v -> {
            String newPassword = updatePasswordEditText.getText().toString().trim();
            if (!newPassword.isEmpty()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("PasswordUpdate", "User password updated.");
                            Toast.makeText(changeLoginInfoActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("PasswordUpdateError", "Failed to update password.", task.getException());
                            Toast.makeText(changeLoginInfoActivity.this, "Failed to update password: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


            } else {
                Toast.makeText(this, "Please enter a valid password.", Toast.LENGTH_SHORT).show();
            }        });

        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(changeLoginInfoActivity.this, MainProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

    }
}