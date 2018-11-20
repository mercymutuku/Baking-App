package com.example.android.bakingapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.bakingapp.models.Ingredients;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.provider.IngredientsContract;
import com.example.android.bakingapp.provider.IngredientsDbHelper;
import com.example.android.bakingapp.utils.CheckNetwork;
import com.example.android.bakingapp.utils.JsonUtils;

import org.json.JSONException;

public class IngredientDetailsActivity extends AppCompatActivity {

    private static final String TAG = IngredientDetailsActivity.class.getSimpleName();
    private int recipeClickedPosition;
    private String recipeName;
    String recipeDetailsString;
    Ingredients[] ingredientsArray;
    Recipe[] recipesArray;

    private final static String MENU_SELECTED = "selected";
    private int selected = -1;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_details);

        Intent intent = getIntent();

        if (intent != null) {

            recipeClickedPosition = intent.getIntExtra("recipeClickedPosition", 0);
            recipeName = intent.getStringExtra("recipeName");

            recipeDetailsString = intent.getStringExtra("recipeDetailsString");

            try {
                ingredientsArray = JsonUtils.parseIngredientsJson(recipeDetailsString, recipeClickedPosition);
                recipesArray = JsonUtils.parseRecipeJson(recipeDetailsString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String strSpace = " - ";
        String strIngredients = this.getResources().getString(R.string.ingredients);
        setTitle(strIngredients+ strSpace + recipeName);

        IngredientDetailsFragment fragmentIngredients = new IngredientDetailsFragment();
        fragmentIngredients.setIngredientsData(ingredientsArray);
        fragmentIngredients.setPosition(recipeClickedPosition);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame_layout_ingredient_details, fragmentIngredients)
                .commit();

        invalidateOptionsMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        menuItem = menu.findItem(R.id.action_add_to_widget);


        if (checkRecipe(new IngredientsDbHelper(this).getReadableDatabase(), IngredientsContract.IngredientsEntry.TABLE_NAME, recipeName)){
            menuItem.setVisible(false);
        }

        return true;
    }

    public static boolean check (int id, int theId) {
        return id == theId;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        int theId = R.id.action_add_to_widget;

        if ((!(CheckNetwork.isInternetAvailable(this))) & (!check(id, theId))){
            return false;
        }

        switch (id) {
            case R.id.action_add_to_widget:
                saveIngredientsData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveIngredientsData(){

        IngredientsDbHelper ingredientAppDbHelper = new IngredientsDbHelper(this);
        SQLiteDatabase db = ingredientAppDbHelper.getReadableDatabase();
        String tableName = IngredientsContract.IngredientsEntry.TABLE_NAME;


        if (checkDb(db, tableName)){

            deleteAll(db, tableName);

            Toast.makeText(getApplicationContext(), "current ingredients cleared from widget", Toast.LENGTH_SHORT).show();
            //return;
        }

        ContentValues contentValues = new ContentValues();

        for (int j = 0; j < ingredientsArray.length; j++) {
            contentValues.put(IngredientsContract.IngredientsEntry.COLUMN_RECIPE_NAME,
                    recipesArray[recipeClickedPosition].getRecipeName());
            contentValues.put(IngredientsContract.IngredientsEntry.COLUMN_RECIPE_ID,
                    recipeClickedPosition);
            contentValues.put(IngredientsContract.IngredientsEntry.COLUMN_QUANTITY,
                    ingredientsArray[j].getQuantity());
            contentValues.put(IngredientsContract.IngredientsEntry.COLUMN_MEASURE,
                ingredientsArray[j].getMeasure());
            contentValues.put(IngredientsContract.IngredientsEntry.COLUMN_INGREDIENT,
                    ingredientsArray[j].getIngredient());
            getContentResolver().insert(IngredientsContract.IngredientsEntry.CONTENT_URI, contentValues);
        }

        menuItem.setVisible(false);

        RecipeWidgetProvider.sendRefreshBroadcast(this);

        Toast.makeText(getApplicationContext(), recipeName+" ingredients added to widget", Toast.LENGTH_LONG).show();
    }

    private boolean checkDb(SQLiteDatabase db, String tableName) {

        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        return cursor.moveToNext();

    }

    private boolean checkRecipe(SQLiteDatabase db, String tableName, String recipeName) {

        String the_recipe_name = recipeName;

        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE recipe_name = ?", new String[]{the_recipe_name});

        return cursor.moveToNext();

    }

    private void deleteAll(SQLiteDatabase db, String tableName) {

        db.execSQL("DELETE FROM " + tableName);

    }

}
