package com.michaeljordanr.songstats;

import android.app.Application;

import com.michaeljordanr.songstats.data.local.AppDataBase;
import com.michaeljordanr.songstats.repository.SpotifyRepository;
import com.michaeljordanr.songstats.repository.StatsRepository;

public class BasicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppDataBase getDatabase() {
        return AppDataBase.getAppDatabase(this);
    }

    public StatsRepository getStatsRepository() {
        return StatsRepository.getInstance(getDatabase());
    }

    public SpotifyRepository getSpotifyRepository() {
        return SpotifyRepository.getInstance();
    }
}
