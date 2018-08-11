package com.michaeljordanr.songstats.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.michaeljordanr.songstats.databinding.ItemDetailRecyclerViewBinding;
import com.michaeljordanr.songstats.model.Stats;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    private Context context;
    private List<Stats> statsList;

    public DetailAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemDetailRecyclerViewBinding binding =
                ItemDetailRecyclerViewBinding.inflate(layoutInflater, parent, false);

        return new DetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        Stats stats = statsList.get(position);
        holder.bind(stats);
    }

    @Override
    public int getItemCount() {
        if (statsList == null) return 0;
        return statsList.size();
    }

    public void setData(List<Stats> statsList) {
        this.statsList = statsList;
        notifyDataSetChanged();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        private final ItemDetailRecyclerViewBinding binding;

        DetailViewHolder(ItemDetailRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Stats stats) {
            binding.setStats(stats);
            binding.executePendingBindings();

            Picasso.with(context)
                    .load(stats.getImageUrl())
                    .into(binding.ivMostListened);
        }
    }
}
