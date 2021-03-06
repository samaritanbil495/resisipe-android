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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    Button registerButton;

    ArrayList<User> allUsers;
    public static Retrofit retrofit;
    public static RecipeService service;
    public static Call<List<User>> call;
    public User currentUser;

    boolean exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        allUsers = new ArrayList<>(100);
        initializeHeroku();

        emailText = findViewById(R.id.loginEmailText);
        passwordText = findViewById(R.id.loginPasswordText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.toRegister);


        loginButton.setOnClickListener((v) -> {
            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            for(User u : allUsers) {
                if(u.getEmail().equals(email)) {
                    exists = true;
                    //Toast.makeText(LoginActivity.this, "Succesfull login!", Toast.LENGTH_SHORT).show();
                    Log.i("basarili", u.getEmail() + " " + u.getId());
                    currentUser = u;
                    break;
                }
            }

            if(!exists) {
                Toast.makeText(getApplicationContext(), "Email or password incorrect!", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("basariliasd", currentUser.getEmail() + " " + currentUser.getId());
                Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                toMain.putExtra("userID", currentUser.getId());
                startActivity(toMain);
                finish();
            }


        });

        registerButton.setOnClickListener((V) -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
            finish();
        });

    }

    public void initializeHeroku() {

        Gson gson = new GsonBuilder().setLenient().create();
        // https://jsonplaceholder.typicode.com
        retrofit = new Retrofit.Builder().baseUrl("https://resisipe.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RecipeService recipeService = retrofit.create(RecipeService.class);
        call = recipeService.loginUser();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                allUsers.addAll(response.body());
                Log.i("Fetching users is succesful!", String.valueOf(allUsers.size()));
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("YOU'RE A FAILURE!", t.getMessage().toString());
            }
        });
    }
}
