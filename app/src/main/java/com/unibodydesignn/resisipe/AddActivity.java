
package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.IOException;

public class AddActivity extends AppCompatActivity {

    Button homeButton;
    Button addButton;
    Button searchButton;
    Button addRecipeButton;
    EditText recipeNameText;
    EditText recipeDetailsText;
    EditText recipeIngredientsText;
    EditText recipeTagsText;
    Button uploadPhoto;

    public static final int LOAD_IMG = 2;
    public Recipe recipe;
    public static Retrofit retrofit;
    public static RecipeService service;
    public static Call<Recipe> call;
    public String userID;
    Uri selectedImage;

    public static final int READ_EXTERNAL_STORAGE_PERMISSION = 0;

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
        uploadPhoto = findViewById(R.id.uploadImage);

        recipeNameText = findViewById(R.id.recipeName);
        recipeDetailsText = findViewById(R.id.recipeDetails);
        recipeIngredientsText = findViewById(R.id.recipeIngredients);
        recipeTagsText = findViewById(R.id.recipeTags);

        addRecipeButton.setOnClickListener((v) -> {
            addRecipe();
        });

        uploadPhoto.setOnClickListener((v) -> {
            uploadImage();
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

        Intent i = new Intent(AddActivity.this, MainActivity.class);
        i.putExtra("recipe_name", recipeName);
        i.putExtra("recipe_details", recipeDetails);
        i.putExtra("recipe_ingredients", recipeIngredients);
        i.putExtra("recipe_tags", recipeTags);
        i.putExtra("user_id", userID);
        startActivity(i);


        if(recipeName != null && !recipeName.equals("") && recipeDetails != null && !recipeDetails.equals("")) {
            recipe.setRecipeName(recipeName);
            recipe.setRecipeDetails(recipeDetails);
            recipe.setRecipeID(generateID());
            recipe.setRecipeIngredients(recipeIngredients);
            recipe.setRecipeTags(recipeTags);
            recipe.setRecipeUserID(userID);
            recipe.setRecipeImage(null);
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

    public void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, LOAD_IMG);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case LOAD_IMG:
                    //data.getData returns the content URI for the selected Image
                    selectedImage = data.getData();
                    //iv.setImageURI(selectedImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        //recipe.setRecipeImage(bitmap);
                        File file = new File(getRealPathFromURI(selectedImage));
                        uploadPhoto.setText(file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void addRecipeToHeroku(Recipe recipe) {
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl("https://resisipe.herokuapp.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).
                        build();

        RecipeService recipeService = retrofit.create(RecipeService.class);
        call = recipeService.postData(recipe.getRecipeName(), recipe.getRecipeDetails(), recipe.getRecipeIngredients(), recipe.getRecipeTags(), recipe.getRecipeID());
        //calling the api
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                Toast.makeText(AddActivity.this, "Added", Toast.LENGTH_SHORT).show();
                Log.i("eklendi mi ", call.request().toString());
                Log.i("info :", response.body().toString());
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

    void requestPermission() {
        if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AddActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void uploadFile(Uri fileUri) {
        // create upload service client
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl("https://resisipe.herokuapp.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).
                        build();

        RecipeService recipeService = retrofit.create(RecipeService.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(getRealPathFromURI(selectedImage));

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = (Call<ResponseBody>) service.uploadImage(body, description, file.getName());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}