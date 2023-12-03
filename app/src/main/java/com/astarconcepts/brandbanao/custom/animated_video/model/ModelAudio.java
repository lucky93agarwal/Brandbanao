package com.astarconcepts.brandbanao.custom.animated_video.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class ModelAudio {

    @SerializedName("id")
    int id;
    @SerializedName("title")
    String audioTitle;
    @SerializedName("audio_url")
    String audioUrl;
    String audioDuration;
    String audioArtist;
    Uri audioUri;


    public String getAudioTitle() {
        return audioTitle;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getaudioTitle() {
        return audioTitle;
    }

    public void setaudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getaudioDuration() {
        return audioDuration;
    }

    public void setaudioDuration(String audioDuration) {
        this.audioDuration = audioDuration;
    }

    public String getaudioArtist() {
        return audioArtist;
    }

    public void setaudioArtist(String audioArtist) {
        this.audioArtist = audioArtist;
    }

    public Uri getaudioUri() {
        return audioUri;
    }

    public void setaudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }

}