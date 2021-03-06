package com.unibodydesignn.resisipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.List;

public class RecipeAdapter extends BaseAdapter {

    private List<Recipe> recipeList;
    private Context context;


    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Object getItem(int i) {
        return recipeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.recipelist, null);

        Recipe recipe = recipeList.get(pos);

        ImageView recipePreview = convertView.findViewById(R.id.recipePreview);

        TextView recipeName = convertView.findViewById(R.id.recipeName);
        recipeName.setText(recipe.getRecipeName());

        Button recipeDetails = convertView.findViewById(R.id.detailsButton);
        recipeDetails.setOnClickListener((view -> {
            Recipe recipe2 = recipeList.get(pos);
            JsonObject temp = recipe2.getRecipeImage();
            String imageURL = temp.get("url").toString();
            Intent i = new Intent(context.getApplicationContext(), ShowImagesActivity.class);
            //i.putExtra("recipe", recipe2);
            i.putExtra("image_url", imageURL);
            Log.i("recipeeeee", recipe2.getRecipeID());
            i.putExtra("recipe_id", recipe2.getRecipeID());
            i.putExtra("recipe_name", recipe2.getRecipeName());
            i.putExtra("recipe_details", recipe2.getRecipeDetails());
            i.putExtra("recipe_ingredients", recipe2.getRecipeIngredients());
            i.putExtra("recipe_tags", recipe2.getRecipeTags());
            i.putExtra("recipe_user_id", recipe2.getRecipeUserID());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }));

        convertView.setOnClickListener((view -> {
            Recipe recipee = recipeList.get(pos);
            Intent i = new Intent(context.getApplicationContext(), DetailsActivity.class);
            Log.i("recipeeeee", recipee.getRecipeID());
            i.putExtra("recipe_id", recipee.getRecipeID());
            i.putExtra("recipe_name", recipee.getRecipeName());
            i.putExtra("recipe_details", recipee.getRecipeDetails());
            i.putExtra("recipe_ingredients", recipee.getRecipeIngredients());
            i.putExtra("recipe_tags", recipee.getRecipeTags());
            i.putExtra("recipe_user_id", recipee.getRecipeUserID());

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }));

        return convertView;
    }

    /*
    public void shareIntent(Recipe recipe) {
        Uri uri = Uri.parse("smsto:" + recipe.getRecipeName());
        Uri uri2 = Uri.parse("mailto:" + recipe.getRecipeName());
        String shareBody = "Hi! Here is a recipe that I would like to share with you.\n";
        shareBody += recipe.getRecipeURL();

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND, uri);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(android.content.Intent.EXTRA_PHONE_NUMBER, uri);
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chooserIntent = Intent.createChooser(sharingIntent, null);
        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooserIntent);

        Intent chooserIntent = Intent.createChooser(sharingIntent, "Share via");
        chooserIntent.setType("text/plain");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooserIntent);

    }*/
}
