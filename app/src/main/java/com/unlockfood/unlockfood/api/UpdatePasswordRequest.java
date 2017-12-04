package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mykelneds on 23/08/2017.
 */

public class UpdatePasswordRequest {

    @SerializedName("old_password")
    String oldPassword;

    @SerializedName("new_password")
    String newPassword;

    public UpdatePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
