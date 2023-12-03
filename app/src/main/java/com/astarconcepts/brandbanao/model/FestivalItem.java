package com.astarconcepts.brandbanao.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class FestivalItem {

    @NonNull
    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String name;

    @SerializedName("image")
    public String image;

    @SerializedName("festivals_date")
    public String date;

    @SerializedName("isActiveStatus")
    public boolean isActive;

    @SerializedName("video")
    public boolean video;


    public FestivalItem(@NonNull String id, String name, String image, String date, boolean isActive, boolean video) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = date;
        this.isActive = isActive;
        this.video = video;
    }


}

