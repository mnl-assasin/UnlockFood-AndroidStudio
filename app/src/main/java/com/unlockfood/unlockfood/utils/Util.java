package com.unlockfood.unlockfood.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.unlockfood.unlockfood.builder.ToastBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by mykelneds on 23/08/2017.
 */

public class Util {

    private static String TAG = Util.class.getSimpleName();

    public static boolean isInternetAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) return false;

        switch (activeNetwork.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                if ((activeNetwork.getState() == NetworkInfo.State.CONNECTED ||
                        activeNetwork.getState() == NetworkInfo.State.CONNECTING))
//                      &&  isInternet())
                    return true;
                break;
            case ConnectivityManager.TYPE_MOBILE:
                if ((activeNetwork.getState() == NetworkInfo.State.CONNECTED ||
                        activeNetwork.getState() == NetworkInfo.State.CONNECTING))
//                      &&  isInternet())
                    return true;
                break;
            default:
                return false;
        }
        return false;
    }

    public static void showResponseError(Context ctx, Response response) {

        String error;

        try {
            error = response.errorBody().string();
            Log.d(TAG, "Raw error: " + error);
            JSONObject jsonObject = new JSONObject(error);
            JSONObject jsonError = jsonObject.getJSONObject("error");

            String message = jsonError.getString("message");
            Log.d(TAG, message);
            ToastBuilder.shortToast(ctx, message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    public static String getErrorMessage(Response response) {
//        Logger.log(TAG, "Response code: " + response.code());
//        return getError(getRawError(response));
//    }
//
//    public static String getError(String json) {
//        Logger.log(TAG, "Raw error: " + json);
//
//        try {
//            JSONObject jsonObject = new JSONObject(json);
//            return jsonObject.getString(KEY_ERROR);
//        } catch (JSONException e) {
//            return json;
//        }
//    }
//
//    public static String getRawError(Response response) {
//        try {
//            String rawError = response.errorBody().string();
//            return rawError;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
