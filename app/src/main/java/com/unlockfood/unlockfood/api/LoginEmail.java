package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mykelneds on 16/04/2017.
 */

public class LoginEmail {

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    public LoginEmail(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
