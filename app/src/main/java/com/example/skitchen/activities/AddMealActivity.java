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
    private Button btnChooseImage;
    private ImageView ivMealPic;
    private EditText etMealName;
    private EditText etMealDescription;
    private RadioGroup rgMealCategory;
    private CheckBox cbGlutenFree;
    private CheckBox cbVegetarian;
    private ProgressBar pbUploadProgress;
    private Uri mImageUri;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private UploadTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        btnUploadMeal = findViewById(R.id.btnUploadMeal);
        btnChooseImage = findViewById(R.id.btnPickImage);
        ivMealPic = findViewById(R.id.ivMealPic);
        etMealName = findViewById(R.id.etMealName);
        etMealDescription = findViewById(R.id.etMealDescription);
        rgMealCategory = findViewById(R.id.rgMealCategory);
        cbGlutenFree = findViewById(R.id.cbGlutenFree);
        cbVegetarian = findViewById(R.id.cbVegetarian);
        pbUploadProgress = findViewById(R.id.pbUploadMeal);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("meals/");
        mStorageRef = FirebaseStorage.getInstance().getReference("meals/");

        btnUploadMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get()
                    .load(mImageUri)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .fit()
                    .centerCrop()
                    .into(ivMealPic);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile() {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = (UploadTask) fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pbUploadProgress.setProgress(0);
                                }
                            }, 500);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    Meal uploadMeal = createMeal();
                                    uploadMeal.setImage(imageUrl);
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(uploadMeal);
                                }
                            });
                            Toast.makeText(AddMealActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Handler toParentActivityHandler = new Handler();
                            toParentActivityHandler.postDelayed(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(getParentActivityIntent());
                                        }
                                    }
                                    , 500);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddMealActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pbUploadProgress.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
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