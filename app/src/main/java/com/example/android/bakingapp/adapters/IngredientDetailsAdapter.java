package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.Ingredients;

public class IngredientDetailsAdapter extends RecyclerView.Adapter<IngredientDetailsAdapter.IngredientDetailsAdapterViewHolder> {

    private Context mContext;
    private Ingredients[] mIngredients = null;
    private TextView tvIngredient, tvMeasure, tvQuantity;

    public IngredientDetailsAdapter(Ingredients[] ingredients, Context context){
        this.mIngredients = ingredients;
        this.mContext = context;
    }

    @NonNull
    @Override
    public IngredientDetailsAdapter.IngredientDetailsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_recipe_ingredients, parent, false);

        return new IngredientDetailsAdapter.IngredientDetailsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientDetailsAdapter.IngredientDetailsAdapterViewHolder holder, int position) {

        String ingredient = mIngredients[position].getIngredient();
        String strSpace = " ";
        String strQty = mContext.getResources().getString(R.string.quantity);
        String strMeasure = mContext.getResources().getString(R.string.measure);
        String quantity = strQty+strSpace+mIngredients[position].getQuantity();
        String measure = strMeasure+strSpace+mIngredients[position].getMeasure();

        tvIngredient.setText(ingredient);
        tvMeasure.setText(measure);
        tvQuantity.setText(quantity);

    }

    @Override
    public int getItemCount() {
        return mIngredients.length;
    }

    class IngredientDetailsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        IngredientDetailsAdapterViewHolder(View itemView) {
            super(itemView);

            tvIngredient = itemView.findViewById(R.id.tv_ingredient);
            tvMeasure = itemView.findViewById(R.id.tv_measure);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);

        }

        @Override
        public void onClick(View v) {
        }
    }
}
