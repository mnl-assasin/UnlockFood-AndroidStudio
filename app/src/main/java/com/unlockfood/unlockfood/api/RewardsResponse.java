package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RewardsResponse {

    @SerializedName("data")
    @Expose
    private List<RewardsData> data = null;

    public List<RewardsData> getData() {
        return data;
    }

    public void setData(List<RewardsData> data) {
        this.data = data;
    }

}