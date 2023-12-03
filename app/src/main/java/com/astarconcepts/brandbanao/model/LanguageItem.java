package com.astarconcepts.brandbanao.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class LanguageItem {

    @NonNull
    @SerializedName("id")
    public String id;
    public String image;
    @SerializedName("title")
    public String title;
    public boolean isChecked;

    public LanguageItem(@NonNull String id, String image, String title, boolean isChecked) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.isChecked = isChecked;
    }


}
