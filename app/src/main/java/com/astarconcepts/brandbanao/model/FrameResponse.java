package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FrameResponse {

    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<FramesModelCategory> framecategories = new ArrayList<>();

    @SerializedName("musiccategories")
    public List<MusicCategoryModel> musiccategories;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FramesModelCategory> getFramecategories() {
        return framecategories;
    }

    public void setFramecategories(List<FramesModelCategory> framecategories) {
        this.framecategories = framecategories;
    }

}
