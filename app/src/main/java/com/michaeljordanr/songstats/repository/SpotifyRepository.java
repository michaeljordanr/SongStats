package com.michaeljordanr.songstats.repository;

import android.util.Log;

import com.michaeljordanr.songstats.data.remote.RetrofitClientInstance;
import com.michaeljordanr.songstats.data.remote.SpotifyService;
import com.michaeljordanr.songstats.model.SpotifyResponse;

import retrofit2.Call;
import retrofit2.Retrofit;

public class SpotifyRepository {
    private static SpotifyRepository sInstance;
    private Retrofit retrofit;

    private SpotifyRepository() {
        retrofit = RetrofitClientInstance.getRetrofitInstance();
    }

    public static SpotifyRepository getInstance() {
        if (sInstance == null) {
            synchronized (SpotifyRepository.class) {
                if (sInstance == null) {
                    sInstance = new SpotifyRepository();
                }
            }
        }
        return sInstance;
    }

    public Call<SpotifyResponse> getImages(String trackId, String token) {
        if (!token.isEmpty()) {
            SpotifyService service = retrofit.create(SpotifyService.class);

            Call<SpotifyResponse> call = service.imagesList(trackId, "Bearer " + token);


            return call;
        } else {
            Log.d("SONGSTATSD", "No token");
        }

        return null;
    }
}
