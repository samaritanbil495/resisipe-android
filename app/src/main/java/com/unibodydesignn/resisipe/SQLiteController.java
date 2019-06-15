package com.unibodydesignn.resisipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class SQLiteController extends SQLiteOpenHelper implements Serializable {
    private static final String DATABASE_NAME = "Recipes";
    private static final int DATABASE_VERSION = 1;
    private final String RECIPE_INFO = "RecipeInfo";

    public SQLiteController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RECIPE_INFO);
        String create_table = "create table if not exists "
                + RECIPE_INFO
                + " (id text, name text, details text, ingredients text, userid text)";
        sqLiteDatabase.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RECIPE_INFO);
        onCreate(sqLiteDatabase);
    }

    public void clearTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(RECIPE_INFO, null, null);
    }

    public void insertRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", recipe.getRecipeID());
        contentValues.put("name", recipe.getRecipeName());
        contentValues.put("details", recipe.getRecipeDetails());
        contentValues.put("ingredients", recipe.getRecipeIngredients());
        contentValues.put("userid", recipe.getRecipeUserID());
        db.insert(RECIPE_INFO, null, contentValues);
    }

    public void updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", recipe.getRecipeID());
        contentValues.put("name", recipe.getRecipeName());
        contentValues.put("details", recipe.getRecipeDetails());
        contentValues.put("ingredients", recipe.getRecipeIngredients());
        contentValues.put("userid", recipe.getRecipeUserID());
        db.update(RECIPE_INFO, contentValues, "id = ?", new String[] { recipe.getRecipeID()});
    }

    public Integer deleteRecipe(String recipeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(RECIPE_INFO, "id = ?", new String[] { recipeID });
    }

    public List<Recipe> getRecipeList() {
        List<Recipe> recipeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + RECIPE_INFO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String recipeID = cursor.getString(0);
                String recipeName = cursor.getString(1);
                String recipeDetails = cursor.getString(2);
                String recipeIngredients = cursor.getString(3);
                String recipeUserID = cursor.getString(4);
                Recipe recipe = new Recipe();
                recipe.setRecipeID(recipeID);
                recipe.setRecipeName(recipeName);
                recipe.setRecipeDetails(recipeDetails);
                recipe.setRecipeIngredients(recipeIngredients);
                recipe.setRecipeUserID(recipeUserID);
                recipeList.add(recipe);
            } while (cursor.moveToNext());
        } cursor.close();
        return recipeList;
    }
}
