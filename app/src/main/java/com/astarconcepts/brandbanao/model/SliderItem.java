package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.SerializedName;

public class SliderItem {

    @SerializedName("banner_name")
    public String sliderName;

    @SerializedName("banner_url")
    public String sliderUrl;

    @SerializedName("banner_image")
    public String sliderImage;

    @SerializedName("banner_value")
    public String bannerValue;


    @SerializedName("category_name")
    public String category_name;

    @SerializedName("banner_type")
    public String sliderType;

    public SliderItem() {
    }


    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSliderName() {
        return sliderName;
    }

    public void setSliderName(String sliderName) {
        this.sliderName = sliderName;
    }

    public String getSliderUrl() {
        return sliderUrl;
    }

    public void setSliderUrl(String sliderUrl) {
        this.sliderUrl = sliderUrl;
    }

    public String getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }

    public String getBannerValue() {
        return bannerValue;
    }

    public void setBannerValue(String bannerValue) {
        this.bannerValue = bannerValue;
    }

    public String getSliderType() {
        return sliderType;
    }

    public void setSliderType(String sliderType) {
        this.sliderType = sliderType;
    }
}
