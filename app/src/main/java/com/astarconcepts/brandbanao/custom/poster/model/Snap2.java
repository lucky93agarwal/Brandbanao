package com.astarconcepts.brandbanao.custom.poster.model;

import java.util.ArrayList;

public class Snap2 {
    int cat_id;
    private int mGravity;
    private String mText;
    private String ratio;
    private ArrayList<BackgroundImage> thumbnailThumbFulls;

    public Snap2(int i, String str, ArrayList<BackgroundImage> arrayList, int i2, String str2) {
        this.mGravity = i;
        this.mText = str;
        this.thumbnailThumbFulls = arrayList;
        this.cat_id = i2;
        this.ratio = str2;
    }

    public String getRatio() {
        return this.ratio;
    }

    public void setRatio(String str) {
        this.ratio = str;
    }

    public String getText() {
        return this.mText;
    }

    public int getGravity() {
        return this.mGravity;
    }

    public ArrayList<BackgroundImage> getPosterThumbFulls() {
        return this.thumbnailThumbFulls;
    }

    public int getCat_id() {
        return this.cat_id;
    }
}
