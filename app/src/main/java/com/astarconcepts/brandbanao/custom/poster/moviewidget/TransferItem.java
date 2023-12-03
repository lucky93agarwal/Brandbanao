package com.astarconcepts.brandbanao.custom.poster.moviewidget;

import com.hw.photomovie.PhotoMovieFactory;

public class TransferItem {

    public TransferItem(int imgRes, String name, PhotoMovieFactory.PhotoMovieType type) {
        this.imgRes = imgRes;
        this.name = name;
        this.type = type;
    }

    public int imgRes;
    public String name;
    public PhotoMovieFactory.PhotoMovieType type;
}
