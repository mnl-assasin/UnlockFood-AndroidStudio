package com.unlockfood.unlockfood.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.api.AdClickRequest;
import com.unlockfood.unlockfood.api.ApiClient;
import com.unlockfood.unlockfood.api.ApiInterface;
import com.unlockfood.unlockfood.data.EZSharedPreferences;
import com.unlockfood.unlockfood.data.RespCode;
import com.unlockfood.unlockfood.receiver.AdminReceiver;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinActivity extends BaseActivity {

    private static final String TAG = PinActivity.class.getSimpleName();

    @Bind(R.id.adView)
    AdView adView;

    @Bind(R.id.tvHeader)
    TextView tvHeader;

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
    TextView tvSettings;
    @Bind(R.id.tvCancel)
    TextView tvCancel;
    @Bind(R.id.tvDelete)
    TextView tvDelete;

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
    @Bind(R.id.container)
    RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        ButterKnife.bind(this);

        initData();
        loadAds();
        initMasterPin();
    }

    private void initData() {

        String background = EZSharedPreferences.getPinBackground(this);
        Picasso.with(this).load(Uri.parse(background)).error(R.drawable.black).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                container.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }

    private void loadAds() {
        new AdAsyncTask().execute();
    }

    private void initMasterPin() {
        String masterPin = EZSharedPreferences.getMasterPin(PinActivity.this);
        Log.d(TAG, masterPin);
        if (masterPin.equals(""))
            startActivity(new Intent(PinActivity.this, PinNominateActivity.class));
    }

    @OnClick({R.id.iv1, R.id.iv2, R.id.iv3, R.id.iv4, R.id.iv5, R.id.iv6, R.id.iv7, R.id.iv8, R.id.iv9, R.id.iv0, R.id.tvSettings, R.id.tvCancel, R.id.tvDelete})
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
            case R.id.tvDelete:
                onDeleteClick();
                break;
        }
    }

    private void onDeleteClick() {

        if (pinCode.length() > 0)
            pinCode = pinCode.substring(0, pinCode.length() - 1);


        if (pinCode.length() == 0) {
            tvCancel.setVisibility(View.VISIBLE);
            tvDelete.setVisibility(View.GONE);
        }

        switch (pinCode.length()) {
            case 0:
                cb1.setChecked(false);
                break;
            case 1:
                cb2.setChecked(false);
                break;
            case 2:
                cb3.setChecked(false);
                break;

        }


    }

    private void addPin(int i) {
        tvHeader.setText("Enter Passcode");

        pinCode += String.valueOf(i);
        tvCancel.setVisibility(View.GONE);
        tvDelete.setVisibility(View.VISIBLE);
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

//        String pin = "1937";
        String masterPin = EZSharedPreferences.getMasterPin(PinActivity.this);

        if (masterPin.equals(pinCode)) {
            addClick();
            finishAffinity();
            postClick();
        } else invalidPin();


    }

    private void addClick() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<Void> call = api.postAdClicks(new AdClickRequest(String.valueOf(EZSharedPreferences.getId(this))));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void postClick() {

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
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
//        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why this needs to be added.");
        startActivityForResult(intent, RespCode.REQ_DEVICE_ADMIN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RespCode.REQ_DEVICE_ADMIN:
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
