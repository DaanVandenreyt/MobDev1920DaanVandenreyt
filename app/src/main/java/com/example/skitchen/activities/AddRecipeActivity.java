package com.example.skitchen.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.skitchen.MealUpload;
import com.example.skitchen.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddRecipeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btnPickImage;
    private Button btnUploadMeal;
    private ImageView ivMealPic;
    private EditText etMealName;
    private EditText etMealDescription;
    private ProgressBar pbUploadMeal;

    private Uri imageUri;

    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    private StorageTask mealUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        btnPickImage = findViewById(R.id.btnPickImage);
        btnUploadMeal = findViewById(R.id.btnUploadMeal);
        ivMealPic = findViewById(R.id.ivMealPic);
        etMealName = findViewById(R.id.etMealName);
        etMealDescription = findViewById(R.id.etMealDescription);
        pbUploadMeal = findViewById(R.id.pbUploadMeal);


        storageRef = FirebaseStorage.getInstance().getReference("mealPics");
        databaseRef = FirebaseDatabase.getInstance().getReference("mealPics");

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser();
            }
        });

        btnUploadMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mealUploadTask != null && mealUploadTask.isInProgress()){
                    Toast.makeText(AddRecipeActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                uploadFile();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(ivMealPic);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null){
            StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            mealUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pbUploadMeal.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(AddRecipeActivity.this, "Upload succesful", Toast.LENGTH_SHORT).show();
                            MealUpload mealUpload = new MealUpload(etMealName.getText().toString().trim(),
                                                                    etMealDescription.getText().toString().trim(),
                                                                    taskSnapshot.getStorage().getDownloadUrl().toString());
                            String uploadID = databaseRef.push().getKey();
                            databaseRef.child(uploadID).setValue(mealUpload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddRecipeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pbUploadMeal.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(AddRecipeActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void OpenFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

}