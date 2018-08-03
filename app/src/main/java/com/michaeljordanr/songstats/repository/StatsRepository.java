package com.michaeljordanr.songstats.repository;

import android.arch.lifecycle.LiveData;

import com.michaeljordanr.songstats.db.AppDataBase;
import com.michaeljordanr.songstats.model.Stats;

import java.util.List;

public class StatsRepository {

    private static StatsRepository sInstance;
    private AppDataBase appDataBase;

    private StatsRepository(AppDataBase appDataBase){
        this.appDataBase = appDataBase;
    }

    public static StatsRepository getInstance(AppDataBase appDataBase){
        if (sInstance == null) {
            synchronized (StatsRepository.class) {
                if (sInstance == null) {
                    sInstance = new StatsRepository(appDataBase);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Stats>> getStats(){
        return appDataBase.statsDao().getStats();
    }

    public Stats getMostListenedStatsByType(String type){
        return appDataBase.statsDao().getMostListenedStatsByType(type);
    }

    public LiveData<List<Stats>> getStatsByType(String type){
        return appDataBase.statsDao().getStatsByType(type);
    }

    public void insert(Stats stats){
        appDataBase.statsDao().save(stats);
    }

    public void deleteAll(){
        appDataBase.statsDao().deleteAll();
    }
}
