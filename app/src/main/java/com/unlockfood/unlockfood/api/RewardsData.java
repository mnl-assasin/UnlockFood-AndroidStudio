package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardsData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("point_requirement")
    @Expose
    private String pointRequirement;
    @SerializedName("badge_url")
    @Expose
    private String badgeUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPointRequirement() {
        return pointRequirement;
    }

    public void setPointRequirement(String pointRequirement) {
        this.pointRequirement = pointRequirement;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }

}