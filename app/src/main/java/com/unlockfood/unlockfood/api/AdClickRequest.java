package com.unlockfood.unlockfood.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mykelneds on 03/08/2017.
 */

public class AdClickRequest {

    @SerializedName("user_id")
    String userId;

    public AdClickRequest(String userId) {
        this.userId = userId;
    }
}
