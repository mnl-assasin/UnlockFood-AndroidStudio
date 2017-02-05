package com.unlockfood.unlockfood.builder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.unlockfood.unlockfood.R;


/**
 * Created by mleano on 5/20/2016.
 */
public class DialogBuilder {

    public static void dialogBuilder(final Context ctx, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(ctx.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void dialogBuilder(final Context ctx, String message, String pButton,
                                     DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(message);
        builder.setPositiveButton(pButton, positiveListener);
        builder.show();
    }

    public static void dialogBuilder(final Context ctx, String message, String pButton,
                                     DialogInterface.OnClickListener positiveListener, String nButton,
                                     DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(message);
        builder.setPositiveButton(pButton, positiveListener);
        builder.setNegativeButton(nButton, negativeListener);
        builder.show();
    }

    public static void dialogBuilder(final Context ctx, String title, String message, boolean cancelable, String pButton,
                                     DialogInterface.OnClickListener positiveListener, String nButton,
                                     DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        builder.setPositiveButton(pButton, positiveListener);
        builder.setNegativeButton(nButton, negativeListener);
        builder.show();
    }

    public static void dialogBuilder(final Context ctx, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(message);
        builder.setPositiveButton(ctx.getString(R.string.done), listener);
        builder.show();

    }

}
