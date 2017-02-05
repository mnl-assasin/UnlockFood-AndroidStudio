package com.unlockfood.unlockfood.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mykelneds on 13/12/2016.
 */

public class EZSharedPreferences {

    private final static String USER_PREFERENCES = "MarikinaEIS";
    private final static String KEY_MASTERPIN = "MASTER PIN";


    public static SharedPreferences getSharedPref(Context ctx) {
        return ctx.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void dropSharedPref(Context ctx) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.clear();
        editor.apply();
    }

    /**
     * G E T T E R
     */

    public static String getMasterPin(Context ctx) {
        return getSharedPref(ctx).getString(KEY_MASTERPIN, "");
    }


    /**
     * S E T T E R
     */

    public static void setMasterPin(Context ctx, String masterPin) {

        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putString(KEY_MASTERPIN, masterPin);
        editor.apply();

    }


}
