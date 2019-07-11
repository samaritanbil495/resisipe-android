package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class AddActivity extends AppCompatActivity {

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
    public String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        recipe = new Recipe();

        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("userID");

        homeButton = findViewById(R.id.homeButton);
        addButton = findViewById(R.id.addButton);
        searchButton = findViewById(R.id.searchButton);
        addRecipeButton = findViewById(R.id.add_recipe);

        recipeNameText = findViewById(R.id.recipeName);
        recipeDetailsText = findViewById(R.id.recipeDetails);
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
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1000);
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


        if(recipeName != null && !recipeName.equals("") && recipeDetails != null && !recipeDetails.equals("")) {
            recipe.setRecipeName(recipeName);
            recipe.setRecipeDetails(recipeDetails);
            recipe.setRecipeID(generateID());
            recipe.setRecipeIngredients(recipeIngredients);
            recipe.setRecipeTags(recipeTags);
            recipe.setRecipeUserID(userID);
            Log.i("info", recipe.getRecipeName());
            Log.i("info", recipe.getRecipeDetails());
            Log.i("info", recipe.getRecipeIngredients());
            Log.i("info", recipe.getRecipeID());
            Log.i("info", recipe.getRecipeTags());
            Log.i("info", recipe.getRecipeUserID());
            addRecipeToHeroku(recipe);
            goHome();
        } else {
            Toast.makeText(getApplicationContext(), "Do not leave empty!", Toast.LENGTH_SHORT).show();
        }
    }
    
    public void addRecipeToHeroku(Recipe recipe) {
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl("https://resisipe.herokuapp.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).
                        build();

        RecipeService recipeService = retrofit.create(RecipeService.class);
        call = recipeService.postData(recipe.getRecipeName(), recipe.getRecipeDetails(), recipe.getRecipeIngredients(), recipe.getRecipeTags());
        //calling the api
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                Toast.makeText(AddActivity.this, "Added", Toast.LENGTH_SHORT).show();
                Log.i("eklendi mi ", call.request().toString());
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String generateID() {
        return RandomStringUtils.randomAlphanumeric(5).toLowerCase(); // for alphanumeric
    }
}
