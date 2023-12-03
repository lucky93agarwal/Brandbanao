package com.astarconcepts.brandbanao.custom.poster.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sticker_info implements Parcelable {

    String st_field1;
    String st_field2 = "";
    String st_field3;
    String st_field4;
    String st_hue;
    String st_scale_prog;
    String st_x_rotateprog;
    String st_y_rotateprog;
    String st_y_rotation;
    String st_z_rotateprog;
    private String st_height;
    private String st_image;
    private String st_order;
    private String st_res_id;
    private String st_res_uri;
    private String st_rotation;
    private String st_width;
    private String st_x_pos;
    private String st_y_pos;
    private String sticker_id;

    int editable;

    public int getEditable() {
        return editable;
    }

    public void setEditable(int editable) {
        this.editable = editable;
    }

    public String STKR_PATH = "", NAME = "";


    public Sticker_info() {
    }


    public String getSTKR_PATH() {
        return STKR_PATH;
    }

    public void setSTKR_PATH(String STKR_PATH) {
        this.STKR_PATH = STKR_PATH;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }


    public String getSt_y_rotation() {
        return this.st_y_rotation;
    }

    public void setSt_y_rotation(String str) {
        this.st_y_rotation = str;
    }

    public String getSt_x_rotateprog() {
        return this.st_x_rotateprog;
    }

    public void setSt_x_rotateprog(String str) {
        this.st_x_rotateprog = str;
    }

    public String getSt_y_rotateprog() {
        return this.st_y_rotateprog;
    }

    public void setSt_y_rotateprog(String str) {
        this.st_y_rotateprog = str;
    }

    public String getSt_z_rotateprog() {
        return this.st_z_rotateprog;
    }

    public void setSt_z_rotateprog(String str) {
        this.st_z_rotateprog = str;
    }

    public String getSt_scale_prog() {
        return this.st_scale_prog;
    }

    public void setSt_scale_prog(String str) {
        this.st_scale_prog = str;
    }

    public String getSt_hue() {
        return this.st_hue;
    }

    public void setSt_hue(String str) {
        this.st_hue = str;
    }

    public String getSt_field1() {
        return this.st_field1;
    }

    public void setSt_field1(String str) {
        this.st_field1 = str;
    }

    public String getSt_field2() {
        return this.st_field2;
    }

    public void setSt_field2(String str) {
        this.st_field2 = str;
    }

    public String getSt_field3() {
        return this.st_field3;
    }

    public void setSt_field3(String str) {
        this.st_field3 = str;
    }

    public String getSt_field4() {
        return this.st_field4;
    }

    public void setSt_field4(String str) {
        this.st_field4 = str;
    }

    public String getSt_order() {
        return this.st_order;
    }

    public void setSt_order(String str) {
        this.st_order = str;
    }

    public String getSt_image() {
        return this.st_image;
    }

    public void setSt_image(String str) {
        this.st_image = str;
    }

    public String getSt_rotation() {
        return this.st_rotation;
    }

    public void setSt_rotation(String str) {
        this.st_rotation = str;
    }

    public String getSt_height() {
        return this.st_height;
    }

    public void setSt_height(String str) {
        this.st_height = str;
    }

    public String getSt_y_pos() {
        return this.st_y_pos;
    }

    public void setSt_y_pos(String str) {
        this.st_y_pos = str;
    }

    public String getSt_x_pos() {
        return this.st_x_pos;
    }

    public void setSt_x_pos(String str) {
        this.st_x_pos = str;
    }

    public String getSt_res_uri() {
        return this.st_res_uri;
    }

    public void setSt_res_uri(String str) {
        this.st_res_uri = str;
    }

    public String getSt_width() {
        return this.st_width;
    }

    public void setSt_width(String str) {
        this.st_width = str;
    }

    public String getSticker_id() {
        return this.sticker_id;
    }

    public void setSticker_id(String str) {
        this.sticker_id = str;
    }

    public String getSt_res_id() {
        return this.st_res_id;
    }

    public void setSt_res_id(String str) {
        this.st_res_id = str;
    }

    public String toString() {
        return "ClassPojo [st_order = " + this.st_order + ", st_image = " + this.st_image + ", st_rotation = " + this.st_rotation + ", st_height = " + this.st_height + ", st_y_pos = " + this.st_y_pos + ", st_x_pos = " + this.st_x_pos + ", st_res_uri = " + this.st_res_uri + ", st_width = " + this.st_width + ", sticker_id = " + this.sticker_id + ", st_res_id = " + this.st_res_id + "]";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.st_field1);
        dest.writeString(this.st_field2);
        dest.writeString(this.st_field3);
        dest.writeString(this.st_field4);
        dest.writeString(this.st_hue);
        dest.writeString(this.st_scale_prog);
        dest.writeString(this.st_x_rotateprog);
        dest.writeString(this.st_y_rotateprog);
        dest.writeString(this.st_y_rotation);
        dest.writeString(this.st_z_rotateprog);
        dest.writeString(this.st_height);
        dest.writeString(this.st_image);
        dest.writeString(this.st_order);
        dest.writeString(this.st_res_id);
        dest.writeString(this.st_res_uri);
        dest.writeString(this.st_rotation);
        dest.writeString(this.st_width);
        dest.writeString(this.st_x_pos);
        dest.writeString(this.st_y_pos);
        dest.writeString(this.sticker_id);
        dest.writeInt(this.editable);
        dest.writeString(this.STKR_PATH);
        dest.writeString(this.NAME);
    }

    public void readFromParcel(Parcel source) {
        this.st_field1 = source.readString();
        this.st_field2 = source.readString();
        this.st_field3 = source.readString();
        this.st_field4 = source.readString();
        this.st_hue = source.readString();
        this.st_scale_prog = source.readString();
        this.st_x_rotateprog = source.readString();
        this.st_y_rotateprog = source.readString();
        this.st_y_rotation = source.readString();
        this.st_z_rotateprog = source.readString();
        this.st_height = source.readString();
        this.st_image = source.readString();
        this.st_order = source.readString();
        this.st_res_id = source.readString();
        this.st_res_uri = source.readString();
        this.st_rotation = source.readString();
        this.st_width = source.readString();
        this.st_x_pos = source.readString();
        this.st_y_pos = source.readString();
        this.sticker_id = source.readString();
        this.editable = source.readInt();
        this.STKR_PATH = source.readString();
        this.NAME = source.readString();
    }

    protected Sticker_info(Parcel in) {
        this.st_field1 = in.readString();
        this.st_field2 = in.readString();
        this.st_field3 = in.readString();
        this.st_field4 = in.readString();
        this.st_hue = in.readString();
        this.st_scale_prog = in.readString();
        this.st_x_rotateprog = in.readString();
        this.st_y_rotateprog = in.readString();
        this.st_y_rotation = in.readString();
        this.st_z_rotateprog = in.readString();
        this.st_height = in.readString();
        this.st_image = in.readString();
        this.st_order = in.readString();
        this.st_res_id = in.readString();
        this.st_res_uri = in.readString();
        this.st_rotation = in.readString();
        this.st_width = in.readString();
        this.st_x_pos = in.readString();
        this.st_y_pos = in.readString();
        this.sticker_id = in.readString();
        this.editable = in.readInt();
        this.STKR_PATH = in.readString();
        this.NAME = in.readString();
    }

    public static final Creator<Sticker_info> CREATOR = new Creator<Sticker_info>() {
        @Override
        public Sticker_info createFromParcel(Parcel source) {
            return new Sticker_info(source);
        }

        @Override
        public Sticker_info[] newArray(int size) {
            return new Sticker_info[size];
        }
    };
}
