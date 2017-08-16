package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mykelneds on 16/08/2017.
 */

public class ProfileUpdateRequest {

    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    public ProfileUpdateRequest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
