package com.michaeljordanr.songstats.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "stats")
public class Stats {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String trackId;
    private String name;
    private String type;
    @ColumnInfo(name = "count")
    private int timesListened;
    private String imageUrl;
    private Date creationDate;

    public Stats() {
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
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

    public void setTimesListened(int timesListened) {
        this.timesListened = timesListened;
    }

    public String getTimesListenedString() {
        return String.valueOf(timesListened);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
