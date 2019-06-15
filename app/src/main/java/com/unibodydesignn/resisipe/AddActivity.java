package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import org.apache.commons.lang3.RandomStringUtils;

public class AddActivity extends AppCompatActivity {

    private SQLiteController db;
    Button homeButton;
    Button addButton;
    Button searchButton;
    Button addRecipeButton;
    ImageView prepare;
    ImageView cooking;
    ImageView cooked;
    ImageView enjoy;
    EditText recipeNameText;
    EditText recipeDetailsText;

    public Recipe recipe;
    public static Retrofit retrofit;
    public static RecipeService service;
    public static Call<Recipe> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        db = MainActivity.db;
        recipe = new Recipe();

        homeButton = findViewById(R.id.deleteButton);
        addButton = findViewById(R.id.addButton);
        searchButton = findViewById(R.id.searchButton);
        addRecipeButton = findViewById(R.id.add_recipe);

        prepare = findViewById(R.id.prepare);
        cooking = findViewById(R.id.cooking);
        cooked = findViewById(R.id.cooked);
        enjoy = findViewById(R.id.enjoy);

        recipeNameText = findViewById(R.id.recipeName);
        recipeDetailsText = findViewById(R.id.recipeDetails);

        prepare.setOnClickListener((v) -> {
            selectPrepareImage();
        });

        cooking.setOnClickListener((v) -> {
            selectCookingImage();
        });

        cooked.setOnClickListener((v) -> {
            selectCookedImage();
        });

        enjoy.setOnClickListener((v) -> {
            selectEnjoyImage();
        });

        addRecipeButton.setOnClickListener((v) -> {
            addRecipe();
        });

        homeButton.setOnClickListener((v) -> {
            goHome();
        });

        searchButton.setOnClickListener((view -> {
            searchRecipe();
        }));
    }

    public void goHome() {
        Intent i = new Intent(AddActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void searchRecipe() {
        Intent i = new Intent(AddActivity.this, SearchActivity.class);
        startActivity(i);
        finish();
    }

    public void addRecipe() {
        String recipeName = recipeNameText.getText().toString().trim();
        String recipeDetails = recipeDetailsText.getText().toString().trim();

        if(recipeName != null || recipeName.equals("") || recipeDetails != null || recipeDetails.equals("")) {
            recipe.setRecipeName(recipeName);
            recipe.setRecipeDetails(recipeDetails);
            recipe.setRecipeID(generateID());
            recipe.setRecipeIngredients("cok sey var icinde");
            db.insertRecipe(recipe);
            goHome();
        } else {
            Toast.makeText(getApplicationContext(), "Do not leave empty!", Toast.LENGTH_SHORT).show();
        }
    }




    public void selectPrepareImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    public void selectCookingImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    public void selectCookedImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
    }

    public void selectEnjoyImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            prepare.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Log.i("set ", "begin");
            recipe.setPrepare(BitmapFactory.decodeFile(picturePath));
            Log.i("set ", "end");
        }
    }

    public String generateID() {
        return RandomStringUtils.randomAlphanumeric(5).toUpperCase(); // for alphanumeric
    }
}
