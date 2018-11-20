package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public String recipeName;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.collection_widget);

            Intent launchRecipeListIntent = new Intent(context, RecipesItemsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchRecipeListIntent, 0);

            views.setOnClickPendingIntent(R.id.widgetTitleLabel, pendingIntent);

            Intent intent = new Intent(context, RecipeWidgetRemoteViewsService.class);

            views.setRemoteAdapter(R.id.widgetListView, intent);

            PendingIntent pendingIntent1 = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(launchRecipeListIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setPendingIntentTemplate(R.id.widgetListView, pendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void sendRefreshBroadcast(Context context){

        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        intent.setComponent(new ComponentName(context, RecipeWidgetProvider.class));

        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        final String action = intent.getAction();

        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            // refresh all your widgets
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, RecipeWidgetProvider.class);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.widgetListView);
        }

        super.onReceive(context, intent);
    }

}

