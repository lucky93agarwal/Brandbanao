package com.astarconcepts.brandbanao.model.video;

import com.astarconcepts.brandbanao.custom.animated_video.model.TemplateModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DashboardResponseModel {

    @SerializedName("id")
    public int cat_id;

    @SerializedName("category")
    public String category;

    @SerializedName("template_list")
    public ArrayList<TemplateModel> templateModels;

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<TemplateModel> getTemplateModels() {
        return templateModels;
    }

    public void setTemplateModels(ArrayList<TemplateModel> templateModels) {
        this.templateModels = templateModels;
    }
}
