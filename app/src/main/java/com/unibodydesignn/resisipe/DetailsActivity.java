package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
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

public class DetailsActivity extends AppCompatActivity {

    SQLiteController db;

    Recipe recipe;
    Button homeButton;
    Button addButton;
    Button searchButton;
    Button editButton;
    Button deleteButton;

    EditText recipeName;
    EditText recipeDetails;
    EditText recipeIngredients;
    EditText recipeTags;

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

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        recipeName = findViewById(R.id.recipeName);
        recipeDetails = findViewById(R.id.recipeDetails);
        recipeIngredients = findViewById(R.id.recipeIngredients);
        recipeTags = findViewById(R.id.recipeTags);
        initializeActivity();

        homeButton = findViewById(R.id.homeButton);
        addButton = findViewById(R.id.addButton);
        searchButton = findViewById(R.id.searchButton);
        editButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);


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
            editRecipe(recipe.getRecipeID());
        });

        deleteButton.setOnClickListener((v) -> {
            deleteRecipe();
            goHome();
        });
    }
    public void initializeActivity() {
        recipeName.setText(recipe.getRecipeName());
        recipeDetails.setText(recipe.getRecipeDetails());
        recipeIngredients.setText(recipe.getRecipeIngredients());
        recipeTags.setText(recipe.getRecipeTags());
    }

    public void editRecipe(String recipe_id) {
        String rcpName = recipeName.getText().toString().trim();
        String rcpDeta = recipeDetails.getText().toString().trim();
        String rcpIng = recipeIngredients.getText().toString().trim();
        String rcpTag = recipeTags.getText().toString().trim();
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeName(rcpName);
        recipe1.setRecipeDetails(rcpDeta);
        recipe1.setRecipeIngredients(rcpIng);
        recipe1.setRecipeTags(rcpTag);
        recipe1.setRecipeID(recipe1.getRecipeID());
        recipe1.setRecipeID(recipe_id);
        updateWithHeroku(recipe1);
        goHome();
    }

    public void updateWithHeroku(Recipe recipe) {

        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl("https://resisipe.herokuapp.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RecipeService recipeService = retrofit.create(RecipeService.class);
        call = recipeService.updateData(recipe,recipe.getRecipeID());//recipe, "57","Harikadetay", "mukemmel", "yuhnasilya","heeyo"
        Log.i("Call nedir ", call.request().toString());

        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                Toast.makeText(DetailsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                //Log.i("Response Body", response.body().toString());
                //Log.i("Mesaj.", response.message());
                Log.i("Mesaj.", response.toString());

            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                Log.i("Hata",t.getMessage());
            }
        });
    }

    public void goHome(){
        startActivity(new Intent(DetailsActivity.this, MainActivity.class));
        finish();
    }

    public void deleteRecipe() {
        deleteWithHeroku(recipe);
    }

    public void deleteWithHeroku(Recipe recipe) {

        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl("https://resisipe.herokuapp.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RecipeService recipeService = retrofit.create(RecipeService.class);
        call = recipeService.deletePost(recipe.getRecipeID());
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                Log.i("silindi mi ", call.request().toString());

            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                Log.i("Hata", t.getMessage());
            }
        });
    }
}