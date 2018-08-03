package com.michaeljordanr.songstats.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.michaeljordanr.songstats.utils.StatsType;

@Entity (tableName = "stats")
public class Stats {

    @PrimaryKey (autoGenerate = true)
    private int id;
    private String name;
    private String type;
    @ColumnInfo(name = "count")
    private int timesListened;
    private String imageUrl;

    public Stats() {
    }

    @Ignore
    public Stats(String name, String type, String imageUrl) {
        this.name = name;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimesListened() {
        return timesListened;
    }

    public String getTimesListenedString() {
        return String.valueOf(timesListened);
    }

    public void setTimesListened(int timesListened) {
        this.timesListened = timesListened;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
