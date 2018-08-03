package com.michaeljordanr.songstats.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaeljordanr.songstats.R;
import com.michaeljordanr.songstats.adapter.DetailAdapter;
import com.michaeljordanr.songstats.databinding.FragmentDetailBinding;
import com.michaeljordanr.songstats.ui.viewmodel.StatsViewModel;
import com.michaeljordanr.songstats.utils.Constants;
import com.michaeljordanr.songstats.utils.StatsType;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    private String type;
    private DetailAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);

        if(getArguments() != null) {
            type = getArguments().getString(Constants.STATS_DETAIL_TYPE);
            binding.rvDetailStats.setLayoutManager(new LinearLayoutManager(getContext()));
            setTitle(StatsType.getTypeDescription(getContext(), type));
        }

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final StatsViewModel model = ViewModelProviders.of(this).get(StatsViewModel.class);
        adapter = new DetailAdapter();
        subscribeToModel(model);
    }

    private void subscribeToModel(final StatsViewModel model){
        model.getStatsByType(type).observe(this, stats -> {
            adapter.setData(stats);
            binding.rvDetailStats.setAdapter(adapter);

            binding.executePendingBindings();
        });
    }

    private void setTitle(String title){
        binding.tvStatsTitle.setText(String.format(getString(R.string.detail_stats_title), title));
    }
}
