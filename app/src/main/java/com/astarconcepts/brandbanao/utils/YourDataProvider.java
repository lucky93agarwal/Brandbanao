package com.astarconcepts.brandbanao.utils;

import android.util.Log;

import com.astarconcepts.brandbanao.custom.poster.model.BackgroundImage;

import java.util.ArrayList;


public class YourDataProvider {
    private ArrayList<BackgroundImage> mCategoryLists = new ArrayList<>();
    private ArrayList<BackgroundImage> mLoadMoreItems = new ArrayList<>();
    private ArrayList<Object> mLoadMorePosterItems = new ArrayList<>();
    private ArrayList<BackgroundImage> mLoadMoreStickerItems = new ArrayList<>();
    private ArrayList<Object> mObjects = new ArrayList<>();
    private ArrayList<BackgroundImage> mStickerCategoryLists = new ArrayList<>();

    public ArrayList<BackgroundImage> getLoadMoreItems() {
        int size = this.mLoadMoreItems.size();
        for (int i = size; i < size + 16; i++) {
            if (i < this.mCategoryLists.size()) {
                this.mLoadMoreItems.add(this.mCategoryLists.get(i));
            }
        }
        return this.mLoadMoreItems;
    }

    public ArrayList<BackgroundImage> getLoadMoreStickerItems() {
        int size = this.mLoadMoreStickerItems.size();
        Log.i("DATA", "DATA STICKER-->" + this.mLoadMoreStickerItems.size());

        for (int i = size; i < size + 20; i++) {
            if (i < this.mStickerCategoryLists.size()) {
                this.mLoadMoreStickerItems.add(this.mStickerCategoryLists.get(i));
            }
        }
        return this.mLoadMoreStickerItems;
    }

    public ArrayList<Object> getLoadMorePosterItems() {
        int size = this.mLoadMorePosterItems.size();
        for (int i = size; i < size + 10; i++) {
            if (i < this.mObjects.size()) {
                this.mLoadMorePosterItems.add(this.mObjects.get(i));
            }
        }
        return this.mLoadMorePosterItems;
    }

    public ArrayList<BackgroundImage> getLoadMoreStickerItemsS() {
        int size = this.mLoadMoreStickerItems.size();
        for (int i = size; i < size + 20; i++) {
            if (i < this.mStickerCategoryLists.size()) {
                this.mLoadMoreStickerItems.add(this.mStickerCategoryLists.get(i));
            }
        }
        return this.mLoadMoreStickerItems;
    }

    public void setBackgroundList(ArrayList<BackgroundImage> arrayList) {
        this.mCategoryLists = arrayList;
    }

    public void setPosterList(ArrayList<Object> arrayList) {
        this.mObjects = arrayList;
    }

    public void setStickerList(ArrayList<BackgroundImage> arrayList) {
        this.mStickerCategoryLists = arrayList;
    }
}
