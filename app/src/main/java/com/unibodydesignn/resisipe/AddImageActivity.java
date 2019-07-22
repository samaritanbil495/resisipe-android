package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class AddImageActivity extends AppCompatActivity {

    ImageView iv;
    String recipeName;
    String recipeDetails;
    String recipeIngredients;
    String recipeTag;
    String userID;

    private final int LOAD_IMG = 1;
    private final int SCAN = 3;

    private final int WRITE_EXTERNAL_STORAGE_PERMISSION = 0;
    private final int READ_EXTERNAL_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        Bundle bundle = getIntent().getExtras();
        recipeName = bundle.getString("recipe_name");
        recipeDetails = bundle.getString("recipe_details");
        recipeIngredients = bundle.getString("recipe_ingredients");
        recipeTag = bundle.getString("recipe_tags");
        userID = bundle.getString("user_id");

        iv = findViewById(R.id.iv);
        iv.setOnClickListener((v) -> {


        });

        Button button = findViewById(R.id.add);
        button.setOnClickListener((view) -> {
            Recipe recipe = new Recipe();
            recipe.setRecipeName(recipeName);
            recipe.setRecipeIngredients(recipeIngredients);
            recipe.setRecipeDetails(recipeDetails);
            recipe.setRecipeUserID(userID);
            recipe.setRecipeImage(null);
            addRecipeToHeroku(recipe);
        });
    }

    public void addRecipeToHeroku(Recipe recipe) {

    }

    public void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, LOAD_IMG);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case LOAD_IMG:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    iv.setImageURI(selectedImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        //recipe.setRecipeImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
    }

    void requestPermission() {
        if (ContextCompat.checkSelfPermission(AddImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(AddImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(AddImageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }

        if (ContextCompat.checkSelfPermission(AddImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(AddImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(AddImageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AddImageActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddImageActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }

            case READ_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AddImageActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddImageActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
