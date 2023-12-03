package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FramesModelCategory {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    @SerializedName("frames")
    List<FrameModel> frameModels;

    public FramesModelCategory(String id, String name, List<FrameModel> frames) {
        this.id = id;
        this.name = name;
        this.frameModels = frames;
    }

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

    public List<FrameModel> getFrameModels() {
        return frameModels;
    }

    public void setFrameModels(List<FrameModel> frameModels) {
        this.frameModels = frameModels;
    }
}
