package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MusicCategoryModel {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;


    public String status;
    public String updated_at;
    public String created_at;
    @SerializedName("musicList")
    public List<MusicModel> musicList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<MusicModel> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<MusicModel> musicList) {
        this.musicList = musicList;
    }
}
