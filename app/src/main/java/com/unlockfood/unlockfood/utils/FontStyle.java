package com.unlockfood.unlockfood.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by mykelneds on 13/01/2017.
 */

public class FontStyle {

    public static Typeface helveticaLight(Context ctx) {
        return Typeface.createFromAsset(ctx.getAssets(), "fonts/helvetica_light.otf");
    }

    public static Typeface helveticaRoman(Context ctx) {
        return Typeface.createFromAsset(ctx.getAssets(), "fonts/helvetica_roman.otf");
    }

    public static Typeface helveticaBold(Context ctx) {
        return Typeface.createFromAsset(ctx.getAssets(), "fonts/helvetica_bold.otf");
    }

    public static Typeface helveticaMed(Context ctx) {
        return Typeface.createFromAsset(ctx.getAssets(), "fonts/helvetica_med.otf");
    }

    public static Typeface myriad(Context ctx) {
        return Typeface.createFromAsset(ctx.getAssets(), "fonts/myriad.otf");
    }
}
