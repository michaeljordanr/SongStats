package com.michaeljordanr.songstats.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.michaeljordanr.songstats.data.local.AppDataBase;
import com.michaeljordanr.songstats.model.Stats;
import com.michaeljordanr.songstats.repository.StatsRepository;
import com.michaeljordanr.songstats.ui.viewmodel.StatsViewModel;
import com.michaeljordanr.songstats.utils.StatsType;

import java.util.ArrayList;
import java.util.List;

public class StatsIntentService extends IntentService {
    public static final String ACTION_FETCH_RECIPES = "com.michaeljordanr.songstats.widget.action.FETCH_RECIPES";


    public StatsIntentService() {
        super("StatsIntentService");
    }

    public static void startActionFetchStats(Context context) {
        Intent intent = new Intent(context, StatsIntentService.class);
        intent.setAction(ACTION_FETCH_RECIPES);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(ACTION_FETCH_RECIPES.equals(action)) {
                new AsyncClass(this).execute();
            }
        }
    }

    class AsyncClass extends AsyncTask<Void, Void, List<List<Stats>>> {
        Context context;

        public AsyncClass(Context context){
            this.context = context;
        }

        @Override
        protected List<List<Stats>> doInBackground(Void... voids) {
            StatsRepository repository = StatsRepository.getInstance(AppDataBase.getAppDatabase(context));
            List<List<Stats>> statsList = new ArrayList<>();

            try {
                statsList.add(repository.getStatsByTypeWidget(StatsType.STATS_TYPE_ARTIST));
                statsList.add(repository.getStatsByTypeWidget(StatsType.STATS_TYPE_ALBUM));
                statsList.add(repository.getStatsByTypeWidget(StatsType.STATS_TYPE_TRACK));
            } catch (Exception e) {
                e.printStackTrace();
            }


            return statsList;
        }

        @Override
        protected void onPostExecute(List<List<Stats>> statsList) {
            com.michaeljordanr.songstats.widget.StatsWidgetProvider.setStatsList(statsList);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetsId = appWidgetManager.getAppWidgetIds(new ComponentName(context, com.michaeljordanr.songstats.widget.StatsWidgetProvider.class));
            com.michaeljordanr.songstats.widget.StatsWidgetProvider.updateAppWidgets(context, appWidgetManager, appWidgetsId, 0);

        }
    }

}
