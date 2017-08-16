package com.unlockfood.unlockfood.data;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.unlockfood.unlockfood.api.UserDetailsData;

/**
 * Created by mykelneds on 13/12/2016.
 */

public class EZSharedPreferences {

    private final static String USER_PREFERENCES = "UnlockfoodPrefs";
    private final static String KEY_MASTERPIN = "MASTER PIN";
    private final static String IS_AGREED_TO_TERMS = "AgreeToTerms";
    private final static String IS_LOGIN = "isLogin";
    private final static String KEY_UNLOCK = "unlock";

    private final static String KEY_ID = "id";
    private final static String KEY_NAME = "name";
    private final static String KEY_FNAME = "firstName";
    private final static String KEY_LNAME = "lastName";
    private final static String KEY_USERNAME = "username";
    private final static String KEY_SOCIAL = "socialType";
    private final static String KEY_PROFILE_PICTURE = "profilePicture";
    private final static String KEY_POINTS = "points";
    private final static String KEY_LEVEL = "level";
    private final static String KEY_PEOPLE_FED = "peopleFed";
    private final static String KEY_TOTAL_PEOPLE_FED = "totalPeopleFed";

    private final static String KEY_LOGIN_TYPE = "loginType";
    private final static String KEY_PIN_BACKGROUND = "pinBackground";


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

    public static boolean isAgreed(Context ctx) {
        return getSharedPref(ctx).getBoolean(IS_AGREED_TO_TERMS, false);
    }

    public static boolean isLogin(Context ctx) {
        return getSharedPref(ctx).getBoolean(IS_LOGIN, false);
    }

    public static int getUnlockCount(Context ctx) {
        return getSharedPref(ctx).getInt(KEY_UNLOCK, 0);
    }

    public static int getLoginType(Context ctx) {
        return getSharedPref(ctx).getInt(KEY_LOGIN_TYPE, 0);
    }

    public static UserDetailsData getUserDetails(Context ctx) {

        UserDetailsData data;

        int id = getSharedPref(ctx).getInt(KEY_ID, 0);
        String name = getSharedPref(ctx).getString(KEY_NAME, null);
        String fName = getSharedPref(ctx).getString(KEY_FNAME, null);
        String lName = getSharedPref(ctx).getString(KEY_LNAME, null);
        String username = getSharedPref(ctx).getString(KEY_USERNAME, null);
        String social = getSharedPref(ctx).getString(KEY_SOCIAL, null);
        String profilePicture = getSharedPref(ctx).getString(KEY_PROFILE_PICTURE, null);
        int points = getSharedPref(ctx).getInt(KEY_POINTS, 0);
        String level = getSharedPref(ctx).getString(KEY_LEVEL, null);
        int peopleFed = getSharedPref(ctx).getInt(KEY_PEOPLE_FED, 0);
        int totalPeopleFed = getSharedPref(ctx).getInt(KEY_TOTAL_PEOPLE_FED, 0);

        data = new UserDetailsData(id, name, fName, lName, username, social, profilePicture, points, level, (double) peopleFed, (double) totalPeopleFed);

        return data;
    }

    public static int getId(Context ctx) {
        return getSharedPref(ctx).getInt(KEY_ID, -1);
    }

    public static String getPinBackground(Context ctx) {
        return getSharedPref(ctx).getString(KEY_PIN_BACKGROUND, "");
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setManifestReadStorage(Context ctx, boolean status) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putBoolean(Manifest.permission.READ_EXTERNAL_STORAGE, status);
        editor.apply();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setManifestWriteStorage(Context ctx, boolean status) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, status);
        editor.apply();
    }

    /**
     * S E T T E R
     */

    public static void setMasterPin(Context ctx, String masterPin) {

        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putString(KEY_MASTERPIN, masterPin);
        editor.apply();

    }

    public static void setAgreed(Context ctx, boolean isAgreed) {

        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putBoolean(IS_AGREED_TO_TERMS, isAgreed);
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

    public static void setUserDetails(Context ctx, UserDetailsData data) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putInt(KEY_ID, data.getId());
        editor.putString(KEY_NAME, data.getName());
        editor.putString(KEY_FNAME, data.getFirstName());
        editor.putString(KEY_LNAME, data.getLastName());
        editor.putString(KEY_USERNAME, data.getUsername());
        editor.putString(KEY_SOCIAL, data.getSocialType());
        editor.putString(KEY_PROFILE_PICTURE, data.getProfilePictureUrl());
        editor.putInt(KEY_POINTS, data.getPoints());
        editor.putString(KEY_LEVEL, data.getLevel());
        editor.putInt(KEY_PEOPLE_FED, (int) data.getPeopleFed());
        editor.putInt(KEY_TOTAL_PEOPLE_FED, (int) data.getTotalPeopleFed());
        editor.apply();
    }

    public static void setLoginType(Context ctx, int loginType) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putInt(KEY_LOGIN_TYPE, loginType);
        editor.apply();
    }

    public static void setPicture(Context ctx, String path) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putString("path", path);
        editor.apply();
    }

    public static void setPINBackground(Context ctx, String path) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putString(KEY_PIN_BACKGROUND, path);
        editor.apply();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean getManifestReadStorage(Context ctx) {
        return getSharedPref(ctx).getBoolean(Manifest.permission.READ_EXTERNAL_STORAGE, false);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean getManifestWriteStorage(Context ctx) {
        return getSharedPref(ctx).getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false);
    }


}
