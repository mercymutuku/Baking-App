package com.example.android.bakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.android.bakingapp.models.Steps;
import com.example.android.bakingapp.utils.JsonUtils;

import org.json.JSONException;

public class StepDetailsActivity extends AppCompatActivity {

    String recipeDetailsString, recipeName;
    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();
    private int recipeClickedPosition, stepClickedPosition;
    Steps[] stepsArray;
    StepDetailsFragment fragment;

    private static final String SAVED_STEP_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Intent intent = getIntent();

        if (intent != null) {
            recipeClickedPosition = intent.getIntExtra("recipeClickedPosition", 0);

            recipeDetailsString = intent.getStringExtra("recipeDetailsString");
            recipeName = intent.getStringExtra("recipeName");

            if (savedInstanceState != null && (savedInstanceState.containsKey(SAVED_STEP_POSITION))) {
                stepClickedPosition = savedInstanceState.getInt(SAVED_STEP_POSITION, 0);

                Log.i(TAG, "savedInstanceState: stepClickedPosition:"+stepClickedPosition);
            } else {
                stepClickedPosition = intent.getIntExtra("stepClickedPosition", 0);

                Log.i(TAG, "stepClickedPosition: initial:"+stepClickedPosition);
            }

            try {
                stepsArray = JsonUtils.parseStepJson(recipeDetailsString, recipeClickedPosition);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String strSpace = " - ";
            String strSteps = this.getResources().getString(R.string.steps);
            setTitle(strSteps+ strSpace + recipeName);
        }

        if (savedInstanceState == null)
        {
            fragment = new StepDetailsFragment();
            fragment.setStepData(stepsArray);
            fragment.setPosition(stepClickedPosition);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout_activity_step_details, fragment)
                    .commit();
        }

    }

    public void onPreviousClick(View view) {

        if(stepClickedPosition == 0) return;

        StepDetailsFragment fragment = new StepDetailsFragment();
        fragment.setStepData(stepsArray);
        fragment.setPosition(--stepClickedPosition);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_activity_step_details, fragment)
                .commit();

    }

    public void onNextClick(View view) {

        if(stepClickedPosition == ((stepsArray.length)-1)) return;

        StepDetailsFragment fragment = new StepDetailsFragment();
        fragment.setStepData(stepsArray);
        fragment.setPosition(++stepClickedPosition);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_activity_step_details, fragment)
                .commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SAVED_STEP_POSITION, stepClickedPosition);
    }
}
