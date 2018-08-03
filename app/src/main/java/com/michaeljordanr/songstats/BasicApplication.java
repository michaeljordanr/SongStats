package com.michaeljordanr.songstats;

import android.app.Application;

import com.michaeljordanr.songstats.db.AppDataBase;
import com.michaeljordanr.songstats.repository.StatsRepository;

public class BasicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppDataBase getDatabase() {
        return AppDataBase.getAppDatabase(this);
    }

    public StatsRepository getRepository() {
        return StatsRepository.getInstance(getDatabase());
    }
}
