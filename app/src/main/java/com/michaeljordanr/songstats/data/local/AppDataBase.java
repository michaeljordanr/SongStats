package com.michaeljordanr.songstats.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.michaeljordanr.songstats.data.local.converter.DateTypeConverter;
import com.michaeljordanr.songstats.data.local.dao.StatsDao;
import com.michaeljordanr.songstats.model.Stats;

@Database(entities = {Stats.class}, version = 1, exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "songstats";
    private static AppDataBase instance;

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
