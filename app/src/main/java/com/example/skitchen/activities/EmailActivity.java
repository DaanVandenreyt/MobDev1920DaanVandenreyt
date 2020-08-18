package com.example.skitchen.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skitchen.Meal;
import com.example.skitchen.MealsAdapter;
import com.example.skitchen.R;
import com.example.skitchen.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmailActivity extends AppCompatActivity {

    private TextView tvName, tvEmail;
    private EditText etMailContent;
    private Button btnSendMail;

    private String cookId;
    private String mailContent;
    private String mailSubject;
    private String mailReceiver;


    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        etMailContent = findViewById(R.id.etMailContent);
        btnSendMail = findViewById(R.id.btnSendMail);

        Intent intent = getIntent();
        cookId = intent.getStringExtra("user");
        mailSubject = "Interest in your meal: " + intent.getStringExtra("meal");

        new GetMealsTask().execute();

        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailContent = etMailContent.getText().toString();

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{mailReceiver});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
                emailIntent.putExtra(Intent.EXTRA_TEXT   , mailContent);
                emailIntent.setType("message/rfc822");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Pick your email client"));
                    finish();
                    Log.i("Finished sending email", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(EmailActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
               }
        });
    }

    private class GetMealsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(cookId);
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //cook.setName((String) snapshot.child("name").getValue());
                    //cook.setEmail(snapshot.child("email").toString());
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    mailReceiver = user.getEmail();
                    String name = "Cook's name: " + user.getName();
                    String mail = "Cook's mail: " + user.getEmail();
                    tvName.setText(name);
                    tvEmail.setText(mail);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EmailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }
}