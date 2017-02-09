package com.unlockfood.unlockfood.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.receiver.AdminReceiver;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    String TAG = "SettingsActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.layoutChangePin)
    LinearLayout layoutChangePin;

    @Bind(R.id.swDeviceAdmin)
    Switch swDeviceAdmin;

    DevicePolicyManager deviceManger;
    ComponentName compName;


    int RESULT_ENABLE = 100;
    String destination = "";
    @Bind(R.id.layoutHelp)
    LinearLayout layoutHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initDeviceAdmin();
    }

    private void initDeviceAdmin() {

        deviceManger = (DevicePolicyManager) getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        compName = new ComponentName(this, AdminReceiver.class);

        if (deviceManger.isAdminActive(compName))
            swDeviceAdmin.setChecked(true);
        else
            swDeviceAdmin.setChecked(false);


        swDeviceAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d(TAG, "Switch ON");
                    if (!deviceManger.isAdminActive(compName)) {
                        Intent intent = new Intent(DevicePolicyManager
                                .ACTION_ADD_DEVICE_ADMIN);
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                compName);
                        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                "Additional text explaining why this needs to be added.");
                        startActivityForResult(intent, RESULT_ENABLE);
                    }
                } else {
                    Log.d(TAG, "Switch OFF");
                    deviceManger.removeActiveAdmin(compName);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (destination.equals(""))
            finish();

        destination = "";
    }


    @OnClick({R.id.layoutHelp, R.id.layoutChangePin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutHelp:
                destination = "help";
                startActivity(new Intent(SettingsActivity.this, HelpActivity.class).putExtra("root", "change pin"));

                break;
            case R.id.layoutChangePin:
                destination = "change pin";
                startActivity(new Intent(SettingsActivity.this, NominatePinActivity.class).putExtra("root", "change pin"));
                break;
        }
    }
}
