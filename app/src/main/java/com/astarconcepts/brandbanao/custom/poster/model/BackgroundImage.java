package com.astarconcepts.brandbanao.custom.poster.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BackgroundImage implements Parcelable {
    public static final Creator<BackgroundImage> CREATOR = new Creator<BackgroundImage>() {
        public BackgroundImage createFromParcel(Parcel parcel) {
            return new BackgroundImage(parcel);
        }

        public BackgroundImage[] newArray(int i) {
            return new BackgroundImage[i];
        }
    };

    @SerializedName("category_name")
    String category_name;
    @SerializedName("id")
    int id;
    @SerializedName("image_url")
    String image_url;
    @SerializedName("thumb_url")
    String thumb_url;

    public BackgroundImage(int i, String str, String str2, String str3) {
        this.id = i;
        this.thumb_url = str;
        this.image_url = str2;
        this.category_name = str3;
    }

    protected BackgroundImage(Parcel parcel) {
        this.id = parcel.readInt();
        this.category_name = parcel.readString();
        this.thumb_url = parcel.readString();
        this.image_url = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getCategory_name() {
        return this.category_name;
    }

    public void setCategory_name(String str) {
        this.category_name = str;
    }

    public String getThumb_url() {
        return this.thumb_url;
    }

    public void setThumb_url(String str) {
        this.thumb_url = str;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public void setImage_url(String str) {
        this.image_url = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.category_name);
        parcel.writeString(this.thumb_url);
        parcel.writeString(this.image_url);
    }
}
