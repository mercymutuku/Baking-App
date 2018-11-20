package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.bakingapp.models.Ingredients;
import com.example.android.bakingapp.models.Steps;
import com.example.android.bakingapp.utils.JsonUtils;

import org.json.JSONException;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.OnStepClickListener, RecipeDetailsFragment.OnIngredientClickListener {

    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();
    private int stepClickedPosition;
    Steps[] stepsArray;
    Ingredients[] ingredientsArray;
    private boolean isTwoPane = false;
    String recipeDetailsString;
    int recipeClickedPosition;
    String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        determinePaneLayout();

        Intent intent = getIntent();

        if (intent != null) {

            recipeName = intent.getStringExtra("recipeName");
            setTitle(recipeName);

            recipeClickedPosition = intent.getIntExtra("recipeClickedPosition", 0);

            recipeDetailsString = intent.getStringExtra("recipeDetailsString");

            try {
                stepsArray = JsonUtils.parseStepJson(recipeDetailsString, recipeClickedPosition);
                ingredientsArray = JsonUtils.parseIngredientsJson(recipeDetailsString, recipeClickedPosition);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        fragment.setStepData(stepsArray);
        fragment.setPosition(recipeClickedPosition);
        fragment.setRecipeName(recipeName);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame_layout_recipe_details, fragment)
                .commit();

    }

    private void determinePaneLayout() {
        FrameLayout fragmentStepDetail = findViewById(R.id.frame_layout_step_details);
        if (fragmentStepDetail != null) {
            isTwoPane = true;
        }
    }

    @Override
    public void onStepClick(int position) {

        stepClickedPosition = position;

        if (isTwoPane){
            Log.i(TAG, "Two Pane Is On: "+stepClickedPosition);

            // set up step details fragment
            StepDetailsFragment fragment = new StepDetailsFragment();
            fragment.setStepData(stepsArray);
            fragment.setPosition(stepClickedPosition);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_step_details, fragment)
                    .commit();

        } else {
            Log.i(TAG, "One Pane Is On");
            //Start StepDetailsActivity
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra("recipeDetailsString", recipeDetailsString);
            intent.putExtra("stepClickedPosition", position);
            intent.putExtra("recipeClickedPosition", recipeClickedPosition);
            intent.putExtra("recipeName", recipeName);
            startActivity(intent);
        }
    }


    public void onNextClick(View view){

        if (isTwoPane){

            if(stepClickedPosition == ((stepsArray.length)-1)) return;

            StepDetailsFragment fragment = new StepDetailsFragment();
            fragment.setStepData(stepsArray);
            fragment.setPosition(++stepClickedPosition);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_activity_step_details, fragment)
                    .commit();

        }

    }

    public void onPreviousClick(View view){

        if (isTwoPane) {
            if (stepClickedPosition == 0) return;

            StepDetailsFragment fragment = new StepDetailsFragment();
            fragment.setStepData(stepsArray);
            fragment.setPosition(--stepClickedPosition);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_activity_step_details, fragment)
                    .commit();
        }

    }

    @Override
    public void onIngredientClicked() {

        if (isTwoPane) {

            // set up ingredient details fragment
            IngredientDetailsFragment fragmentIngredients = new IngredientDetailsFragment();
            fragmentIngredients.setIngredientsData(ingredientsArray);
            fragmentIngredients.setPosition(recipeClickedPosition);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.frame_layout_ingredient_details, fragmentIngredients)
                    .commit();

        } else {

            Intent intent = new Intent(RecipeDetailsActivity.this, IngredientDetailsActivity.class);
            intent.putExtra("recipeDetailsString", recipeDetailsString);
            intent.putExtra("recipeClickedPosition", recipeClickedPosition);
            intent.putExtra("recipeName", recipeName);
            startActivity(intent);
        }
    }
}
