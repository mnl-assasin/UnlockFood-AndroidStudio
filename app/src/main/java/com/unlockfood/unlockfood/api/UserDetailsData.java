package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailsData {


    @SerializedName("id")
    @Expose
    private Integer id;
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
    @SerializedName("social_type")
    @Expose
    private String socialType;
    @SerializedName("profile_picture_url")
    @Expose
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

    public Integer getId() {
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

    public String getSocialType() {
        return socialType;
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

    public UserDetailsData(Integer id, String name, String firstName, String lastName, String username, String socialType, String profilePictureUrl, int points, String level, double peopleFed, double totalPeopleFed) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.socialType = socialType;
        this.profilePictureUrl = profilePictureUrl;
        this.points = points;
        this.level = level;
        this.peopleFed = peopleFed;
        this.totalPeopleFed = totalPeopleFed;
    }
}