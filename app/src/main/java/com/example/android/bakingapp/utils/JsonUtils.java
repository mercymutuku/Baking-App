package com.example.android.bakingapp.utils;

import android.util.Log;

import com.example.android.bakingapp.models.Ingredients;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.models.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_IMAGE = "image";

    private static final String RECIPE_SHORT_DESCRIPTION = "shortDescription";
    private static final String RECIPE_DESCRIPTION = "description";
    private static final String RECIPE_VIDEO_URL = "videoURL";
    private static final String RECIPE_THUMBNAIL_URL = "thumbnailURL";
    private static final String RECIPE_QUANTITY = "quantity";
    private static final String RECIPE_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";

    public static Recipe[] parseRecipeJson(String jsonRecipeDetails) throws JSONException {

        final JSONArray jsonArray = new JSONArray(jsonRecipeDetails);
        Recipe[] theResult = new Recipe[jsonArray.length()];

        for (int i=0; i < jsonArray.length(); i++){
            Recipe recipe = new Recipe();

            recipe.setRecipeName(jsonArray.getJSONObject(i).optString(RECIPE_NAME));
            recipe.setRecipeImage(jsonArray.getJSONObject(i).optString(RECIPE_IMAGE));

            theResult[i] = recipe;
        }

        return theResult;
    }

    public static Steps[] parseStepJson(String jsonRecipeDetails, int clickedPosition) throws JSONException {

        JSONArray jsonStepsArray = null;

        JSONArray jsonArray = new JSONArray(jsonRecipeDetails);

        JSONObject jsonObject = jsonArray.getJSONObject(clickedPosition);

        jsonStepsArray = jsonObject.getJSONArray("steps");

        Steps[] theSteps = new Steps[jsonStepsArray.length()];

        for (int j=0; j < jsonStepsArray.length(); j++){

            Steps step = new Steps();

            step.setShortDescription(jsonStepsArray.getJSONObject(j).optString(RECIPE_SHORT_DESCRIPTION));
            step.setDescription(jsonStepsArray.getJSONObject(j).optString(RECIPE_DESCRIPTION));
            step.setVideoURL(jsonStepsArray.getJSONObject(j).optString(RECIPE_VIDEO_URL));
            step.setThumbnailURL(jsonStepsArray.getJSONObject(j).optString(RECIPE_THUMBNAIL_URL));

            theSteps[j] = step;

        }

        return theSteps;
    }

    public static Ingredients[] parseIngredientsJson(String jsonRecipeDetails, int clickedPosition) throws JSONException {

        JSONArray jsonArray = new JSONArray(jsonRecipeDetails);

        JSONObject jsonObject = jsonArray.getJSONObject(clickedPosition);

        JSONArray jsonIngredientsArray = jsonObject.getJSONArray("ingredients");

        Ingredients[] theIngredients = new Ingredients[jsonIngredientsArray.length()];

        for (int j=0; j < jsonIngredientsArray.length(); j++){

            Ingredients ingredient = new Ingredients();

            ingredient.setIngredient(jsonIngredientsArray.getJSONObject(j).optString(RECIPE_INGREDIENT));
            ingredient.setQuantity(jsonIngredientsArray.getJSONObject(j).optString(RECIPE_QUANTITY));
            ingredient.setMeasure(jsonIngredientsArray.getJSONObject(j).optString(RECIPE_MEASURE));

            theIngredients[j] = ingredient;

        }

        return theIngredients;
    }

    public static void logInfo(String str){
        if (str.length() > 4000){
            Log.i(TAG, str.substring(0, 4000));
            logInfo(str.substring(4000));
        } else {
            Log.i(TAG, str);
        }
    }
}
