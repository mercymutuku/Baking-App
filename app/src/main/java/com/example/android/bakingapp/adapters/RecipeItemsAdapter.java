package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

public class RecipeItemsAdapter extends RecyclerView.Adapter<RecipeItemsAdapter.RecipeItemsAdapterViewHolder> {

    private Context mContext;
    private final RecipeClickListener mRecipeClickListener;
    private Recipe[] mRecipe = null;
    private ImageView imageViewHolder;
    private TextView tvRecipeName;

    private static final String TAG = RecipeItemsAdapter.class.getSimpleName();

    public RecipeItemsAdapter(Recipe[] recipes, Context context, RecipeClickListener recipeClickListener){
        this.mRecipe = recipes;
        this.mContext = context;
        this.mRecipeClickListener = recipeClickListener;
    }

    @NonNull
    @Override
    public RecipeItemsAdapter.RecipeItemsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_recipe_items, parent, false);

        return new RecipeItemsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemsAdapter.RecipeItemsAdapterViewHolder holder, int position) {
        String urlImage = mRecipe[position].getRecipeImage();
        String recipeName = mRecipe[position].getRecipeName();

        switch (position){
            case 0:
                if (!urlImage.equals("")){
                    populateRecipeImage(urlImage);
                }else {
                    imageViewHolder.setImageResource(R.drawable.ic_nutella_pie);
                }
                break;
            case 1:
                if (!urlImage.equals("")){
                    populateRecipeImage(urlImage);
                }else {
                    imageViewHolder.setImageResource(R.drawable.ic_brownie);
                }
                break;
            case 2:
                if (!urlImage.equals("")){
                    populateRecipeImage(urlImage);
                }else {
                    imageViewHolder.setImageResource(R.drawable.ic_yellow_cake);
                }
                break;
            case 3:
                if (!urlImage.equals("")){
                    populateRecipeImage(urlImage);
                }else {
                    imageViewHolder.setImageResource(R.drawable.ic_cheesecake);
                }
                break;
        }

        tvRecipeName.setText(recipeName);
    }

    private void populateRecipeImage(String urlImage){

        Picasso.with(mContext)
                .load(urlImage)
                .fit()
                .into(imageViewHolder);

    }

    @Override
    public int getItemCount() {
        return mRecipe.length;
    }

    public class RecipeItemsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RecipeItemsAdapterViewHolder(View itemView) {
            super(itemView);

            tvRecipeName = itemView.findViewById(R.id.tv_recipe_name);
            imageViewHolder = itemView.findViewById(R.id.iv_recipe_image);
            imageViewHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mRecipeClickListener.onRecipeItemClicked(clickedPosition);
        }
    }

    public interface RecipeClickListener{
        void onRecipeItemClicked(int position);
    }
}
