package com.unibodydesignn.resisipe;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Like implements Serializable {

    @SerializedName("id")
    @Expose
    private String recipeID;//hangi recipenin begenildigi

    @SerializedName("recipe_id")
    @Expose
    private String recipeOwner;

    @SerializedName("user_id")
    @Expose
    private String recipeLiker;

    public Like() {
        this.recipeID = "";
        this.recipeOwner = "";
        this.recipeLiker = "";

    }

    public Like(String id, String recipeOwner, String recipeLiker) {
        this.recipeID = id;
        this.recipeOwner = recipeOwner;
        this.recipeLiker = recipeLiker;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getRecipeID() {
        return this.recipeID;
    }

    public void setRecipeOwner(String recipeOwner) {
        this.recipeOwner = recipeOwner;
    }

    public String getRecipeOwner() {
        return this.recipeOwner;
    }

    public void setRecipeLiker(String recipeLiker) { this.recipeLiker = recipeLiker + " "; }
    public String getRecipeLiker() {
        return this.recipeLiker;
    }


}

