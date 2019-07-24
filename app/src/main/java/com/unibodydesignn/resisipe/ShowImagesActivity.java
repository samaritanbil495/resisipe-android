package com.unibodydesignn.resisipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowImagesActivity extends AppCompatActivity {

    private ImageView iv1;
    private Button shareButton;
    private Button likeButton;


    private final int LOAD_IMG = 1;
    private final int SCAN = 3;

    private final int WRITE_EXTERNAL_STORAGE_PERMISSION = 0;
    private final int READ_EXTERNAL_STORAGE_PERMISSION = 1;
    private final int INTERNET = 2;

    public static Retrofit retrofit;
    public static RecipeService service;
    public static Call<Recipe> call;
    public static Call<List<Like>> likeCall;
    public static Recipe recipe;
    public static String imageURL;


    String recipe_id = "";
    public static ArrayList<Like> likeList;
    public static int numberOfLikes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        requestPermission();
        likeList = new ArrayList<>(10);
        Bundle bundle = getIntent().getExtras();
        imageURL = bundle.getString("image_url");
        //recipe = (Recipe) bundle.get("recipe");
        recipe_id = bundle.getString("recipe_id");
        Log.i("oncretat", recipe_id + "");
        iv1 = findViewById(R.id.iv1);
        iv1.setOnClickListener((v) -> {
            uploadImage();
        });

        shareButton = findViewById(R.id.shareButton);
        likeButton = findViewById(R.id.likeButton);
        likeButton.setText(String.valueOf(0));
        likeList = initializeHeroku();
        Log.i("Peki ya burada : " , likeList.size()+"");

        shareButton.setOnClickListener((v) -> {
            shareRecipe(recipe_id);
        });

        likeButton.setOnClickListener((v) -> {
            likeRecipeRetrofit(recipe_id);
            likeList = initializeHeroku();
            Log.i("slyinda", likeList.toString());
            this.numberOfLikes++;
            Log.i("like count clikec", String.valueOf(likeCount(recipe_id, likeList)));
            likeButton.setText(String.valueOf(1));
        });
        saveImageHeroku();
    }

    public void shareRecipe(String recipe_id) {
        String shareBody = "https://resisipe.herokuapp.com/" + "recipes"+"/" + recipe_id;

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, LOAD_IMG);
    }

    public  void likeRecipe(){
        likeRecipeRetrofit(recipe_id);
    }

    private void sendNotification(String msg) {
        Log.i("Begendi", "Begendi");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long notificatioId = System.currentTimeMillis();

        Intent intent = new Intent(getApplicationContext(), ShowImagesActivity.class); // Here pass your activity where you want to redirect.

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, 0);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            //currentapiVersion = R.mipmap.ic_notification_lolipop;
        } else{
            currentapiVersion = R.mipmap.ic_launcher;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(currentapiVersion)
                .setContentTitle(this.getResources().getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);
        mNotificationManager.notify((int) notificatioId, notificationBuilder.build());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case LOAD_IMG:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    iv1.setImageURI(selectedImage);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        //recipe.setRecipeImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
    }

    void requestPermission() {
        if (ContextCompat.checkSelfPermission(ShowImagesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowImagesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(ShowImagesActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }

        if (ContextCompat.checkSelfPermission(ShowImagesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowImagesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(ShowImagesActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ShowImagesActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShowImagesActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }

            case READ_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ShowImagesActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShowImagesActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void saveImageHeroku() {

        try {
            Log.i("merhaba", "dn");

            imageURL = imageURL.substring(1, imageURL.length() -1);
            Log.i("url ", imageURL);
            String url1 = "https://resisipe.herokuapp.com" + imageURL;
            URL url = new URL(url1);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            iv1.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void likeRecipeRetrofit(String recipe_id) {
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl("https://resisipe.herokuapp.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).
                        build();

        RecipeService recipeService = retrofit.create(RecipeService.class);
        Log.i("id id id ", recipe_id + "");
        call = recipeService.likeGivenRecipe(recipe_id);
        //calling the api
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                //Toast.makeText(ShowImagesActivity.this, "Added", Toast.LENGTH_SHORT).show();
                //Log.i("eklendi mi ", call.request().toString());
                //Log.i("info :", response.body().toString());
                Log.i("liked ", "recipe");
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.i("failıure", "failure");
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("like count", call.request().body().toString());
            }
        });
    }

    public ArrayList<Like> initializeHeroku() {
        likeList.clear();
        Gson gson = new GsonBuilder().setLenient().create();
        // https://jsonplaceholder.typicode.com
        retrofit = new Retrofit.Builder().baseUrl("https://resisipe.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RecipeService recipeService = retrofit.create(RecipeService.class);
        likeCall = recipeService.allLikes();

        likeCall.enqueue(new Callback<List<Like>>() {
            ArrayList<Like> temp =new ArrayList<Like>();
            @Override
            public void onResponse(Call<List<Like>> call, Response<List<Like>> response) {
                for(Like l : response.body()) {
                    likeList.add(l);
                }
                for(Like r : likeList) {
                    Log.i("like info recipe id ", r.getRecipeID());
                    Log.i("like info recipe liker ", r.getRecipeLiker());
                    Log.i("like info recipe owner", r.getRecipeOwner());
                    temp.add(r);
                }
                Log.i("cikinca", likeList.toString());
                likeList = temp;
                int n = likeCount(recipe_id, likeList);
                likeButton.setText(String.valueOf(n));
                numberOfLikes = n;
            }

            @Override
            public void onFailure(Call<List<Like>> call, Throwable t) {
                Log.d("snow", t.getMessage().toString());
            }
        });
        return likeList;
    }

    public int likeCount(String id, ArrayList<Like> list) {
        int count = 0;
        Log.i("size ", ""+list.size());
        for(Like l : list) {
            Log.i("owner ", l.getRecipeOwner());
            if(l.getRecipeOwner().equals(id))
                ++count;
        }
        Log.i("count ", count + " ");
        return count;
    }

}