package com.astarconcepts.brandbanao.model;

import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceItem {

    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("type")
    public String type;

    @SerializedName("description")
    public String description;


    @SerializedName("url")
    public String url;

    @SerializedName("icon")
    public String icon;

    @TypeConverters
    @SerializedName("detail")
    public List<String> pointItemList;

    public ServiceItem() {
    }


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getPointItemList() {
        return pointItemList;
    }

    public void setPointItemList(List<String> pointItemList) {
        this.pointItemList = pointItemList;
    }
}
