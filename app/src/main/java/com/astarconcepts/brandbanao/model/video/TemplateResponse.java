package com.astarconcepts.brandbanao.model.video;

import com.astarconcepts.brandbanao.custom.animated_video.model.TemplateModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TemplateResponse {
    @SerializedName("msg")
    public ArrayList<TemplateModel> msg;
    @SerializedName("records")
    public ArrayList<TemplateModel> records;
    @SerializedName("code")
    String code;
    @SerializedName("key")
    String key;
    @SerializedName("have_next_page")
    String have_next_page;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<TemplateModel> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<TemplateModel> records) {
        this.records = records;
    }

    public String getHave_next_page() {
        return have_next_page;
    }

    public void setHave_next_page(String have_next_page) {
        this.have_next_page = have_next_page;
    }

    public ArrayList<TemplateModel> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<TemplateModel> msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
