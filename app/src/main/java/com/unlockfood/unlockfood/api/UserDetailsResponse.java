package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailsResponse {

    @SerializedName("data")
    @Expose
    private UserDetailsData data;

    public UserDetailsData getData() {
        return data;
    }

    public void setData(UserDetailsData data) {
        this.data = data;
    }

}