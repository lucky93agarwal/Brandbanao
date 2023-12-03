package com.astarconcepts.brandbanao.custom.poster.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ThumbnailCo implements Parcelable {

    public static final Creator<ThumbnailCo> CREATOR = new Creator<ThumbnailCo>() {
        @Override
        public ThumbnailCo createFromParcel(Parcel source) {
            return new ThumbnailCo(source);
        }

        @Override
        public ThumbnailCo[] newArray(int size) {
            return new ThumbnailCo[size];
        }
    };


    @SerializedName("title")
    String title;

    @SerializedName("tag")
    String tag;

    @SerializedName("post_zip")
    String post_zip;
    @SerializedName("user_name")
    String user_name;
    @SerializedName("created")
    int created;
    @SerializedName("template_total")
    int template_total;
    @SerializedName("views")
    int views;
    @SerializedName("premium")
    boolean premium;
    @SerializedName("template_type")
    int template_type;
    @SerializedName("template_w_h_ratio")
    String template_w_h_ratio;
    private String back_image;
    private String cat_id;
    private String post_id;
    private String post_thumb;
    private String ratio;
    private ArrayList<Sticker_info> sticker_info;
    private ArrayList<Text_infoposter> text_infoposter;
    @SerializedName("text_info1")
    private String text_info1;
    @SerializedName("text_json")
    private String text_json;
    protected ThumbnailCo(Parcel in) {
        this.back_image = in.readString();
        this.cat_id = in.readString();
        this.post_id = in.readString();
        this.post_thumb = in.readString();
        this.ratio = in.readString();
        this.sticker_info = in.createTypedArrayList(Sticker_info.CREATOR);
        this.text_infoposter = in.createTypedArrayList(Text_infoposter.CREATOR);
        this.text_info1 = in.readString();
        this.title = in.readString();
        this.tag = in.readString();
        this.user_name = in.readString();
        this.created = in.readInt();
        this.template_type = in.readInt();
        this.template_w_h_ratio = in.readString();
        this.template_total = in.readInt();
        this.views = in.readInt();
    }

    public String getPost_zip() {
        return post_zip;
    }

    public void setPost_zip(String post_zip) {
        this.post_zip = post_zip;
    }

    public boolean isPremium() {
        return premium;
    }

    public String getText_json() {
        return text_json;
    }

    public void setText_json(String text_json) {
        this.text_json = text_json;
    }

    public String getTemplate_w_h_ratio() {
        return template_w_h_ratio;
    }

    public void setTemplate_w_h_ratio(String template_w_h_ratio) {
        this.template_w_h_ratio = template_w_h_ratio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(int template_type) {
        this.template_type = template_type;
    }

    public int getTemplate_total() {
        return template_total;
    }

    public void setTemplate_total(int template_total) {
        this.template_total = template_total;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }


    public String getCat_id() {
        return this.cat_id;
    }

    public void setCat_id(String str) {
        this.cat_id = str;
    }

    public String getRatio() {
        return this.ratio;
    }

    public void setRatio(String str) {
        this.ratio = str;
    }

    public String getBack_image() {
        return this.back_image;
    }

    public void setBack_image(String str) {
        this.back_image = str;
    }

    public String getPost_id() {
        return this.post_id;
    }

    public void setPost_id(String str) {
        this.post_id = str;
    }

    public String getPost_thumb() {
        return this.post_thumb;
    }

    public void setPost_thumb(String str) {
        this.post_thumb = str;
    }

    public ArrayList<Text_infoposter> getText_info() {
        return this.text_infoposter;
    }

    public void setText_info(ArrayList<Text_infoposter> arrayList) {
        this.text_infoposter = arrayList;
    }

    public ArrayList<Sticker_info> getSticker_info() {
        return this.sticker_info;
    }

    public void setSticker_info(ArrayList<Sticker_info> arrayList) {
        this.sticker_info = arrayList;
    }

    public String toString() {
        return "ClassPojo [cat_id = " + this.cat_id + ", ratio = " + this.ratio + ", back_image = " + this.back_image + ", post_id = " + this.post_id + ", post_thumb = " + this.post_thumb + ", text_info = " + this.text_infoposter + ", sticker_info = " + this.sticker_info + "]";
    }

    public String getText_info1() {
        return text_info1;
    }

    public void setText_info1(String text_info1) {
        this.text_info1 = text_info1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.back_image);
        dest.writeString(this.cat_id);
        dest.writeString(this.post_id);
        dest.writeString(this.post_thumb);
        dest.writeString(this.ratio);
        dest.writeTypedList(this.sticker_info);
        dest.writeTypedList(this.text_infoposter);
        dest.writeString(this.text_info1);
        dest.writeString(this.title);
        dest.writeString(this.tag);
        dest.writeString(this.user_name);
        dest.writeInt(this.created);
        dest.writeInt(this.template_type);
        dest.writeString(this.template_w_h_ratio);
        dest.writeInt(this.template_total);
        dest.writeInt(this.views);
    }

    public void readFromParcel(Parcel source) {
        this.back_image = source.readString();
        this.cat_id = source.readString();
        this.post_id = source.readString();
        this.post_thumb = source.readString();
        this.ratio = source.readString();
        this.sticker_info = source.createTypedArrayList(Sticker_info.CREATOR);
        this.text_infoposter = source.createTypedArrayList(Text_infoposter.CREATOR);
        this.text_info1 = source.readString();
        this.title = source.readString();
        this.tag = source.readString();
        this.user_name = source.readString();
        this.created = source.readInt();
        this.template_type = source.readInt();
        this.template_w_h_ratio = source.readString();
        this.template_total = source.readInt();
        this.views = source.readInt();
    }
}
