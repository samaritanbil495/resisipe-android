package com.unibodydesignn.resisipe;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Recipe implements Serializable {

    @SerializedName("id")
    @Expose
    private String recipeID;

    @SerializedName("recipe_name")
    @Expose
    private String recipeName;

    @SerializedName("recipe_ingredients")
    @Expose
    private String recipeIngredients;

    @SerializedName("recipe_detail")
    @Expose
    private String recipeDetails;

    @SerializedName("recipe_id")
    @Expose
    private String recipeUserID;

    @SerializedName("recipe_tags")
    @Expose
    private String recipeTags;

    @SerializedName("image")
    private JsonObject recipeImage;

    private String imageURL;
    private String jsonText;
    private String recipeURL;


    public Recipe() {
        this.recipeID = "";
        this.recipeName = "";
        this.recipeIngredients = "";
        this.recipeUserID = "";
        this.recipeImage = null;
    }

    public Recipe(String id, String recipeName, String recipeIngredients, String recipeUserID, JsonObject recipeImage) {
        this.recipeID = id;
        this.recipeName = recipeName;
        this.recipeIngredients = recipeIngredients;
        this.recipeUserID = recipeUserID;
        this.recipeImage = recipeImage;
        this.imageURL = recipeImage.get("url").toString();
        Log.i("Cagirildi mi ?", "cs");
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getRecipeID() {
        return this.recipeID;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeName() {
        return this.recipeName;
    }

    public void setRecipeIngredients(String recipeIngredients) {
       this.recipeIngredients = recipeIngredients + " ";
    }

    public JsonObject getRecipeImage() {
        return this.recipeImage;
    }

    public String getImageURL() {
        JsonObject recipeImage = getRecipeImage();
        if (recipeImage != null)
        this.imageURL = recipeImage.get("url").toString();
        return this.imageURL;
    }

    public void setRecipeImage(JsonObject recipeImage) {
        this.recipeImage = recipeImage;

    }

    public String getRecipeIngredients() {
        return this.recipeIngredients;
    }

    public void setRecipeDetails(String recipeDetails) {
        this.recipeDetails = recipeDetails;
    }

    public String getRecipeDetails() {
        return this.recipeDetails;
    }

    public void setRecipeUserID(String recipeUserID) {
        this.recipeUserID = recipeUserID;
    }

    public String getRecipeUserID() {
        return this.recipeUserID;
    }

    public void setRecipeTags(String recipeTags) {
        this.recipeTags = recipeTags;
    }

    public String getRecipeTags() {
        return this.recipeTags;
    }

    public void setRecipeURL(String recipeURL) {
        this.recipeURL = recipeURL;
    }

    public String getRecipeURL() {
        return this.recipeURL;
    }

    public String constructJSONCardInfo() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", recipeID);
            obj.put("title", recipeName);
            obj.put("body", recipeDetails);
        } catch (org.json.JSONException e) {
            Log.i("Hata", "!");
        }
        jsonText = obj.toString();
        return jsonText;
    }

}
