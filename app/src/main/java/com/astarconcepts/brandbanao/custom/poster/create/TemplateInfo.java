package com.astarconcepts.brandbanao.custom.poster.create;

import com.astarconcepts.brandbanao.custom.poster.model.ElementInfoPoster;
import com.astarconcepts.brandbanao.custom.poster.views.text.TextInfo;

import java.util.ArrayList;


public class TemplateInfo {
    int OVERLAY_BLUR = 0;
    int OVERLAY_OPACITY = 0;
    ArrayList<ElementInfoPoster> elementInfoArrayList;
    ArrayList<TextInfo> textInfoArrayList;
    private String FRAME_NAME;
    private String OVERLAY_NAME = "";
    private String PROFILE_TYPE;
    private String RATIO;
    private String SEEK_VALUE;
    private String TEMPCOLOR = "";
    private int TEMPLATE_ID;
    private String TEMP_PATH = "";
    private String THUMB_URI;
    private String TYPE;


    public TemplateInfo() {
    }

    public int getTEMPLATE_ID() {
        return this.TEMPLATE_ID;
    }

    public void setTEMPLATE_ID(int i) {
        this.TEMPLATE_ID = i;
    }

    public String getTHUMB_URI() {
        return this.THUMB_URI;
    }

    public void setTHUMB_URI(String str) {
        this.THUMB_URI = str;
    }

    public String getFRAME_NAME() {
        return this.FRAME_NAME;
    }

    public void setFRAME_NAME(String str) {
        this.FRAME_NAME = str;
    }

    public String getRATIO() {
        return this.RATIO;
    }

    public void setRATIO(String str) {
        this.RATIO = str;
    }

    public String getPROFILE_TYPE() {
        return this.PROFILE_TYPE;
    }

    public void setPROFILE_TYPE(String str) {
        this.PROFILE_TYPE = str;
    }

    public String getSEEK_VALUE() {
        return this.SEEK_VALUE;
    }

    public void setSEEK_VALUE(String str) {
        this.SEEK_VALUE = str;
    }

    public String getTYPE() {
        return this.TYPE;
    }

    public void setTYPE(String str) {
        this.TYPE = str;
    }

    public int getOVERLAY_OPACITY() {
        return this.OVERLAY_OPACITY;
    }

    public void setOVERLAY_OPACITY(int i) {
        this.OVERLAY_OPACITY = i;
    }

    public String getTEMPCOLOR() {
        return this.TEMPCOLOR;
    }

    public void setTEMPCOLOR(String str) {
        this.TEMPCOLOR = str;
    }

    public String getOVERLAY_NAME() {
        return this.OVERLAY_NAME;
    }

    public void setOVERLAY_NAME(String str) {
        this.OVERLAY_NAME = str;
    }

    public String getTEMP_PATH() {
        return this.TEMP_PATH;
    }

    public void setTEMP_PATH(String str) {
        this.TEMP_PATH = str;
    }

    public int getOVERLAY_BLUR() {
        return this.OVERLAY_BLUR;
    }

    public void setOVERLAY_BLUR(int i) {
        this.OVERLAY_BLUR = i;
    }

    public ArrayList<TextInfo> getTextInfoArrayList() {
        return this.textInfoArrayList;
    }

    public void setTextInfoArrayList(ArrayList<TextInfo> arrayList) {
        this.textInfoArrayList = arrayList;
    }

    public ArrayList<ElementInfoPoster> getElementInfoArrayList() {
        return this.elementInfoArrayList;
    }

    public void setElementInfoArrayList(ArrayList<ElementInfoPoster> arrayList) {
        this.elementInfoArrayList = arrayList;
    }
}
