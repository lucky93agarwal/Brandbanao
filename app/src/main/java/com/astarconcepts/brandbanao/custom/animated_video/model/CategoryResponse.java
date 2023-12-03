package com.astarconcepts.brandbanao.custom.animated_video.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryResponse {
    @SerializedName("msg")
    public ArrayList<CategoryImg> msg;
    @SerializedName("code")
    String code;

    public ArrayList<CategoryImg> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<CategoryImg> msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
