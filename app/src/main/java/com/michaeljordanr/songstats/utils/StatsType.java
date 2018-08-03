package com.michaeljordanr.songstats.utils;

import android.content.Context;

import com.michaeljordanr.songstats.R;

public class StatsType {
    public static final String STATS_TYPE_ARTIST = "ARTIST_STATS";
    public static final String STATS_TYPE_ALBUM = "ALBUM_STATS";
    public static final String STATS_TYPE_TRACK = "TRACK_STATS";

    public static String getTypeDescription(Context context, String type){
        switch (type){
            case STATS_TYPE_ARTIST:
                return context.getText(R.string.stats_type_artist).toString();
            case STATS_TYPE_ALBUM:
                return context.getText(R.string.stats_type_album).toString();
            case STATS_TYPE_TRACK:
                return context.getText(R.string.stats_type_track).toString();
                default:
                    return " - ";
        }
    }
}
