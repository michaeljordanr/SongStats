package com.michaeljordanr.songstats.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.michaeljordanr.songstats.R;
import com.michaeljordanr.songstats.ui.fragment.DetailFragment;
import com.michaeljordanr.songstats.utils.Constants;
import com.michaeljordanr.songstats.utils.StatsType;

public class DetailActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageButton ibBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tv_title_toolbar);
        ibBack = findViewById(R.id.ib_back);

        ibBack.setOnClickListener(v -> finish());


        if (getIntent().getStringExtra(Constants.STATS_DETAIL_TYPE) != null) {
            String type = getIntent().getStringExtra(Constants.STATS_DETAIL_TYPE);

            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString(Constants.STATS_DETAIL_TYPE, type);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_detail, fragment).commit();

            setTitle(StatsType.getTypeDescription(this, type));
        }
    }

    private void setTitle(String title) {
        tvTitle.setText(String.format(getString(R.string.detail_stats_title), title).toUpperCase());
    }
}
