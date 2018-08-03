package com.michaeljordanr.songstats.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.michaeljordanr.songstats.R;
import com.michaeljordanr.songstats.adapter.DashboardAdapter;
import com.michaeljordanr.songstats.databinding.ActivityDashboardBinding;
import com.michaeljordanr.songstats.model.Stats;
import com.michaeljordanr.songstats.ui.viewmodel.StatsViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private StatsViewModel statsViewModel;

    private DashboardAdapter adapter;
    private List<Stats> statsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        statsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);

        adapter = new DashboardAdapter(getBaseContext());
        binding.rvStats.setAdapter(adapter);
        binding.rvStats.setLayoutManager(new LinearLayoutManager(this));

        subscribeObservable();
    }

    private void subscribeObservable() {
        statsViewModel.getObs().observe(this, stats -> {
            if (stats != null) {

                statsList = statsViewModel.getMostListened();
                if(statsList.size() > 0) {
                    adapter.setData(statsList);
                }

                for (Stats s : stats) {
                    Log.d("STATS", s.getName() + "| type: " + s.getType() + "| count: " + s.getTimesListened());
                }
            }
        });
    }

}
