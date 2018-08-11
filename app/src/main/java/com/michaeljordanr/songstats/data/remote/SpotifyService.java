package com.michaeljordanr.songstats.data.remote;

import com.michaeljordanr.songstats.model.SpotifyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface SpotifyService {
    @GET("tracks/{trackId}")
    Call<SpotifyResponse> imagesList(@Path("trackId") String trackId,
                                     @Header("Authorization") String authHeader);
}
