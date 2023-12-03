package com.astarconcepts.brandbanao.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "favoritelist")
public class FavoriteList {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "code")
    String code;

    @ColumnInfo(name = "poster_id")
    int poster_id;


    @ColumnInfo(name = "video_id")
    int video_id;

    @ColumnInfo(name = "type")
    int type;

    @ColumnInfo(name = "title")
    String title;

    @ColumnInfo(name = "no_of_image")
    String resImageNum;


    @ColumnInfo(name = "post_thumb")
    String post_thumb;

    @ColumnInfo(name = "data_html")
    String data_html;

    @ColumnInfo(name = "zip_link")
    String zip_link;

    @SerializedName("zip_link_preview")
    String zip_link_preview;

    @ColumnInfo(name = "template_json")
    String template_json;

    @ColumnInfo(name = "category")
    String category;

    @ColumnInfo(name = "premium")
    boolean premium;

    @ColumnInfo(name = "created")
    int created;

    @ColumnInfo(name = "views")
    int views;


    @ColumnInfo(name = "text_json")
    String text_json;
    @ColumnInfo(name = "template_type")
    String template_type;
    @SerializedName("template_w_h_ratio")
    private String template_w_h_ratio;

    public String getText_json() {
        return text_json;
    }

    public void setText_json(String text_json) {
        this.text_json = text_json;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public int getPoster_id() {
        return poster_id;
    }

    public void setPoster_id(int poster_id) {
        this.poster_id = poster_id;
    }

    public String getTemplate_w_h_ratio() {
        return template_w_h_ratio;
    }

    public void setTemplate_w_h_ratio(String template_w_h_ratio) {
        this.template_w_h_ratio = template_w_h_ratio;
    }


    public String getPost_thumb() {
        return post_thumb;
    }

    public void setPost_thumb(String post_thumb) {
        this.post_thumb = post_thumb;
    }

    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public String getData_html() {
        return data_html;
    }

    public void setData_html(String data_html) {
        this.data_html = data_html;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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


    public String getTemplate_json() {
        return template_json;
    }

    public void setTemplate_json(String template_json) {
        this.template_json = template_json;
    }


}
