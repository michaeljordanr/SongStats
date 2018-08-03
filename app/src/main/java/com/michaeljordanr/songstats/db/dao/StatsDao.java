package com.michaeljordanr.songstats.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.michaeljordanr.songstats.model.Stats;

import java.util.List;

@Dao
public interface StatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Stats... stats);

    @Query("delete from stats")
    void deleteAll();

    @Query("SELECT id, name, type, imageUrl, count(*) AS count from stats " +
            "GROUP BY name, type")
    LiveData<List<Stats>> getStats();

    @Query("SELECT id, name, type, imageUrl, count(*) AS count from stats " +
            "WHERE type = :type " +
            "GROUP BY name")
    LiveData<List<Stats>> getStatsByType(String type);

    @Query("SELECT id, name, type, imageUrl, count(*) AS count from stats " +
            "WHERE type = :type " +
            "GROUP BY name, type " +
            "ORDER BY count(*) DESC " +
            "LIMIT 1")
    Stats getMostListenedStatsByType(String type);
}
