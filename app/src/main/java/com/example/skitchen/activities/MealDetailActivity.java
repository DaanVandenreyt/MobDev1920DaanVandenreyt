package com.example.skitchen.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.skitchen.R;
import com.example.skitchen.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MealDetailActivity extends AppCompatActivity {

    private TextView tvName, tvDesc, tvCat;
    private TextView glutenView, veggieView;
    private Button btnToEmail;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private String mealUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        tvName = findViewById(R.id.tvNameData);
        tvDesc = findViewById(R.id.tvDescData);
        tvCat = findViewById(R.id.tvCatData);

        glutenView = findViewById(R.id.tvGluten);
        veggieView = findViewById(R.id.tvVeggie);

        btnToEmail = findViewById(R.id.btnToEmail);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        setValues();

        btnToEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealDetailActivity.this, EmailActivity.class);
                intent.putExtra("user", mealUser);
                intent.putExtra("meal", tvName.getText());
                startActivity(intent);
            }
        });
    }

    private void setValues() {
        Intent intent = getIntent();
        tvName.setText(intent.getStringExtra("name"));
        tvDesc.setText(intent.getStringExtra("description"));
        tvCat.setText(intent.getStringExtra("category"));

        boolean isGlutenFree = intent.getBooleanExtra("gluten", false);
        boolean isVeggie = intent.getBooleanExtra("veggie", false);

        if (isGlutenFree) {
            glutenView.setBackground(ContextCompat.getDrawable(this, R.drawable.checked_background));
        }
        if (isVeggie) {
            veggieView.setBackground(ContextCompat.getDrawable(this, R.drawable.checked_background));
        }

        mealUser = intent.getStringExtra("user");

        if (!Objects.equals(mealUser, currentUser.getUid())) {
            btnToEmail.setVisibility(View.VISIBLE);
        }
    }
}