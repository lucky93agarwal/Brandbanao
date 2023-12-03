package com.astarconcepts.brandbanao.model.video;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DashboardTemplateResponseMain {

    @SerializedName("data")
    public ArrayList<DashboardResponseModel> data;
    @SerializedName("code")
    String code;

    public ArrayList<DashboardResponseModel> getData() {
        return data;
    }

}
