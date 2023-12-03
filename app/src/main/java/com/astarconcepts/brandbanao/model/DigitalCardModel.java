package com.astarconcepts.brandbanao.model;

import com.astarconcepts.brandbanao.custom.poster.model.ThumbnailCo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DigitalCardModel {


    @SerializedName("id")
    public int id;

    @SerializedName("user_id")
    private int userId;


    @SerializedName("business_card_id")
    private int business_card_id;

    @SerializedName("card")
    private String card;

    @SerializedName("premium")
    private boolean premium;

    @SerializedName("name")
    public String name;

    @SerializedName("image")
    public String image;

    public int get_id() {
        return id;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void set_id(int id) {
        this.id = id;
    }


    public int getBusinessCardId() {
        return business_card_id;
    }

    public void setBusinessCardId(int id) {
        this.business_card_id = id;
    }


    public String getCard() {
        return card;
    }

    public void setCard(String id) {
        this.card = id;
    }

    public int getUser_id() {
        return userId;
    }

    public void setUser_id(int id) {
        this.userId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
