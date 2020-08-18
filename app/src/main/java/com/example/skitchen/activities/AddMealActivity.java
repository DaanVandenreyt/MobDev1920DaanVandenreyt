package com.example.skitchen.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.skitchen.Meal;
import com.example.skitchen.MealCategory;
import com.example.skitchen.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddMealActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Meal newMeal;

    private Button btnUploadMeal;
    private ImageView ivMealPic;
    private EditText etMealName;
    private EditText etMealDescription;
    private RadioGroup rgMealCategory;
    private CheckBox cbGlutenFree;
    private CheckBox cbVegetarian;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        btnUploadMeal = findViewById(R.id.btnUploadMeal);
        ivMealPic = findViewById(R.id.ivMealPic);
        etMealName = findViewById(R.id.etMealName);
        etMealDescription = findViewById(R.id.etMealDescription);
        rgMealCategory = findViewById(R.id.rgMealCategory);
        cbGlutenFree = findViewById(R.id.cbGlutenFree);
        cbVegetarian = findViewById(R.id.cbVegetarian);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        databaseRef = FirebaseDatabase.getInstance().getReference("meals/");



        btnUploadMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new uploadMealTask().execute();

            }
        });
    }

    private class uploadMealTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            newMeal = createMeal();
            String uploadID = databaseRef.push().getKey();
            databaseRef.child(uploadID).setValue(newMeal);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getBaseContext(), "Upload complete", Toast.LENGTH_SHORT).show();

            Handler handler = new Handler();
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            startActivity(getParentActivityIntent());
                        }
                    }
            , 500);
        }
    }

    private Meal createMeal() {
        Meal meal = new Meal(etMealName.getText().toString(), etMealDescription.getText().toString(), currentUser.getUid());
        MealCategory mealCategory = MealCategory.NONE;
        switch (rgMealCategory.getCheckedRadioButtonId()) {
            case R.id.catPasta:
                mealCategory = MealCategory.PASTA;
                break;
            case R.id.catMeat:
                mealCategory = MealCategory.MEAT;
                break;
            case R.id.catFish:
                mealCategory = MealCategory.FISH;
                break;
            case R.id.catSalad:
                mealCategory = MealCategory.SALAD;
                break;
        }
        meal.setCategory(mealCategory);
        meal.setGlutenFree(cbGlutenFree.isChecked());
        meal.setVegetarian(cbVegetarian.isChecked());

        return meal;
    }
}