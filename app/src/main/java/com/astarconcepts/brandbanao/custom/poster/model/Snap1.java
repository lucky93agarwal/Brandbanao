package com.astarconcepts.brandbanao.custom.poster.model;

import java.util.ArrayList;

public class Snap1 {
    private int mGravity;
    private String mText;
    private String ratio;
    private ArrayList<ThumbnailThumbFull> thumbnailThumbFulls;

    public Snap1(int mGravity, String mText, ArrayList<ThumbnailThumbFull> arrayList, String ratio) {

        this.mGravity = mGravity;
        this.mText = mText;
        this.thumbnailThumbFulls = arrayList;
        this.ratio = ratio;

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

    public ArrayList<ThumbnailThumbFull> getPosterThumbFulls() {
        return this.thumbnailThumbFulls;
    }
}


