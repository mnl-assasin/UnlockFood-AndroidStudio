package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HallOfFameResponse {

    @SerializedName("data")
    @Expose
    private List<HallOfFameData> data = null;

    public List<HallOfFameData> getData() {
        return data;
    }

    public void setData(List<HallOfFameData> data) {
        this.data = data;
    }

}