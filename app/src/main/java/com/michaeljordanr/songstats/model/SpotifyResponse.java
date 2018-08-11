package com.michaeljordanr.songstats.model;

import com.google.gson.annotations.SerializedName;

public class SpotifyResponse {
    @SerializedName("album")
    private SpotifyAlbumResponse albumResponse;

    public SpotifyAlbumResponse getAlbumResponse() {
        return albumResponse;
    }

    public void setAlbumResponse(SpotifyAlbumResponse albumResponse) {
        this.albumResponse = albumResponse;
    }
}
