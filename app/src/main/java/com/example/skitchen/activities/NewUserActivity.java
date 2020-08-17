package com.example.skitchen.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.skitchen.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class NewUserActivity extends AppCompatActivity {

    private static final String TAG = "[NewUserActivity]";

    private EditText etEmail;
    private EditText etName;
    private EditText etPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etMail);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);

        Button btnCreateUser = findViewById(R.id.btnCreateAccount);
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    public void createUser(String email, String password) {
        if (!email.contains("@")) {
            Toast.makeText(this, "Email shoeld contain '@'", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(this, "Password should be at least ", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(etName.getText().toString()).build();
                            mAuth.getCurrentUser().updateProfile(changeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });

                            Intent intent = new Intent(NewUserActivity.this, PrimaryActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(NewUserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            etEmail.setText("");
                            etPassword.setText("");
                        }
                    }
                });
    }
}