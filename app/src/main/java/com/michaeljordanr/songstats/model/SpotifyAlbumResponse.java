package com.michaeljordanr.songstats.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpotifyAlbumResponse {
    @SerializedName("images")
    List<ImageSpotifyResponse> images;

    public List<ImageSpotifyResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageSpotifyResponse> images) {
        this.images = images;
    }
}
