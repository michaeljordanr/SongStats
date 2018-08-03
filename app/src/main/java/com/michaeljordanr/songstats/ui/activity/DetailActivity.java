package com.michaeljordanr.songstats.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.michaeljordanr.songstats.R;
import com.michaeljordanr.songstats.ui.fragment.DetailFragment;
import com.michaeljordanr.songstats.utils.Constants;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(getIntent().getStringExtra(Constants.STATS_DETAIL_TYPE) != null) {
            String type = getIntent().getStringExtra(Constants.STATS_DETAIL_TYPE);

            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putString(Constants.STATS_DETAIL_TYPE, type);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_detail, fragment).commit();
        }
    }
}
