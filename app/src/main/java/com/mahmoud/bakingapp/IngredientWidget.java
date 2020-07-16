package com.mahmoud.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String title = preferences.getString(RecipeInfoFragment.PREF_EXTRA_TITLE,null);
        String ingredients = preferences.getString(RecipeInfoFragment.PREF_EXTRA_INGREDIENTS,null);

        if(title !=null || ingredients!= null)
        {
            views.setTextViewText(R.id.widget_recipe_title, title);
            views.setTextViewText(R.id.widget_recipe_ingredient,ingredients);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            views.setOnClickPendingIntent(R.id.widget_recipe_title, pendingIntent);
            views.setOnClickPendingIntent(R.id.widget_recipe_ingredient, pendingIntent);
            views.setOnClickPendingIntent(R.id.widget_layout,pendingIntent);
        }



        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

