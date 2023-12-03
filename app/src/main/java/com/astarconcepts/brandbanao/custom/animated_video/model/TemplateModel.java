package com.astarconcepts.brandbanao.custom.animated_video.model;

import com.google.gson.annotations.SerializedName;


public class TemplateModel {

    @SerializedName("id")
    String id;
    @SerializedName("code")
    String code;

    @SerializedName("title")
    String title;
    @SerializedName("no_of_image")
    String resImageNum;

    @SerializedName("zip_link")
    String zip_link;

    @SerializedName("data_html")
    String data_html;

    @SerializedName("template_type")
    String template_type;

    @SerializedName("template_json")
    String template_json;

    @SerializedName("category")
    String category;

    @SerializedName("created")
    int created;

    boolean isTemplate;

    @SerializedName("template_total")
    int template_total;


    @SerializedName("images_path")
    String images_path;


    @SerializedName("views")
    int views;

    @SerializedName("premium")
    boolean premium;



    @SerializedName("zip_path_preview")
    String zip_link_preview;


    public String getImages_path() {
        return images_path;
    }

    public void setImages_path(String images_path) {
        this.images_path = images_path;
    }

    public String getData_html() {
        return data_html;
    }

    public void setData_html(String data_html) {
        this.data_html = data_html;
    }

    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public int getTemplate_total() {
        return template_total;
    }

    public void setTemplate_total(int template_total) {
        this.template_total = template_total;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getTemplate_json() {
        return template_json;
    }

    public void setTemplate_json(String template_json) {
        this.template_json = template_json;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResImageNum() {
        return resImageNum;
    }

    public void setResImageNum(String resImageNum) {
        this.resImageNum = resImageNum;
    }

    public String getZip_link() {
        return zip_link;
    }

    public void setZip_link(String zip_link) {
        this.zip_link = zip_link;


    }


    public String getZip_link_preview() {
        return zip_link_preview;
    }

    public void setZip_link_preview(String zip_link_preview) {
        this.zip_link_preview = zip_link_preview;
    }


    public String getTemplate_Json() {
        return template_json;
    }

    public void setTemplate_Json(String template_json) {
        this.template_json = template_json;
    }


}
