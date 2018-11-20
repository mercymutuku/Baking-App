package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.models.Ingredients;
import com.example.android.bakingapp.models.Recipe;
import com.example.android.bakingapp.provider.IngredientsContract.IngredientsEntry;
import com.example.android.bakingapp.utils.JsonUtils;

import org.json.JSONException;

import java.net.URL;

public class RecipeWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private String TAG = RecipeWidgetRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    private Cursor mCursor;


    RecipeWidgetRemoteViewsFactory(Context applicationContext, Intent intent){
        mContext = applicationContext;
    }

    // called when the app widget is created for the first time
    @Override
    public void onCreate() {



    }

    // called whenever the app widget is updated
    @Override
    public void onDataSetChanged() {

        if (mCursor != null){
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        Uri uri = IngredientsEntry.CONTENT_URI;

        mCursor = mContext.getContentResolver().query(uri, null, null, null, IngredientsEntry.COLUMN_RECIPE_NAME + " DESC ");

        Log.i(TAG, "mCursor: "+mCursor);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

        if (mCursor != null){
            mCursor.close();
        }

    }

    // returns number of records in the cursor
    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    // handles all the processing work. returns a RemoteViews object , in this case  the single list item
    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION || mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);

        int curPosition = position + 1;

        String recipe_ingredient = curPosition + " " +mCursor.getString(3);
        String recipe_measure_and_quantity = mCursor.getString(1) + " " + mCursor.getString(2);

        rv.setTextViewText(R.id.widgetItemIngredientLabel, recipe_ingredient);
        rv.setTextViewText(R.id.widgetItemQuantitynMeasure, recipe_measure_and_quantity);

        Intent fillInIntent = new Intent();

        rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    // returns the number of types of views we have in ListView. In this case we have the same types in each ListView so we return 1
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
