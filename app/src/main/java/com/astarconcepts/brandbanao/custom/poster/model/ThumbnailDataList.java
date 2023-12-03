package com.astarconcepts.brandbanao.custom.poster.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ThumbnailDataList {

    @SerializedName("poster_list")
    ArrayList<ThumbnailThumbFull> poster_list;
    @SerializedName("post_id")
    private String cat_id;
    @SerializedName("cat_name")
    private String cat_name;
    @SerializedName("thumb_img")
    private String thumb_img;

    @SerializedName("template_w_h_ratio")
    private String template_w_h_ratio;


    public String getTemplate_w_h_ratio() {
        return template_w_h_ratio;
    }

    public void setTemplate_w_h_ratio(String template_w_h_ratio) {
        this.template_w_h_ratio = template_w_h_ratio;
    }


    public String getCat_id() {
        return this.cat_id;
    }

    public void setCat_id(String str) {
        this.cat_id = str;
    }

    public String getThumb_img() {
        return this.thumb_img;
    }

    public void setThumb_img(String str) {
        this.thumb_img = str;
    }

    public String getCat_name() {
        return this.cat_name;
    }

    public void setCat_name(String str) {
        this.cat_name = str;
    }

    public ArrayList<ThumbnailThumbFull> getPoster_list() {
        return this.poster_list;
    }

    public void setPoster_list(ArrayList<ThumbnailThumbFull> arrayList) {
        this.poster_list = arrayList;
    }

}
