package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.SerializedName;

public class WhatsappOtpResponse {

    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;

    @SerializedName("otp")
    public String otp;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
