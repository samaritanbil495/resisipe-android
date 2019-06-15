package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
    SQLiteController db;
    public static Retrofit retrofit;
    public static RecipeService service;
    public static Call<List<Recipe>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        db = MainActivity.db;
        recipeList = db.getRecipeList();

        initialize(recipeList);

        searchText = findViewById(R.id.searchText);
        searchRecipeButton = findViewById(R.id.searchRecipeButton);

        searchRecipeButton.setOnClickListener((v) -> {
            String searchedWord = searchText.getText().toString().trim();
            search(searchedWord);
            initialize(searchedList);
        });
    }

    public void search(String tag) {
        searchedList = new ArrayList<>();
        initialize(searchedList);
        String word = tag;
        for(Recipe rcp : recipeList) {
            if(rcp.getRecipeName().contains(word))
                searchedList.add(rcp);
        }
    }

    public void initialize(List<Recipe> list) {
        ListView lv = findViewById(R.id.recipes);
        RecipeAdapter ra = new RecipeAdapter(getApplicationContext(), list);
        lv.setAdapter(ra);
    }


}
