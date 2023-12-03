package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ad_enabled")
    @Expose
    private Integer adEnabled;
    @SerializedName("admob_banner")
    @Expose
    private String admobBanner;
    @SerializedName("admob_inter")
    @Expose
    private String admobInter;
    @SerializedName("admob_reward")
    @Expose
    private String admobReward;
    @SerializedName("admob_native")
    @Expose
    private String admobNative;
    @SerializedName("admob_app_open")
    @Expose
    private String admobAppOpen;
    @SerializedName("admob_app_id")
    @Expose
    private String admobAppId;
    @SerializedName("admob_native_count")
    @Expose
    private Integer admobNativeCount;
    @SerializedName("admob_inter_count")
    @Expose
    private Integer admobInterCount;
    @SerializedName("native_ads_enable")
    @Expose
    private Integer nativeAdsEnable;
    @SerializedName("interstitial_ads_enable")
    @Expose
    private Integer interstitialAdsEnable;
    @SerializedName("rewarded_ads_enable")
    @Expose
    private Integer rewardedAdsEnable;
    @SerializedName("banner_ads_enable")
    @Expose
    private Integer bannerAdsEnable;
    @SerializedName("app_opens_ads_enable")
    @Expose
    private Integer appOpensAdsEnable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdEnabled() {
        return adEnabled;
    }

    public void setAdEnabled(Integer adEnabled) {
        this.adEnabled = adEnabled;
    }

    public String getAdmobBanner() {
        return admobBanner;
    }

    public void setAdmobBanner(String admobBanner) {
        this.admobBanner = admobBanner;
    }

    public String getAdmobInter() {
        return admobInter;
    }

    public void setAdmobInter(String admobInter) {
        this.admobInter = admobInter;
    }

    public String getAdmobReward() {
        return admobReward;
    }

    public void setAdmobReward(String admobReward) {
        this.admobReward = admobReward;
    }

    public String getAdmobNative() {
        return admobNative;
    }

    public void setAdmobNative(String admobNative) {
        this.admobNative = admobNative;
    }

    public String getAdmobAppOpen() {
        return admobAppOpen;
    }

    public void setAdmobAppOpen(String admobAppOpen) {
        this.admobAppOpen = admobAppOpen;
    }

    public String getAdmobAppId() {
        return admobAppId;
    }

    public void setAdmobAppId(String admobAppId) {
        this.admobAppId = admobAppId;
    }

    public Integer getAdmobNativeCount() {
        return admobNativeCount;
    }

    public void setAdmobNativeCount(Integer admobNativeCount) {
        this.admobNativeCount = admobNativeCount;
    }

    public Integer getAdmobInterCount() {
        return admobInterCount;
    }

    public void setAdmobInterCount(Integer admobInterCount) {
        this.admobInterCount = admobInterCount;
    }

    public Integer getNativeAdsEnable() {
        return nativeAdsEnable;
    }

    public void setNativeAdsEnable(Integer nativeAdsEnable) {
        this.nativeAdsEnable = nativeAdsEnable;
    }

    public Integer getInterstitialAdsEnable() {
        return interstitialAdsEnable;
    }

    public void setInterstitialAdsEnable(Integer interstitialAdsEnable) {
        this.interstitialAdsEnable = interstitialAdsEnable;
    }

    public Integer getRewardedAdsEnable() {
        return rewardedAdsEnable;
    }

    public void setRewardedAdsEnable(Integer rewardedAdsEnable) {
        this.rewardedAdsEnable = rewardedAdsEnable;
    }

    public Integer getBannerAdsEnable() {
        return bannerAdsEnable;
    }

    public void setBannerAdsEnable(Integer bannerAdsEnable) {
        this.bannerAdsEnable = bannerAdsEnable;
    }

    public Integer getAppOpensAdsEnable() {
        return appOpensAdsEnable;
    }

    public void setAppOpensAdsEnable(Integer appOpensAdsEnable) {
        this.appOpensAdsEnable = appOpensAdsEnable;
    }

}