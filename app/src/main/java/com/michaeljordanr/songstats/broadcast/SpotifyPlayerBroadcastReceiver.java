package com.michaeljordanr.songstats.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.michaeljordanr.songstats.db.AppDataBase;
import com.michaeljordanr.songstats.model.Stats;
import com.michaeljordanr.songstats.repository.StatsRepository;
import com.michaeljordanr.songstats.utils.StatsType;

import static com.michaeljordanr.songstats.utils.Constants.METADATA_CHANGED;

public class SpotifyPlayerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        StatsRepository repository = StatsRepository.getInstance(AppDataBase.getAppDatabase(context));

        String action = intent.getAction();

        if (action != null && action.equals(METADATA_CHANGED)) {


            String trackId = intent.getStringExtra("id");
            String artistName = intent.getStringExtra("artist");
            String albumName = intent.getStringExtra("album");
            String trackName = intent.getStringExtra("track");

            repository.insert(getArtistStats(artistName));
            repository.insert(getAlbumStats(albumName));
            repository.insert(getTrackStats(trackName));

            Log.d("SPOTIFY", trackId);
            Log.d("SPOTIFY", artistName);
            Log.d("SPOTIFY", albumName);
            Log.d("SPOTIFY", trackName);
        }
    }

    private Stats getArtistStats(String name){
        Stats stats = new Stats();
        stats.setName(name);
        stats.setType(StatsType.STATS_TYPE_ARTIST);
        return stats;
    }

    private Stats getAlbumStats(String name){
        Stats stats = new Stats();
        stats.setName(name);
        stats.setType(StatsType.STATS_TYPE_ALBUM);
        return stats;
    }

    private Stats getTrackStats(String name){
        Stats stats = new Stats();
        stats.setName(name);
        stats.setType(StatsType.STATS_TYPE_TRACK);
        return stats;
    }
}
