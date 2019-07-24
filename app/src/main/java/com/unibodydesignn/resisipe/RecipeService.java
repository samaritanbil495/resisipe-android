package com.unibodydesignn.resisipe;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface  RecipeService {

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
            @Field("recipe_tags") String tags,
            @Field("recipe_id") String recipeid);
    
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
    @POST("/uploads/recipe/image/1/{image_name}")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part part, @Part("description") RequestBody description, @Path("image_name") String filename);

    @PATCH("users/{id}/edit")
    Call<User> updateUser(
            @Body JsonObject user,
            @Path("id") String id
    );

    @POST("/recipes/{recipe_id}/likes")
    Call<Recipe> likeGivenRecipe(@Path("recipe_id") String id);

    // /likes
    @GET("/andlikes/index")
    Call<List<Like>> allLikes();




    // kullanıcıyı register etmek icin --> http://resisipe.herokuapp.com/signup urlsi
    // kullanıcıyı register edildikten sonra -->  http://resisipe.herokuapp.com/users/5
    // kullanıcı login olması icin --> http://resisipe.herokuapp.com/login
    // kullanıcı login olduktan sonra http://resisipe.herokuapp.com/



}

