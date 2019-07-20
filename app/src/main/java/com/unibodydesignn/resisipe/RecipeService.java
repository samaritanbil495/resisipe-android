package com.unibodydesignn.resisipe;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface RecipeService {

    @Headers({
            "Accept: application/json"
    })

    // /recipes_m
    @GET("/andrecipes/index")
    Call<List<Recipe>> allRecipes();
    
    @FormUrlEncoded
    @POST("/andrecipes/new")
    Call<Recipe> postData(
            @Field("recipe_name") String name,
            @Field("recipe_detail") String details,
            @Field("recipe_ingredients") String ingredients,
            @Field("recipe_tags") String tags);
    
    @DELETE("/recipes/{id}")
    Call<Recipe> deletePost(@Path("id") String id);
    
    @PATCH("recipes/{id}")
    Call<Recipe> updateData(
            @Body Recipe user,
            @Path("id") String id
    );

    @GET("/andusers/index")
    Call<List<User>> loginUser();

    @Headers("Content-Type: application/json")
    @POST("/users/")
    Call<User> registerUser(@Body JsonObject user);

    @Multipart
    @POST("/image")
    Call<Recipe> uploadImage(MultipartBody.Part part, RequestBody description);




    // kullanıcıyı register etmek icin --> http://resisipe.herokuapp.com/signup urlsi
    // kullanıcıyı register edildikten sonra -->  http://resisipe.herokuapp.com/users/5
    // kullanıcı login olması icin --> http://resisipe.herokuapp.com/login
    // kullanıcı login olduktan sonra http://resisipe.herokuapp.com/

    /*

    void getRetrofitImage() {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    RetrofitImageAPI service = retrofit.create(RetrofitImageAPI.class);

    Call<ResponseBody> call = service.getImageDetails();

    call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {

            try {

                Log.d("onResponse", "Response came from server");

                boolean FileDownloaded = DownloadImage(response.body());

                Log.d("onResponse", "Image is downloaded and saved ? " + FileDownloaded);

            } catch (Exception e) {
                Log.d("onResponse", "There is an error");
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(Throwable t) {
            Log.d("onFailure", t.toString());
        }
    });
}
Following is the file handling code for image:

private boolean DownloadImage(ResponseBody body) {

        try {
            Log.d("DownloadImage", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = body.byteStream();
                out = new FileOutputStream(getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            }
            catch (IOException e) {
                Log.d("DownloadImage",e.toString());
                return false;
            }
            finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

            int width, height;
            ImageView image = (ImageView) findViewById(R.id.imageViewId);
            Bitmap bMap = BitmapFactory.decodeFile(getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
            width = 2*bMap.getWidth();
            height = 6*bMap.getHeight();
            Bitmap bMap2 = Bitmap.createScaledBitmap(bMap, width, height, false);
            image.setImageBitmap(bMap2);

            return true;

        } catch (IOException e) {
            Log.d("DownloadImage",e.toString());
            return false;
        }
    }





     */

}

