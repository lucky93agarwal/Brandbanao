package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.SerializedName;

public class MusicModel {

    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("thumb_url")
    public String thumbUrl;

    @SerializedName("audio_url")
    public String audio_url;
    @SerializedName("category_id")
    public int category_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }


    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

}
