package com.astarconcepts.brandbanao.custom.poster.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ServerData {

    @SerializedName("Fonts")
    @Expose
    private List<String> fonts = new ArrayList();

    @SerializedName("Sticker")
    @Expose
    private Sticker sticker = new Sticker();

    public List<String> getFonts() {
        return this.fonts;
    }

    public void setFonts(List<String> list) {
        this.fonts = list;
    }

    public Sticker getSticker() {
        return this.sticker;
    }

    public void setSticker(Sticker sticker2) {
        this.sticker = sticker2;
    }
}
