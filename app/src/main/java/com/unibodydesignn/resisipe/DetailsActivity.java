package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {

    SQLiteController db;

    Recipe recipe;
    Button homeButton;
    Button addButton;
    Button searchButton;
    Button editButton;
    Button deleteButton;

    ImageView prepareImage;
    ImageView cookingImage;
    ImageView cookedImage;
    ImageView enjoyImage;

    EditText recipeName;
    EditText recipeDetails;

    Bitmap prepare;
    Bitmap cooking;
    Bitmap cooked;
    Bitmap enjoy;

    public static Retrofit retrofit;
    public static RecipeService service;
    public static Call<Recipe> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        db = MainActivity.db;
        recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        recipeName = findViewById(R.id.recipeName);
        recipeDetails = findViewById(R.id.recipeDetails);
        prepareImage = findViewById(R.id.prepare);
        cookingImage = findViewById(R.id.cooking);
        cookedImage = findViewById(R.id.cooked);
        enjoyImage = findViewById(R.id.enjoy);

        initializeActivity();

        homeButton = findViewById(R.id.homeButton);
        addButton = findViewById(R.id.addButton);
        searchButton = findViewById(R.id.searchButton);
        editButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

        prepareImage.setOnClickListener((v) -> {
            selectPrepareImage();
        });

        cookingImage.setOnClickListener((v) -> {
            selectCookingImage();
        });

        cookedImage.setOnClickListener((v) -> {
            selectCookedImage();
        });

        enjoyImage.setOnClickListener((v) -> {
            selectEnjoyImage();
        });

        homeButton.setOnClickListener((v) -> {
            Intent i = new Intent(DetailsActivity.this, MainActivity.class);
            startActivity(i);
        });

        addButton.setOnClickListener((v) -> {
            Intent i = new Intent(DetailsActivity.this, AddActivity.class);
            startActivity(i);
        });

        searchButton.setOnClickListener((v) -> {
            Intent i = new Intent(DetailsActivity.this, SearchActivity.class);
            startActivity(i);
        });

        editButton.setOnClickListener(view -> {
            editRecipe();
        });

        deleteButton.setOnClickListener((v) -> {
            deleteRecipe();
        });
    }
    public void initializeActivity() {
        recipeName.setText(recipe.getRecipeName());
        recipeDetails.setText(recipe.getRecipeDetails());
    }

    public void editRecipe() {
        String rcpName = recipeName.getText().toString().trim();
        String rcpDeta = recipeDetails.getText().toString().trim();
        recipe.setRecipeName(rcpName);
        recipe.setRecipeIngredients(rcpDeta);
        db.updateRecipe(recipe);
    }



    public void deleteRecipe() {
        db.deleteRecipe(recipe.getRecipeID());
    }



    public void selectPrepareImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    public void selectCookingImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    public void selectCookedImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 3);
    }

    public void selectEnjoyImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                prepare = extras.getParcelable("data");
                prepareImage.setImageBitmap(prepare);
            }
        } else if (requestCode == 2) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                cooking = extras.getParcelable("data");
                cookingImage.setImageBitmap(cooking);
            }
        } else if (requestCode == 3) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                cooked = extras.getParcelable("data");
                cookedImage.setImageBitmap(cooked);
            }
        } else if (requestCode == 4) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                enjoy = extras.getParcelable("data");
                enjoyImage.setImageBitmap(enjoy);
            }
        } else {
            Log.i("BASARISIZ!", "error");
        }
    }
}
