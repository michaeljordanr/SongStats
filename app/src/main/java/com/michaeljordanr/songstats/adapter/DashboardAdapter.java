package com.michaeljordanr.songstats.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaeljordanr.songstats.databinding.ItemDashboardRecyclerViewBinding;
import com.michaeljordanr.songstats.model.Stats;
import com.michaeljordanr.songstats.utils.StatsType;

import java.util.ArrayList;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

    private Context context;
    private List<Stats> statsList;
    private final DashboardAdapterOnClickListener listener;

    public interface DashboardAdapterOnClickListener{
        void onClick(String type);
    }

    public DashboardAdapter(Context context, DashboardAdapterOnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemDashboardRecyclerViewBinding binding =
                ItemDashboardRecyclerViewBinding.inflate(layoutInflater, parent, false);

        return new DashboardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        Stats stats = statsList.get(position);
        holder.bind(stats);
    }

    @Override
    public int getItemCount() {
        if(statsList == null) return 0;
        return statsList.size();
    }

    public void setData(List<Stats> statsList){
        this.statsList = statsList;
        notifyDataSetChanged();
    }

    class DashboardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ItemDashboardRecyclerViewBinding binding;

        DashboardViewHolder(ItemDashboardRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        void bind(Stats stats){
            binding.setStats(stats);
            binding.executePendingBindings();

            binding.tvTitle.setText(StatsType.getTypeDescription(context, stats.getType()));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Stats stats = statsList.get(position);
            listener.onClick(stats.getType());
        }
    }
}
