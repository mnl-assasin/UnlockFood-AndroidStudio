package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HallOfFameData {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("profile_picture_url")
    private String profilePictureUrl;
    @SerializedName("points")
    @Expose
    private int points;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("people_fed")
    @Expose
    private double peopleFed;
    @SerializedName("total_people_fed")
    @Expose
    private double totalPeopleFed;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public int getPoints() {
        return points;
    }

    public String getLevel() {
        return level;
    }

    public double getPeopleFed() {
        return peopleFed;
    }

    public double getTotalPeopleFed() {
        return totalPeopleFed;
    }
}