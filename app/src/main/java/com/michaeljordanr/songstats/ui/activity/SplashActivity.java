package com.michaeljordanr.songstats.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.michaeljordanr.songstats.R;

public class SplashActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler h = new Handler();
        h.postDelayed(this, 1000);
    }

    @Override
    public void run() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}
