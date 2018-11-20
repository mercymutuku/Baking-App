package com.example.android.bakingapp.provider;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bakingapp.provider.IngredientsContract.IngredientsEntry;

public class IngredientsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bakingDb.db";
    private static final  int VERSION = 1;

    public IngredientsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE " + IngredientsEntry.TABLE_NAME + " ("+
                IngredientsEntry._ID + " INTEGER PRIMARY KEY, " +
                IngredientsEntry.COLUMN_QUANTITY + " TEXT NOT NULL, " +
                IngredientsEntry.COLUMN_MEASURE + " TEXT NOT NULL, " +
                IngredientsEntry.COLUMN_INGREDIENT + " TEXT NOT NULL, " +
                IngredientsEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                IngredientsEntry.COLUMN_RECIPE_ID + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now simply drop the table and create a new one.
        db.execSQL("DROP TABLE IF EXISTS " + IngredientsEntry.TABLE_NAME);
        onCreate(db);
    }
}
