package com.example.skitchen.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.example.skitchen.R;

public class MealDetailActivity extends AppCompatActivity {

    TextView tvName, tvDesc, tvCat;
    TextView glutenView, veggieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        tvName = findViewById(R.id.tvNameData);
        tvDesc = findViewById(R.id.tvDescData);
        tvCat = findViewById(R.id.tvCatData);

        glutenView = findViewById(R.id.tvGluten);
        veggieView = findViewById(R.id.tvVeggie);

        setValues();
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
    }
}