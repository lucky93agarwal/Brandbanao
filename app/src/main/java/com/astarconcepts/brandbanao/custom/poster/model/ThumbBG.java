package com.astarconcepts.brandbanao.custom.poster.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ThumbBG {

    @SerializedName("thumbnail_bg")
    ArrayList<MainBG> thumbnail_bg;

    public ArrayList<MainBG> getThumbnail_bg() {
        return this.thumbnail_bg;
    }

}
