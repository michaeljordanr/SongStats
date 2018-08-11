package com.michaeljordanr.songstats.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StatsWidgetRemoteViewsService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new com.michaeljordanr.songstats.widget.StatsViewsFactory(this.getApplicationContext());
    }
}
