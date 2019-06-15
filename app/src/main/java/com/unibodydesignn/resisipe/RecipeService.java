package com.unibodydesignn.resisipe;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
    
    @FormUrlEncoded
    @PATCH("/recipes/{id}")
    Call<Recipe> updateData(
            @Path("id") String id,
            @Field("recipe_name") String name,
            @Field("recipe_detail") String details,
            @Field("recipe_ingredients") String ingredients,
            @Field("recipe_tags") String tags);

}

