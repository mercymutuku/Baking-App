package com.example.android.bakingapp.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class UrlUtils {


    private static final String RECIPE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String TAG = UrlUtils.class.getSimpleName();

    public static URL buildUrl(){
        Uri uri = Uri.parse(RECIPE_BASE_URL)
                .buildUpon()
                .build();

        URL url = null;

        try {
            url = new URL(uri.toString());
            //Log.i(TAG, "The URL: "+ url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Problems creating the URL", e);
        }

        return url;
    }

    public static String getResponseFromHttp(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
