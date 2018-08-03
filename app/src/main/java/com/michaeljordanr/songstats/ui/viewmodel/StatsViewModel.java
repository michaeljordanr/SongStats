package com.michaeljordanr.songstats.ui.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.michaeljordanr.songstats.BasicApplication;
import com.michaeljordanr.songstats.model.Stats;
import com.michaeljordanr.songstats.repository.StatsRepository;
import com.michaeljordanr.songstats.utils.StatsType;

import java.util.ArrayList;
import java.util.List;

public class StatsViewModel extends AndroidViewModel {

    private StatsRepository repository;

    private LiveData<List<Stats>> obs;

    private LiveData<List<Stats>> observableStatsDetail;

    private MutableLiveData<List<Stats>> observableMostListened;

    public StatsViewModel(@NonNull Application application) {
        super(application);
        repository = ((BasicApplication) application).getRepository();
        getMostListened();
        obs = repository.getStats();
    }

    public void insert(Stats stats){
        repository.insert(stats);
    }

    public void deleteAll(){
        repository.deleteAll();
    }


    public List<Stats> getMostListened(){
        observableMostListened = new MutableLiveData<>();
        List<Stats> statsList = new ArrayList<>();

        Stats artistStats = repository.getMostListenedStatsByType(StatsType.STATS_TYPE_ARTIST);
        if(artistStats != null) {
            statsList.add(artistStats);
        }

        Stats albumStats = repository.getMostListenedStatsByType(StatsType.STATS_TYPE_ALBUM);
        if(albumStats != null) {
            statsList.add(albumStats);
        }

        Stats trackStats = repository.getMostListenedStatsByType(StatsType.STATS_TYPE_TRACK);
        if(trackStats != null){
            statsList.add(trackStats);
        }

        return statsList;
    }

    public LiveData<List<Stats>> getStatsByType(String type){
        return repository.getStatsByType(type);
    }

    public LiveData<List<Stats>> getObs() {
        return obs;
    }

}
