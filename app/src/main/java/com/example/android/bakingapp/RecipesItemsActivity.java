package com.example.android.bakingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.adapters.RecipeItemsAdapter;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.utils.JsonUtils;
import com.example.android.bakingapp.utils.UrlUtils;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesItemsActivity extends AppCompatActivity implements RecipeItemsAdapter.RecipeClickListener {

    @BindView(R.id.rv_recipe_card) RecyclerView mRecyclerView;
    @BindView(R.id.btn_retry) Button btn_retry;
    @BindView(R.id.tv_error) TextView tv_error;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    private Recipe[] mRecipes = null;

    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_IMAGE = "image";

    String recipeQueryResponse;

    public static final String TAG = RecipesItemsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_items);

        ButterKnife.bind(this);

        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        mRecyclerView.setHasFixedSize(true);

        if (!isOnline()){
            networkError();
            return;
        }

        new RecipesFetchTask().execute();
    }


    private boolean isOnline(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnectedOrConnecting();
    }

    public void networkError(){
        progressbar.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.VISIBLE);
        btn_retry.setVisibility(View.VISIBLE);
    }

    public void hideViews(){
        tv_error.setVisibility(View.INVISIBLE);
        progressbar.setVisibility(View.INVISIBLE);
        btn_retry.setVisibility(View.INVISIBLE);
    }

    public void retry(){
        if (!isOnline()){
            networkError();
            return;
        }

        hideViews();

        new RecipesFetchTask().execute();
    }

    @Override
    public void onRecipeItemClicked(int position) {
        if (!isOnline()){
            mRecyclerView.setVisibility(View.INVISIBLE);
            networkError();
            return;
        }

        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipeClickedPosition", position);
        intent.putExtra("recipeDetailsString", recipeQueryResponse);
        intent.putExtra("recipeName", mRecipes[position].getRecipeName());

        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    private class RecipesFetchTask extends AsyncTask<String, Void, Recipe[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mRecyclerView.setVisibility(View.INVISIBLE);
            progressbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Recipe[] doInBackground(String... strings) {

            if (!isOnline()){
                networkError();
                return null;
            }

            URL theRecipeUrl = UrlUtils.buildUrl();

            try {

                recipeQueryResponse = UrlUtils.getResponseFromHttp(theRecipeUrl);
                mRecipes = JsonUtils.parseRecipeJson(recipeQueryResponse);

            } catch (Exception e){
                e.printStackTrace();
            }

            return mRecipes;
        }

        @Override
        protected void onPostExecute(Recipe[] recipes) {

            new RecipesFetchTask().cancel(true);

            if (recipes !=null){
                mRecipes = recipes;
                RecipeItemsAdapter adapter = new RecipeItemsAdapter(recipes, RecipesItemsActivity.this, RecipesItemsActivity.this);
                mRecyclerView.setAdapter(adapter);

                progressbar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                Log.e(TAG, "Problems with the adapter");
            }
        }
    }
}
