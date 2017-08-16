package com.unlockfood.unlockfood.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.builder.DialogBuilder;
import com.unlockfood.unlockfood.data.EZSharedPreferences;
import com.unlockfood.unlockfood.data.RespCode;
import com.unlockfood.unlockfood.receiver.AdminReceiver;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends BaseActivity {

    String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.gif)
    GifImageView gif;

    @Bind(R.id.btnGetStarted)
    Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPermissions();
        initMasterPin();
//        loadURL();

        if (EZSharedPreferences.isLogin(this)) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }
    }

    @OnClick(R.id.btnGetStarted)
    public void onClick() {
        if (EZSharedPreferences.isLogin(this))
            startActivity(new Intent(this, DashboardActivity.class));
        else
            startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    public void initPermissions() {

        DevicePolicyManager deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName compName = new ComponentName(this, AdminReceiver.class);

        if (!deviceManger.isAdminActive(compName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
//            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why this needs to be added.");
            startActivityForResult(intent, RespCode.REQ_DEVICE_ADMIN);
        }
    }

    private void initMasterPin() {
        Log.d(TAG, "initMasterPin");
        String masterPin = EZSharedPreferences.getMasterPin(MainActivity.this);
        Log.d(TAG, masterPin);
        if (masterPin.equals("")) {
            startActivity(new Intent(MainActivity.this, PinNominateActivity.class));
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RespCode.REQ_DEVICE_ADMIN:
                if (resultCode == Activity.RESULT_OK) {
                } else {
                    DialogBuilder.dialogBuilder(MainActivity.this, "You need to grant the app Device Admin access", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
