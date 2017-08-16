package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mykelneds on 16/04/2017.
 */

public class RegisterEmail {

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

    @SerializedName("password")
    String password;

    public RegisterEmail(String socialType, String username, String firstName, String lastName, String email, String password) {
        this.socialType = socialType;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
