package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.SerializedName;

public class ModelCreated {


    @SerializedName("created")
    int created;

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }
}
