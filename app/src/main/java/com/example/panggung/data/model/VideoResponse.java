package com.example.panggung.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<Video> videos = null;

    public int getId() {
        return id;
    }

    public List<Video> getVideos() {
        return videos;
    }
}
