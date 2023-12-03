package com.astarconcepts.brandbanao.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BusinessItem implements Serializable {

    @SerializedName("id")
    public String businessid;
    @SerializedName("name")
    public String name;
    @SerializedName("tagline")
    public String tagline;
    @SerializedName("logo")
    public String logo;
    @SerializedName("email")
    public String email;
    @SerializedName("mobile_no")
    public String phone;
    @SerializedName("website")
    public String website;
    @SerializedName("address")
    public String address;
    @SerializedName("category_name")
    public String businesscategory;

    @SerializedName("category_id")
    public String categoryId;
    @SerializedName("social_youtube")
    public String social_youtube;
    @SerializedName("social_twitter")
    public String social_twitter;
    @SerializedName("social_instagram")
    public String social_instagram;
    @SerializedName("social_facebook")
    public String social_facebook;
    @SerializedName("is_default")
    public Boolean isDefault;

    public BusinessItem() {
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getSocial_youtube() {
        return social_youtube;
    }

    public void setSocial_youtube(String social_youtube) {
        this.social_youtube = social_youtube;
    }

    public String getSocial_twitter() {
        return social_twitter;
    }

    public void setSocial_twitter(String social_twitter) {
        this.social_twitter = social_twitter;
    }

    public String getSocial_instagram() {
        return social_instagram;
    }

    public void setSocial_instagram(String social_instagram) {
        this.social_instagram = social_instagram;
    }

    public String getSocial_facebook() {
        return social_facebook;
    }

    public void setSocial_facebook(String social_facebook) {
        this.social_facebook = social_facebook;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getBusinessCategory() {
        return businesscategory;
    }

    public void setBusinesscategory(String businesscategory) {
        this.businesscategory = businesscategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
