package com.michaeljordanr.songstats.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.michaeljordanr.songstats.db.dao.StatsDao;
import com.michaeljordanr.songstats.model.Stats;

@Database(entities = {Stats.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instance;

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    private static final String DATABASE_NAME = "songstats-db";

    public static AppDataBase getAppDatabase(Context context) {
        if (instance == null) {
            synchronized (AppDataBase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class,
                        DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }

    public abstract StatsDao statsDao();
}
