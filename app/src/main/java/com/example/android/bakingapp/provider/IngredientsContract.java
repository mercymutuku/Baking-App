package com.example.android.bakingapp.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class IngredientsContract {

    static final String AUTHORITY = "com.example.android.bakingapp";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static final String PATH_INGREDIENTS = "ingredients";

    public static final class IngredientsEntry implements BaseColumns{

        public static final Uri CONTENT_URI=
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(PATH_INGREDIENTS)
                        .build();
        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";

        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        //public static final String COLUMN_INGREDIENTS = "ingredient";
    }
}
