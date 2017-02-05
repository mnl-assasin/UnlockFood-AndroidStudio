package com.unlockfood.unlockfood.builder;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by mleano on 5/20/2016.
 */
public class ToastBuilder {

    public static void shortToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}
