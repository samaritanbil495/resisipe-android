package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    EditText searchText;
    Button searchRecipeButton;
    List<Recipe> recipeList;
    List<Recipe> searchedList;

    Button homeButton;
    Button searchButton;
    Button addButton;

    public static Retrofit retrofit;
    public static RecipeService service;
    public static Call<List<Recipe>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recipeList = new ArrayList<>();
        //recipeList = db.getRecipeList();
        searchedList = new ArrayList<>();
        initializeHeroku();
        initialize(recipeList);

        searchText = findViewById(R.id.searchText);
        searchRecipeButton = findViewById(R.id.searchRecipeButton);

        addButton = findViewById(R.id.addButton);
        homeButton = findViewById(R.id.homeButton);
        searchButton = findViewById(R.id.searchButton);

        searchRecipeButton.setOnClickListener((v) -> {
            String searchedWord = searchText.getText().toString().trim();
            if(searchedWord == null || searchedWord.equals(""))
                Toast.makeText(SearchActivity.this, "Search for a tag!", Toast.LENGTH_SHORT).show();
            else {
                search(searchedWord);
                initialize(searchedList);
            }
        });

        addButton.setOnClickListener((v) -> {
            goAdd();
        });

        homeButton.setOnClickListener((v) -> {
            goHome();
        });

        searchButton.setOnClickListener((v) -> {
            initialize(recipeList);
        });

    }

    public void search(String tag) {
        Log.i("tag", tag);
        for(Recipe rcp : recipeList) {
            if(rcp.getRecipeTags() != null && !rcp.getRecipeTags().equals("") && rcp.getRecipeTags().contains(tag))
                searchedList.add(rcp);
        }
        Log.i("list size", String.valueOf(searchedList.size()));
    }

    public void initialize(List<Recipe> list) {
        ListView lv = findViewById(R.id.recipes);
        RecipeAdapter ra = new RecipeAdapter(getApplicationContext(), list);
        lv.setAdapter(ra);
    }

    public void initializeHeroku() {

        Gson gson = new GsonBuilder().setLenient().create();
        // https://jsonplaceholder.typicode.com
        retrofit = new Retrofit.Builder().baseUrl("https://resisipe.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RecipeService recipeService = retrofit.create(RecipeService.class);
        call = recipeService.allRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                for(Recipe r : response.body()) {
                    recipeList.add(r);
                    Log.i("tag", r.getRecipeTags());
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d("snow", t.getMessage().toString());
            }
        });
    }
    public void goHome() {
        startActivity(new Intent(SearchActivity.this, MainActivity.class));
    }

    public void goAdd() {
        startActivity(new Intent(SearchActivity.this, AddActivity.class));
    }

}