package com.astarconcepts.brandbanao.custom.animated_video.model;

import java.util.ArrayList;

/**
 * Created by deepshikha on 3/3/17.
 */

public class ModelImages {
    String str_folder;
    ArrayList<String> alImagepath;

    public ModelImages() {

    }

    public ModelImages(String str_folder, ArrayList<String> al_imagepath) {
        this.str_folder = str_folder;
        this.alImagepath = al_imagepath;
    }

    public String getStr_folder() {
        return str_folder;
    }

    public void setStr_folder(String str_folder) {
        this.str_folder = str_folder;
    }

    public ArrayList<String> getAlImagepath() {
        return alImagepath;
    }

    public void setAlImagepath(ArrayList<String> alImagepath) {
        this.alImagepath = alImagepath;
    }
}
