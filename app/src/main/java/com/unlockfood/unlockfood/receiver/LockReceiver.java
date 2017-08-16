package com.unlockfood.unlockfood.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unlockfood.unlockfood.activity.PinActivity;
import com.unlockfood.unlockfood.data.EZSharedPreferences;

public class LockReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        if (EZSharedPreferences.isAgreed(context))
            context.startActivity(new Intent(context, PinActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}