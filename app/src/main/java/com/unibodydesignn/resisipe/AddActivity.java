package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

public class AddActivity extends AppCompatActivity {

    private SQLiteController db;
    Button homeButton;
    Button addButton;
    Button searchButton;
    Button addRecipeButton;
    EditText recipeNameText;
    EditText recipeDetailsText;
    EditText recipeIngredientsText;
    EditText recipeTagsText;

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

        recipeNameText = findViewById(R.id.recipeName);
        recipeDetailsText = findViewById(R.id.recipeIngredients);
        recipeIngredientsText = findViewById(R.id.recipeIngredients);
        recipeTagsText = findViewById(R.id.recipeTags);

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
        String recipeIngredients = recipeIngredientsText.getText().toString().trim();
        String recipeTags = recipeTagsText.getText().toString().trim();

        if(recipeName != null && recipeName.equals("") && recipeDetails != null && recipeDetails.equals("")) {
            recipe.setRecipeName("");
            recipe.setRecipeDetails(recipeDetails);
            recipe.setRecipeID(generateID());
            recipe.setRecipeIngredients(recipeIngredients);
            recipe.setRecipeTags(recipeTags);
            db.insertRecipe(recipe);
            goHome();
        } else {
            Toast.makeText(getApplicationContext(), "Do not leave empty!", Toast.LENGTH_SHORT).show();
        }
    }


    public String generateID() {
        return RandomStringUtils.randomAlphanumeric(5).toLowerCase(); // for alphanumeric
    }
}
