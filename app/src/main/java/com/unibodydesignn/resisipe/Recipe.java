package com.unibodydesignn.resisipe;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;

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

    private Bitmap prepare;
    private Bitmap cooking;
    private Bitmap cooked;
    private Bitmap enjoy;
    private String jsonText;

    public Recipe() {
        this.recipeID = "";
        this.recipeName = "";
        this.recipeIngredients = "";
        this.recipeUserID = "";
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

    public void setPrepare(Bitmap prepare) {
        this.prepare = prepare;
    }

    public Bitmap getPrepare() {
        return this.prepare;
    }

    public void setCooking(Bitmap cooking) {
        this.cooking = cooking;
    }

    public Bitmap getCooking() {
        return this.cooking;
    }

    public void setCooked(Bitmap cooked) {
        this.cooked = cooked;
    }

    public Bitmap getCooked() {
        return this.cooked;
    }

    public void setEnjoy(Bitmap enjoy) {
        this.enjoy = enjoy;
    }

    public Bitmap getEnjoy() {
        return this.enjoy;
    }

    public void setRecipeTags(String recipeTags) {
        this.recipeTags = recipeTags;
    }

    public String getRecipeTags() {
        return this.recipeTags;
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
