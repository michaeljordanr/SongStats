package com.michaeljordanr.songstats.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.michaeljordanr.songstats.R;
import com.michaeljordanr.songstats.adapter.DashboardAdapter;
import com.michaeljordanr.songstats.broadcast.SpotifyPlayerBroadcastReceiver;
import com.michaeljordanr.songstats.databinding.ActivityDashboardBinding;
import com.michaeljordanr.songstats.model.Stats;
import com.michaeljordanr.songstats.ui.viewmodel.StatsViewModel;
import com.michaeljordanr.songstats.utils.Constants;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.ArrayList;
import java.util.List;

import static com.michaeljordanr.songstats.utils.Constants.AUTH_TOKEN_REQUEST_CODE;
import static com.michaeljordanr.songstats.utils.Constants.SPOTIFY_TOKEN;

public class DashboardActivity extends AppCompatActivity implements DashboardAdapter.DashboardAdapterOnClickListener,
SharedPreferences.OnSharedPreferenceChangeListener{

    private SharedPreferences sharedPreferences;
    private ActivityDashboardBinding binding;
    private StatsViewModel statsViewModel;

    private DashboardAdapter adapter;
    private List<Stats> statsList = new ArrayList<>();

    private SpotifyPlayerBroadcastReceiver receiver;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        statsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        adapter = new DashboardAdapter(this, this);
        binding.rvStats.setAdapter(adapter);
        binding.rvStats.setLayoutManager(new LinearLayoutManager(this));

        binding.ibSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            receiver = new SpotifyPlayerBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.METADATA_CHANGED);

            registerReceiver(receiver, intentFilter);
        }

        subscribeObservable();
        loadAdsBanner();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        SharedPreferences prefs = getSharedPreferences(Constants.SONGSTATSPREFERENCES, MODE_PRIVATE);

        if (prefs.getString(Constants.SPOTIFY_TOKEN, "").isEmpty()) {
            if(sharedPreferences.getBoolean(getString(R.string.pref_spotify_integration_key), true)) {
                onRequestSpotifyToken();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            unregisterReceiver(receiver);
        }
    }

    private void subscribeObservable() {
        statsViewModel.getStatsObs().observe(this, stats -> {
            if (stats != null) {

                statsList = statsViewModel.getMostListened();
                if (statsList.size() > 0) {
                    adapter.setData(statsList);
                }

                for (Stats s : stats) {
                    Log.d("STATS", s.getName() + "| type: " + s.getType() + "| count: " + s.getTimesListened());
                }
            }
        });
    }

    @Override
    public void onClick(String type) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.STATS_DETAIL_TYPE, type);
        startActivity(intent);
    }

    private void onRequestSpotifyToken() {
        String scheme = getString(R.string.com_spotify_sdk_redirect_scheme);
        String host = getString(R.string.com_spotify_sdk_redirect_host);

        final AuthenticationRequest request = statsViewModel
                .getAuthenticationRequest(AuthenticationResponse.Type.TOKEN, scheme, host);
        AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SPOTIFY_TOKEN, response.getAccessToken());
            editor.apply();

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "getToken");
            bundle.putString(FirebaseAnalytics.Param.SUCCESS, "true");
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    private void loadAdsBanner() {
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        binding.adView.loadAd(adRequest);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!sharedPreferences.getBoolean(getString(R.string.pref_spotify_integration_key),
                getResources().getBoolean(R.bool.pref_spotify_integration_default))){
            clearToken();
        } else {
            if(sharedPreferences.getString(Constants.SPOTIFY_TOKEN, "").isEmpty()) {
                onRequestSpotifyToken();
            }
        }
    }

    private void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPOTIFY_TOKEN, "");
        editor.apply();
    }
}
