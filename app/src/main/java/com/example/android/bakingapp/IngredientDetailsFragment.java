package com.example.android.bakingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.adapters.IngredientDetailsAdapter;
import com.example.android.bakingapp.models.Ingredients;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientDetailsFragment extends Fragment {

    @BindView(R.id.rv_ingredient_details)
    RecyclerView recyclerViewIngredients;

    Ingredients[] theIngredients;
    int recipeClickedPosition;
    private static final String TAG = IngredientDetailsActivity.class.getSimpleName();

    public IngredientDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ingredient_details, container, false);

        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewIngredients.setLayoutManager(layoutManager);
        recyclerViewIngredients.setHasFixedSize(true);

        if (theIngredients !=null){
            IngredientDetailsAdapter adapter = new IngredientDetailsAdapter(theIngredients, getContext());
            recyclerViewIngredients.setAdapter(adapter);
        } else {
            Log.e(TAG, "Problems with the RecipeDetailsActivity adapter");
        }

        return rootView;
    }

    public void setIngredientsData(Ingredients[] ingredients) {
        this.theIngredients = ingredients;
    }

    public void setPosition(int position) {
        this.recipeClickedPosition = position;
    }

}
