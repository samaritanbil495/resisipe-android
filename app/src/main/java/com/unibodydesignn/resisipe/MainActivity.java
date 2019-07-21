package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {

    Button homeButton;
    Button addButton;
    Button searchButton;
    Button myRecipes;
    List<Recipe> recipeList;
    private final int WRITE_EXTERNAL_STORAGE_PERMISSION = 0;
    private final int READ_EXTERNAL_STORAGE_PERMISSION = 1;
    public static Retrofit retrofit;
    public static RecipeService service;
    public static Call<List<Recipe>> call;
    public String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
        recipeList = new ArrayList<>(100);
        initializeHeroku();
        initializeRecipes();
        homeButton = findViewById(R.id.homeButton);
        addButton = findViewById(R.id.addButton);
        searchButton = findViewById(R.id.searchButton);
        myRecipes = findViewById(R.id.myRecipes);

        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("userID");

        addButton.setOnClickListener((v) -> {
            addResipe();
        });

        searchButton.setOnClickListener((v) -> {
            searchResipe();
        });

        homeButton.setOnClickListener((v) -> {
            initializeHeroku();
            initializeRecipes();
        });

        myRecipes.setOnClickListener((v) -> {
            showMyRecipes();
        });
    }

    public void showMyRecipes() {
        Intent myRecipes = new Intent(MainActivity.this, MyRecipesActivity.class);
        myRecipes.putExtra("userID", userID);
        startActivity(myRecipes);
    }

    public void addResipe() {
        Intent toSearch = new Intent(MainActivity.this, AddActivity.class);
        startActivity(toSearch);
    }

    public void searchResipe() {
        Intent toSearch = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(toSearch);
    }

    public void initializeRecipes() {
        ListView lv = findViewById(R.id.recipes);
        RecipeAdapter ra = new RecipeAdapter(getApplicationContext(), recipeList);
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
                recipeList.addAll(response.body());
                Log.i("size : ", String.valueOf(recipeList.size()));
                for(Recipe r : recipeList) {
                    Log.i("image url main act", r.getImageURL() + " ...");
                }
                initializeRecipes();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d("snow", t.getMessage().toString());
            }
        });

    }


    void requestPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }

            case READ_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }


}
