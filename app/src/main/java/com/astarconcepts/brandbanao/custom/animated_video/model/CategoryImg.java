package com.astarconcepts.brandbanao.custom.animated_video.model;

import com.google.gson.annotations.SerializedName;

public class CategoryImg {
    @SerializedName("id")
    String id;
    @SerializedName("category")
    String category;
    @SerializedName("image_url")
    String image_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
