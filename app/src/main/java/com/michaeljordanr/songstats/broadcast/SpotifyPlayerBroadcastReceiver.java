package com.michaeljordanr.songstats.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.michaeljordanr.songstats.R;
import com.michaeljordanr.songstats.data.local.AppDataBase;
import com.michaeljordanr.songstats.model.ImageSpotifyResponse;
import com.michaeljordanr.songstats.model.SpotifyResponse;
import com.michaeljordanr.songstats.model.Stats;
import com.michaeljordanr.songstats.repository.SpotifyRepository;
import com.michaeljordanr.songstats.repository.StatsRepository;
import com.michaeljordanr.songstats.ui.activity.DashboardActivity;
import com.michaeljordanr.songstats.utils.Constants;
import com.michaeljordanr.songstats.utils.StatsType;
import com.michaeljordanr.songstats.widget.StatsIntentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.michaeljordanr.songstats.utils.Constants.METADATA_CHANGED;
import static com.michaeljordanr.songstats.utils.Constants.SPOTIFY_TOKEN;

public class SpotifyPlayerBroadcastReceiver extends BroadcastReceiver {

    private SpotifyRepository spotifyRepository = SpotifyRepository.getInstance();
    private StatsRepository statsRepository;
    private SharedPreferences sharedPreferences;

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        statsRepository = StatsRepository.getInstance(AppDataBase.getAppDatabase(context));

        String action = intent.getAction();

        if (action != null && action.equals(METADATA_CHANGED)) {
            if (sharedPreferences.getBoolean(context.getString(R.string.pref_spotify_integration_key),
                    context.getResources().getBoolean(R.bool.pref_spotify_integration_default))) {
                String trackId = intent.getStringExtra("id")
                        .replace("spotify:track:", "");
                String artistName = intent.getStringExtra("artist");
                String albumName = intent.getStringExtra("album");
                String trackName = intent.getStringExtra("track");

                List<Stats> statsList = new ArrayList<>();

                if (!artistName.isEmpty()) {
                    statsList.add(getArtistStats(trackId, artistName));
                }

                if (!albumName.isEmpty()) {
                    statsList.add(getAlbumStats(trackId, albumName));
                }

                if (!trackName.isEmpty()) {
                    statsList.add(getTrackStats(trackId, trackName));
                }

                save(statsList);
            }
        }
    }

    private Stats getArtistStats(String trackId, String name) {
        Stats stats = new Stats();
        stats.setTrackId(trackId);
        stats.setName(name);
        stats.setType(StatsType.STATS_TYPE_ARTIST);
        return stats;
    }

    private Stats getAlbumStats(String trackId, String name) {
        Stats stats = new Stats();
        stats.setTrackId(trackId);
        stats.setName(name);
        stats.setType(StatsType.STATS_TYPE_ALBUM);
        return stats;
    }

    private Stats getTrackStats(String trackId, String name) {
        Stats stats = new Stats();
        stats.setTrackId(trackId);
        stats.setName(name);
        stats.setType(StatsType.STATS_TYPE_TRACK);
        return stats;
    }

    private String getToken() {
        if(sharedPreferences.getString(Constants.SPOTIFY_TOKEN, "").isEmpty()){
            refreshToken();
        }
        return sharedPreferences.getString(Constants.SPOTIFY_TOKEN, "");
    }

    private void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPOTIFY_TOKEN, "");
        editor.apply();
    }

    private void save(List<Stats> statsList) {
        final boolean[] error = {false};
        String token = getToken();

        for(Stats stats : statsList) {
            if(!token.isEmpty()) {
                Call<SpotifyResponse> call = spotifyRepository.getImages(stats.getTrackId(), token);

                if (call != null) {
                    call.enqueue(new Callback<SpotifyResponse>() {
                        @Override
                        public void onResponse(Call<SpotifyResponse> call, Response<SpotifyResponse> response) {
                            SpotifyResponse resp = response.body();
                            if (resp != null) {
                                stats.setImageUrl(getBetterThumb(resp.getAlbumResponse().getImages()).getUrl());
                                statsRepository.insert(stats);
                                StatsIntentService.startActionFetchStats(context);
                            } else {
                                Log.d("SONGSTATSD", "ERROR: " + response.errorBody());
                                error[0] = true;
                            }
                        }

                        @Override
                        public void onFailure(Call<SpotifyResponse> call, Throwable t) {
                            Log.d("SONGSTATSD", "Something went wrong...");
                        }
                    });
                } else {
                    error[0] = true;
                }
            }
        }

        if(error[0]){
            refreshToken();
        }
    }

    private ImageSpotifyResponse getBetterThumb(List<ImageSpotifyResponse> images) {
        for (ImageSpotifyResponse image : images) {
            if (image.getWidth() == 300) {
                return image;
            }
        }

        return images.get(0);
    }

    private void refreshToken() {
        clearToken();
        Intent intent = new Intent(context, DashboardActivity.class);
        context.startActivity(intent);
    }
}
