package io.mypersonaltrainer.mypersonaltrainer.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import io.mypersonaltrainer.mypersonaltrainer.R;

/**
 * Created by aditya on 7/19/17.
 */

public class ExerciseWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_exercise_provider);

        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.lvWidget,intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
