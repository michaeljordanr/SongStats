package com.michaeljordanr.songstats.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.michaeljordanr.songstats.BasicApplication;
import com.michaeljordanr.songstats.model.Stats;
import com.michaeljordanr.songstats.repository.SpotifyRepository;
import com.michaeljordanr.songstats.repository.StatsRepository;
import com.michaeljordanr.songstats.utils.StatsType;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.ArrayList;
import java.util.List;

import static com.michaeljordanr.songstats.utils.Constants.CLIENT_ID;

public class StatsViewModel extends AndroidViewModel {

    private StatsRepository statsRepository;
    private SpotifyRepository spotifyRepository;

    private LiveData<List<Stats>> statsObs;

    public StatsViewModel(@NonNull Application application) {
        super(application);
        statsRepository = ((BasicApplication) application).getStatsRepository();
        spotifyRepository = ((BasicApplication) application).getSpotifyRepository();
        getMostListened();
        statsObs = statsRepository.getStats();
    }

    public void insert(Stats stats) {
        statsRepository.insert(stats);
    }

    public void deleteAll() {
        statsRepository.deleteAll();
    }


    public List<Stats> getMostListened() {
        List<Stats> statsList = new ArrayList<>();

        Stats artistStats = statsRepository.getMostListenedStatsByType(StatsType.STATS_TYPE_ARTIST);
        if (artistStats != null) {
            statsList.add(artistStats);
        }

        Stats albumStats = statsRepository.getMostListenedStatsByType(StatsType.STATS_TYPE_ALBUM);
        if (albumStats != null) {
            statsList.add(albumStats);
        }

        Stats trackStats = statsRepository.getMostListenedStatsByType(StatsType.STATS_TYPE_TRACK);
        if (trackStats != null) {
            statsList.add(trackStats);
        }

        return statsList;
    }

    public AuthenticationRequest getAuthenticationRequest(AuthenticationResponse.Type type, String scheme, String host) {
        return new AuthenticationRequest.Builder(CLIENT_ID, type, getRedirectUri(scheme, host).toString())
                .setShowDialog(false)

                .build();
    }

    private Uri getRedirectUri(String scheme, String host) {
        return new Uri.Builder()
                .scheme(scheme)
                .authority(host)
                .build();
    }


    public LiveData<List<Stats>> getStatsByType(String type) {
        return statsRepository.getStatsByType(type);
    }

    public LiveData<List<Stats>> getStatsObs() {
        return statsObs;
    }

}
