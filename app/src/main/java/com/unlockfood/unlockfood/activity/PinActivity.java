package com.unlockfood.unlockfood.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.data.EZSharedPreferences;
import com.unlockfood.unlockfood.receiver.AdminReceiver;
import com.unlockfood.unlockfood.widgets.TextViewLight;
import com.unlockfood.unlockfood.widgets.TextViewMed;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.unlockfood.unlockfood.activity.MainActivity.RESULT_ENABLE;

public class PinActivity extends BaseActivity {

    private static final String TAG = PinActivity.class.getSimpleName();

    @Bind(R.id.activity_pin)
    RelativeLayout activityPin;

    @Bind(R.id.adView)
    AdView adView;

    @Bind(R.id.tvHeader)
    TextViewLight tvHeader;

    @Bind(R.id.iv1)
    ImageView iv1;
    @Bind(R.id.iv2)
    ImageView iv2;
    @Bind(R.id.iv3)
    ImageView iv3;
    @Bind(R.id.iv4)
    ImageView iv4;
    @Bind(R.id.iv5)
    ImageView iv5;
    @Bind(R.id.iv6)
    ImageView iv6;
    @Bind(R.id.iv7)
    ImageView iv7;
    @Bind(R.id.iv8)
    ImageView iv8;
    @Bind(R.id.iv9)
    ImageView iv9;
    @Bind(R.id.iv0)
    ImageView iv0;
    @Bind(R.id.tvSettings)
    TextViewMed tvSettings;
    @Bind(R.id.tvCancel)
    TextViewMed tvCancel;

    String pinCode = "";
    @Bind(R.id.cb1)
    CheckBox cb1;
    @Bind(R.id.cb2)
    CheckBox cb2;
    @Bind(R.id.cb3)
    CheckBox cb3;
    @Bind(R.id.cb4)
    CheckBox cb4;

    @Bind(R.id.containerPin)
    LinearLayout containerPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        ButterKnife.bind(this);

        loadAds();
        initMasterPin();
    }

    private void loadAds() {
        new AdAsyncTask().execute();
    }

    private void initMasterPin() {
        String masterPin = EZSharedPreferences.getMasterPin(PinActivity.this);
        Log.d(TAG, masterPin);
        if (masterPin.equals(""))
            startActivity(new Intent(PinActivity.this, NominatePinActivity.class));
    }

    @OnClick({R.id.iv1, R.id.iv2, R.id.iv3, R.id.iv4, R.id.iv5, R.id.iv6, R.id.iv7, R.id.iv8, R.id.iv9, R.id.iv0, R.id.tvSettings, R.id.tvCancel})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv1:
                addPin(1);
                break;
            case R.id.iv2:
                addPin(2);
                break;
            case R.id.iv3:
                addPin(3);
                break;
            case R.id.iv4:
                addPin(4);
                break;
            case R.id.iv5:
                addPin(5);
                break;
            case R.id.iv6:
                addPin(6);
                break;
            case R.id.iv7:
                addPin(7);
                break;
            case R.id.iv8:
                addPin(8);
                break;
            case R.id.iv9:
                addPin(9);
                break;
            case R.id.iv0:
                addPin(0);
                break;
            case R.id.tvSettings:
                onSettingsClick();
                break;
            case R.id.tvCancel:
                onCancelClick();
                break;
        }
    }


    private void addPin(int i) {
        tvHeader.setText("Enter Passcode");

        pinCode += String.valueOf(i);
        switch (pinCode.length()) {
            case 1:
                cb1.setChecked(true);
                break;
            case 2:
                cb2.setChecked(true);
                break;
            case 3:
                cb3.setChecked(true);
                break;
            case 4:
                cb4.setChecked(true);
                processPin(pinCode);
                break;

        }


    }

    private void processPin(String pinCode) {

//        String tempPin = "1937";
        String masterPin = EZSharedPreferences.getMasterPin(PinActivity.this);

        if (masterPin.equals(pinCode)) finishAffinity();
        else invalidPin();


    }

    private void invalidPin() {
        pinCode = "";
        tvHeader.setText("Invalid passcode");

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        containerPin.startAnimation(anim);

        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        cb4.setChecked(false);

    }

    private void onSettingsClick() {
        startActivity(new Intent(PinActivity.this, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    ComponentName compName;

    private void onCancelClick() {


        deviceManger = (DevicePolicyManager) getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(
                Context.ACTIVITY_SERVICE);
        compName = new ComponentName(this, AdminReceiver.class);

        boolean active = deviceManger.isAdminActive(compName);
        if (active) {
            deviceManger.lockNow();
        } else {
            enable();
        }
    }

    private void enable() {
        Intent intent = new Intent(DevicePolicyManager
                .ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                compName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Additional text explaining why this needs to be added.");
        startActivityForResult(intent, RESULT_ENABLE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("DeviceAdminSample", "Admin enabled!");
                } else {
                    Toast.makeText(getApplicationContext(), "Please agree to cancel", Toast.LENGTH_LONG).show();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        onCancelClick();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                Log.d(TAG, "KEY CODE");
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class AdAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AdRequest adRequest = new AdRequest.Builder().build();
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
//                    super.onAdOpened();
                    Log.d(TAG, "Don't open the ad");
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }
            });

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        cb4.setChecked(false);

        pinCode = "";

    }

    @Override
    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
//        onCancelClick();
//        return;
    }
}
