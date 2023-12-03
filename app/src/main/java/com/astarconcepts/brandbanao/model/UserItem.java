package com.astarconcepts.brandbanao.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserItem implements Serializable {

    @NonNull
    @SerializedName("user_id")
    public String userId;

    @SerializedName("name")
    public String userName;

    @SerializedName("image")
    public String userImage;

    @SerializedName("email")
    public String email;

    @SerializedName("status")
    public String status;

    @SerializedName("login_type")
    public String login_type ;

    @SerializedName("mobile_no")
    public String phone;

    @SerializedName("designation")
    public String designation;
    @SerializedName("plan_id")
    public String planId;
    @SerializedName("plan_name")
    public String planName;

    @SerializedName("plan_duration")
    public String planDuration;

    @SerializedName("plan_start_date")
    public String planStartDate;

    @SerializedName("plan_end_date")
    public String planEndDate;

    @SerializedName("is_subscribe")
    public boolean isSubscribed;

    @SerializedName("business_limit")
    public int businessLimit;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getPhone() {
        return phone;
    }

    public String getDesignation() {
        return designation;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanDuration() {
        return planDuration;
    }

    public void setPlanDuration(String planDuration) {
        this.planDuration = planDuration;
    }

    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    public String getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public int getBusinessLimit() {
        return businessLimit;
    }

    public void setBusinessLimit(int businessLimit) {
        this.businessLimit = businessLimit;
    }
}
