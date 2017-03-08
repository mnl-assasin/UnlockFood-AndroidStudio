package com.unlockfood.unlockfood.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mykelneds on 13/12/2016.
 */

public class EZSharedPreferences {

    private final static String USER_PREFERENCES = "UnlockfoodPrefs";
    private final static String KEY_MASTERPIN = "MASTER PIN";
    private final static String IS_LOGIN = "isLogin";
    private final static String KEY_UNLOCK = "unlock";


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

    public static boolean isLogin(Context ctx) {
        return getSharedPref(ctx).getBoolean(IS_LOGIN, false);
    }

    public static int getUnlockCount(Context ctx) {
        return getSharedPref(ctx).getInt(KEY_UNLOCK, 0);
    }


    /**
     * S E T T E R
     */

    public static void setMasterPin(Context ctx, String masterPin) {

        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putString(KEY_MASTERPIN, masterPin);
        editor.apply();

    }

    public static int setLogin(Context ctx, boolean isLogin) {

        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.apply();

        if (isLogin) {
            return getUnlockCount(ctx);
        } else return 0;
    }

    public static void setUnlockCount(Context ctx) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        boolean isLogin = isLogin(ctx);

        if (!isLogin) {
            editor.putInt(KEY_UNLOCK, getUnlockCount(ctx) + 1);
        }
    }


}
