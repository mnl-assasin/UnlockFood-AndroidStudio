package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mykelneds on 16/08/2017.
 */

public class ResetPasswordRequest {

    @SerializedName("email")
    String email;

    public ResetPasswordRequest(String email) {
        this.email = email;
    }
}
