package com.michaeljordanr.songstats.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.TextView;

import com.michaeljordanr.songstats.R;
import com.michaeljordanr.songstats.ui.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);

        TextView tvTitle = toolbar.findViewById(R.id.tv_title_toolbar);
        ImageButton ibBack = toolbar.findViewById(R.id.ib_back);
        tvTitle.setText(getString(R.string.settings));
        ibBack.setOnClickListener(v -> finish());

        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_settings, new SettingsFragment())
                .commit();
    }
}
