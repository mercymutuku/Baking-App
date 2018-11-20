package com.example.android.bakingapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.adapters.RecipeDetailsAdapter;
import com.example.android.bakingapp.models.Steps;
import com.example.android.bakingapp.utils.JsonUtils;

import org.json.JSONException;

import static com.example.android.bakingapp.utils.JsonUtils.logInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.StepsAdapterClickListener {

    private int recipeClickedPosition;
    private String recipeDetailsString;
    private String recipeName;
    RecyclerView mRecyclerView;
    TextView tv_recipe_ingredient;
    private Steps[] theSteps;
    OnStepClickListener mCallback;
    OnIngredientClickListener mOnIngredientClickListener;

    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // attach callback to fragment
        try {
            mCallback = (OnStepClickListener) context;
            mOnIngredientClickListener = (OnIngredientClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement Listeners");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        tv_recipe_ingredient = rootView.findViewById(R.id.tv_recipe_ingredient);
        tv_recipe_ingredient.setText(R.string.ingredients);

        tv_recipe_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickIngredientTextView();
            }
        });

        mRecyclerView = rootView.findViewById(R.id.rv_recipe_details);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (theSteps !=null){
            RecipeDetailsAdapter adapter = new RecipeDetailsAdapter(theSteps, getContext(), this);
            mRecyclerView.setAdapter(adapter);
        } else {
            Log.e(TAG, "Problems with the RecipeDetailsActivity adapter");
        }

        return rootView;
    }

    public void setStepData(Steps[] steps) {
        this.theSteps = steps;
    }

    public void setPosition(int position) {
        this.recipeClickedPosition = position;
    }

    public void setRecipeDetailsString(String recipeDetailsString) {
        this.recipeDetailsString = recipeDetailsString;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void onClickIngredientTextView(){
        mOnIngredientClickListener.onIngredientClicked();
    }

    @Override
    public void onStepClicked(int position) {
        mCallback.onStepClick(position);
    }

    //step click interface
    public interface OnStepClickListener {
        void onStepClick(int position);
    }

    //Ingredient click interface
    public interface OnIngredientClickListener {
        void onIngredientClicked();
    }
}
