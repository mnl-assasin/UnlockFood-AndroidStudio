package com.unlockfood.unlockfood.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unlockfood.unlockfood.activity.PinActivity;

public class LockReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
//        Activity a = new Activity();
//        Intent i = new Intent(a.getBaseContext(), PinActivity.class);
//        a.startActivity(i);
        context.startActivity(new Intent(context, PinActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}