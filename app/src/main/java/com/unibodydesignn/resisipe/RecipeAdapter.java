package com.unibodydesignn.resisipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

        Recipe recipe = recipeList.get(pos);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.recipelist, null);

        String name = recipe.getRecipeName();
        String details = recipe.getRecipeIngredients();
        Bitmap pre = recipe.getPrepare();
        Bitmap coo = recipe.getCooking();
        Bitmap cod = recipe.getCooked();
        Bitmap enj = recipe.getEnjoy();

        ImageView recipePreview = convertView.findViewById(R.id.recipePreview);
        recipePreview.setImageBitmap(recipe.getEnjoy());

        TextView recipeName = convertView.findViewById(R.id.recipeName);
        recipeName.setText(recipe.getRecipeName());

        Button recipeDetails = convertView.findViewById(R.id.details);
        recipeDetails.setOnClickListener((view -> {
            Intent i = new Intent(context.getApplicationContext(), DetailsActivity.class);
            i.putExtra("recipe", recipe);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }));

        return convertView;
    }
}