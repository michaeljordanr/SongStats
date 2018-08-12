package com.michaeljordanr.songstats.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.RemoteViews;

import com.michaeljordanr.songstats.R;
import com.michaeljordanr.songstats.model.Stats;
import com.michaeljordanr.songstats.ui.activity.DetailActivity;
import com.michaeljordanr.songstats.utils.StatsType;

import java.util.List;

public class StatsWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_NEXT = "ACTION_NEXT";
    public static final String ACTION_PREVIOUSLY = "ACTION_PREVIOUSLY";

    private static List<List<Stats>> statsList;
    private static int position = 0;
    private static Class<? extends StatsWidgetProvider> varClass;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        StatsIntentService.startActionFetchStats(context);
        varClass = getClass();
    }

    public static void setStatsList(List<List<Stats>> statsListSend) {
        statsList = statsListSend;
    }

    public static List<Stats> getStatsList() {
        return statsList.get(position);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, int position) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, position);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int i) {
        position = i;
        if(statsList.size() > 0) {
            List<Stats> stats = statsList.get(position);

            // Create the RemoteViews
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_stats);
            views.setTextViewText(R.id.tv_recipe_title, StatsType.getTypeDescription(context, stats.get(0).getType()));

            // Set's up the previously action
            if (position > 0) {
                views.setViewVisibility(R.id.iv_previously, View.VISIBLE);
                Intent previouslyIntent = new Intent(context, varClass);
                previouslyIntent.setAction(ACTION_PREVIOUSLY);
                PendingIntent previouslyPendingIntent = PendingIntent.getBroadcast(context, 0, previouslyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.iv_previously, previouslyPendingIntent);
            } else {
                views.setViewVisibility(R.id.iv_previously, View.GONE);
            }

            // Set's up the next action
            if (position < statsList.size() - 1) {
                views.setViewVisibility(R.id.iv_next, View.VISIBLE);
                Intent nextIntent = new Intent(context, varClass);
                nextIntent.setAction(ACTION_NEXT);
                PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.iv_next, nextPendingIntent);
            } else {
                views.setViewVisibility(R.id.iv_next, View.GONE);
            }

            // Set's up the listview
            Intent adapterIntent = new Intent(context, com.michaeljordanr.songstats.widget.StatsWidgetRemoteViewsService.class);
            views.setRemoteAdapter(R.id.lv_stats, adapterIntent);

            // template to handle the click listener for each item
            Intent clickIntentTemplate = new Intent(context, DetailActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.lv_stats, clickPendingIntentTemplate);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetsId = appWidgetManager.getAppWidgetIds(new ComponentName(context, StatsWidgetProvider.class));

        switch (intent.getAction()) {
            case ACTION_NEXT:
                ++position;
                updateAppWidgets(context, appWidgetManager, appWidgetsId, position);
                break;
            case ACTION_PREVIOUSLY:
                --position;
                updateAppWidgets(context, appWidgetManager, appWidgetsId, position);
                break;
        }

        switch (intent.getAction()) {
            case ACTION_NEXT:
            case ACTION_PREVIOUSLY:
                // refresh all your widgets
                AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                ComponentName cn = new ComponentName(context, StatsWidgetProvider.class);
                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.lv_stats);
                break;
        }


        super.onReceive(context, intent);
    }
}
