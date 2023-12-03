package com.astarconcepts.brandbanao.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppInfo {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("onesignal_app_id")
    @Expose
    public String onesignalAppId;
    @SerializedName("app_name")
    @Expose
    public String appName;
    @SerializedName("email")

    public String offerImage;
    @SerializedName("offerImage")

    @Expose
    public String email;
    @SerializedName("app_version")
    @Expose
    public String appVersion;
    @SerializedName("app_timezone")
    @Expose
    public String appTimezone;
    @SerializedName("whatsapp_contact_enable")
    @Expose
    public String whatsappContactEnable;
    @SerializedName("whatsapp_number")
    @Expose
    public String whatsappNumber;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("privacy_policy")
    @Expose
    public String privacyPolicy;
    @SerializedName("refund_policy")
    @Expose
    public String refundPolicy;
    @SerializedName("terms_condition")
    @Expose
    public String termsCondition;
    @SerializedName("razorpay_key_id")
    @Expose
    public String razorpayKeyId;
    @SerializedName("razorpay_key_secret")
    @Expose
    public String razorpayKeySecret;
    @SerializedName("razorpay_enable")
    @Expose
    public String razorpayEnable;
    @SerializedName("stripe_enable")
    @Expose
    public String stripeEnable;
    @SerializedName("stripe_publishable_Key")
    @Expose
    public String stripePublishableKey;
    @SerializedName("stripe_secret_key")
    @Expose
    public String stripeSecretKey;
    @SerializedName("paytm_merchant_key")
    @Expose
    public String paytmMerchantKey;
    @SerializedName("paytm_merchant_id")
    @Expose
    public String paytmMerchantId;
    @SerializedName("paytm_type")
    @Expose
    public String paytmType;
    @SerializedName("paytm_enable")
    @Expose
    public String paytm_enable;

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }

    public Integer getId() {
        return id;
    }

    public String getPaytm_enable() {
        return paytm_enable;
    }

    public void setPaytm_enable(String paytm_enable) {
        this.paytm_enable = paytm_enable;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOnesignalAppId() {
        return onesignalAppId;
    }

    public void setOnesignalAppId(String onesignalAppId) {
        this.onesignalAppId = onesignalAppId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppTimezone() {
        return appTimezone;
    }

    public void setAppTimezone(String appTimezone) {
        this.appTimezone = appTimezone;
    }

    public String getWhatsappContactEnable() {
        return whatsappContactEnable;
    }

    public void setWhatsappContactEnable(String whatsappContactEnable) {
        this.whatsappContactEnable = whatsappContactEnable;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public String getRefundPolicy() {
        return refundPolicy;
    }

    public void setRefundPolicy(String refundPolicy) {
        this.refundPolicy = refundPolicy;
    }

    public String getTermsCondition() {
        return termsCondition;
    }

    public void setTermsCondition(String termsCondition) {
        this.termsCondition = termsCondition;
    }

    public String getRazorpayKeyId() {
        return razorpayKeyId;
    }

    public void setRazorpayKeyId(String razorpayKeyId) {
        this.razorpayKeyId = razorpayKeyId;
    }

    public String getRazorpayKeySecret() {
        return razorpayKeySecret;
    }

    public void setRazorpayKeySecret(String razorpayKeySecret) {
        this.razorpayKeySecret = razorpayKeySecret;
    }

    public String getRazorpayEnable() {
        return razorpayEnable;
    }

    public void setRazorpayEnable(String razorpayEnable) {
        this.razorpayEnable = razorpayEnable;
    }

    public String getStripeEnable() {
        return stripeEnable;
    }

    public void setStripeEnable(String stripeEnable) {
        this.stripeEnable = stripeEnable;
    }

    public String getStripePublishableKey() {
        return stripePublishableKey;
    }

    public void setStripePublishableKey(String stripePublishableKey) {
        this.stripePublishableKey = stripePublishableKey;
    }

    public String getStripeSecretKey() {
        return stripeSecretKey;
    }

    public void setStripeSecretKey(String stripeSecretKey) {
        this.stripeSecretKey = stripeSecretKey;
    }

    public String getPaytmMerchantKey() {
        return paytmMerchantKey;
    }

    public void setPaytmMerchantKey(String paytmMerchantKey) {
        this.paytmMerchantKey = paytmMerchantKey;
    }

    public String getPaytmMerchantId() {
        return paytmMerchantId;
    }

    public void setPaytmMerchantId(String paytmMerchantId) {
        this.paytmMerchantId = paytmMerchantId;
    }

    public String getPaytmType() {
        return paytmType;
    }

    public void setPaytmType(String paytmType) {
        this.paytmType = paytmType;
    }

} 