package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.SerializedName;

public class PaytmResponse {

    @SerializedName("txnToken")
    public String txnToken;

    @SerializedName("callback_url")
    public String callback_url;

    public PaytmResponse(String txnToken, String callback_url) {
        this.txnToken = txnToken;
        this.callback_url = callback_url;
    }
}
