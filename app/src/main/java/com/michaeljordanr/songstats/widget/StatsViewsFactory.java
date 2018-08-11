package com.michaeljordanr.songstats.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.michaeljordanr.songstats.R;
import com.michaeljordanr.songstats.model.Stats;

import java.util.List;

public class StatsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Stats> statsList;

    public StatsViewsFactory(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        this.statsList = StatsWidgetProvider.getStatsList();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return statsList == null ? 0 : statsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Stats stats = statsList.get(position);

        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.item_widget_stats_list);

        row.setTextViewText(R.id.tv_short_description, stats.getName());
        row.setTextViewText(R.id.tv_count, stats.getTimesListenedString());

        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
