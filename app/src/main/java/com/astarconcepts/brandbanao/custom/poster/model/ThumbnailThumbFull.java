package com.astarconcepts.brandbanao.custom.poster.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class ThumbnailThumbFull implements Parcelable, Comparable, Cloneable {

    public static final Creator<ThumbnailThumbFull> CREATOR = new Creator<ThumbnailThumbFull>() {
        @Override
        public ThumbnailThumbFull createFromParcel(Parcel source) {
            return new ThumbnailThumbFull(source);
        }

        @Override
        public ThumbnailThumbFull[] newArray(int size) {
            return new ThumbnailThumbFull[size];
        }
    };
    @SerializedName("post_id")
    int post_id;
    @SerializedName("post_thumb")
    String post_thumb;
    @SerializedName("created")
    int created;
    @SerializedName("premium")
    boolean premium;
    @SerializedName("data")
    private ArrayList<ThumbnailThumbFull> data;
    @SerializedName("template_w_h_ratio")
    private String template_w_h_ratio;
    @SerializedName("text_json")
    private String text_json;


    public ThumbnailThumbFull() {
    }

    protected ThumbnailThumbFull(Parcel in) {
        this.post_id = in.readInt();
        this.post_thumb = in.readString();
        this.created = in.readInt();
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

    public ArrayList<ThumbnailThumbFull> getData() {
        return data;
    }

    public void setData(ArrayList<ThumbnailThumbFull> data) {
        this.data = data;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPost_thumb() {
        return post_thumb;
    }

    public void setPost_thumb(String post_thumb) {
        this.post_thumb = post_thumb;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.post_id);
        dest.writeString(this.post_thumb);
        dest.writeInt(this.created);
    }

    public void readFromParcel(Parcel source) {
        this.post_id = source.readInt();
        this.post_thumb = source.readString();
        this.created = source.readInt();
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
