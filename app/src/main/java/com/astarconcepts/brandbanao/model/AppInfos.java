package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.SerializedName;

public class AppInfos {

    @SerializedName("AppInfo")
    public AppInfo appInfo;

    @SerializedName("AppUpdate")
    public AppUpdate appUpdate;

    @SerializedName("AdsModel")
    public AdsModel adsModel;

    @SerializedName("Offer")
    public OfferItem offerItem;

    public void setOfferItem(OfferItem offerItem) {
        this.offerItem = offerItem;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public OfferItem getOfferItem() {
        return offerItem;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public AppUpdate getAppUpdate() {
        return appUpdate;
    }

    public void setAppUpdate(AppUpdate appUpdate) {
        this.appUpdate = appUpdate;
    }

    public AdsModel getAdsModel() {
        return adsModel;
    }

    public void setAdsModel(AdsModel adsModel) {
        this.adsModel = adsModel;
    }
}
