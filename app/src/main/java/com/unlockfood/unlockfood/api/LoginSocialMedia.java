package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mykelneds on 16/04/2017.
 */

public class LoginSocialMedia {

    @SerializedName("social_type")
    String socialType;

    @SerializedName("username")
    String username;

    public LoginSocialMedia(String socialType, String username) {
        this.socialType = socialType;
        this.username = username;
    }
}
