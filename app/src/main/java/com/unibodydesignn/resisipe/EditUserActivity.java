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
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EditUserActivity extends AppCompatActivity {

    User user;
    public static Retrofit retrofit;
    public static RecipeService service;
    public static Call<User> call;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Bundle bundle = getIntent().getExtras();
        user_id = bundle.getString("userID");

        EditText nicknameText = findViewById(R.id.nicknameText);
        nicknameText.setText("unibodydesignn");
        EditText emailText = findViewById(R.id.emailText);
        emailText.setText("mmete@etu.edu.tr");
        EditText passwordText = findViewById(R.id.passwordText);
        EditText confirmPasswordText = findViewById(R.id.confirmPasswordText);
        EditText nDayText = findViewById(R.id.nDayText);
        nDayText.setText("2");
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener((v) -> {
            if(!confirmPasswordText.getText().toString().equals(passwordText.getText().toString()))
                Toast.makeText(EditUserActivity.this, "Passwords must match!", Toast.LENGTH_SHORT);
            else {
                user = new User();
                user.setNickname(nicknameText.getText().toString().trim());
                user.setEmail(emailText.getText().toString().trim());
                user.setPassword(passwordText.getText().toString().trim());
                user.setRemainingTime(Integer.parseInt(nDayText.getText().toString()));
                addUserToHeroku(user);
            }
        });
    }

    public void addUserToHeroku(User user) {
        Log.i("user email", user.getEmail());
        Log.i("user password", user.getPassword());

        JsonObject innerObject = new JsonObject();
        innerObject.addProperty("email", user.getEmail());
        innerObject.addProperty("password", user.getPassword());
        innerObject.addProperty("nicknmame", user.getNickname());
        //innerObject.addProperty("");

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("user", innerObject);

        String info = jsonObject.toString();
        Log.i("Created", info);

        User gs = new GsonBuilder().create().fromJson(info, User.class);

        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl("https://resisipe.herokuapp.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RecipeService recipeService = retrofit.create(RecipeService.class);
        call = recipeService.updateUser(jsonObject, user_id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(EditUserActivity.this, "Edited!", Toast.LENGTH_SHORT).show();
                Log.i("eklendi mi? ", call.request().toString());
                response.raw().header("user");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("user eklendi mi ?", "hayir!");
                // Intent to main login
            }
        });
    }
}
