package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mykelneds on 16/04/2017.
 */

public class RegisterSocialMedia {

    @SerializedName("social_type")
    String socialType;

    @SerializedName("username")
    String username;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("email")
    String email;

    @SerializedName("profile_picture_url")
    String profilePictureUrl;

    public RegisterSocialMedia(String socialType, String username, String firstName, String lastName, String email, String profilePictureUrl) {
        this.socialType = socialType;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
    }
}
