package com.unlockfood.unlockfood.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.builder.DialogBuilder;
import com.unlockfood.unlockfood.data.EZSharedPreferences;
import com.unlockfood.unlockfood.widgets.TextViewLight;
import com.unlockfood.unlockfood.widgets.TextViewMed;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NominatePinActivity extends AppCompatActivity {

    String TAG = NominatePinActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvHeader)
    TextViewLight tvHeader;
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

    String masterPin = "";
    String oldPin = "";
    String newPin = "";
    String confirmPin = "";


    String tempPin = "";
    String root = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominate_pin);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initData();
    }

    private void initData() {

        root = getIntent().getStringExtra("root");

        if (root == null)
            root = "";

        Log.d(TAG, "Root: " + root);
        masterPin = EZSharedPreferences.getMasterPin(getApplicationContext());
        if (!root.equals("")) {
            Log.d(TAG, root);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            tvHeader.setText("Enter passcode");
        } else {
            Log.d(TAG, "NO ROOT");
            tvHeader.setText("Enter new passcode");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onCancelClick();
        }

        return super.onOptionsItemSelected(item);
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
            case R.id.tvCancel:
                onCancelClick();
                break;
        }
    }

    private void onCancelClick() {
        if (root.equals("change pin")) {
            finish();
        } else {
            DialogBuilder.dialogBuilder(NominatePinActivity.this, "Are you sure you want to exit?", "If you exit now youre passcode will be 0000\nYou can change it later in settings", false,
                    "Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EZSharedPreferences.setMasterPin(NominatePinActivity.this, "0000");
                            finish();
                        }
                    }, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
        }
    }


    private void addPin(int i) {
//        tvHeader.setText("Enter Passcode");

        tempPin += String.valueOf(i);
        Log.d(TAG, "ADD PIN: " + i + " PIN: " + tempPin);
        switch (tempPin.length()) {
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
                processPin(tempPin);
                tempPin = "";
                break;

        }
    }

    private void processPin(String tempPin) {

        if (root.equals("change pin")) {

            if (oldPin.equals("")) {
                oldPin = tempPin;
                tvHeader.setText("Enter new passcode");
            } else if (newPin.equals("")) {
                newPin = tempPin;
                tvHeader.setText("Confirm new passcode");
            } else {
                confirmPin = tempPin;
                processPin(oldPin, newPin, confirmPin);
            }
            clearPin();
        } else {
            if (newPin.equals("")) {
                newPin = tempPin;
                tvHeader.setText("Confirm new passcode");
            } else {
                confirmPin = tempPin;
                processPin(newPin, confirmPin);
            }
            clearPin();
        }
    }

    private void processPin(String newPin, String confirmPin) {
        if (newPin.equals(confirmPin)) {
            EZSharedPreferences.setMasterPin(NominatePinActivity.this, newPin);
            DialogBuilder.dialogBuilder(NominatePinActivity.this, "Pin Creation Succesful", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
        } else {
            DialogBuilder.dialogBuilder(NominatePinActivity.this, "Pin Mismatch", "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    clearPin();
                    resetPin();
                }
            });
        }
    }

    private void processPin(String oldPin, String newPin, String confirmPin) {
        if (oldPin.equals(masterPin)) {
            if (newPin.equals(confirmPin)) {
                EZSharedPreferences.setMasterPin(NominatePinActivity.this, newPin);
                DialogBuilder.dialogBuilder(NominatePinActivity.this, "Change pin sucessful", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
            } else {
                DialogBuilder.dialogBuilder(NominatePinActivity.this, "Pin Mismatch", "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        clearPin();
                        resetPin();
                    }
                });
            }
        } else {
            DialogBuilder.dialogBuilder(NominatePinActivity.this, "Incorrect Pin", "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    clearPin();
                    resetPin();
                }
            });
        }
    }

    private void clearPin() {
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        cb4.setChecked(false);
    }

    private void resetPin() {
        oldPin = "";
        newPin = "";
        confirmPin = "";

        masterPin = EZSharedPreferences.getMasterPin(NominatePinActivity.this);

        if (root.equals("change pin")) {
            tvHeader.setText("Enter passcode");
        } else {
            tvHeader.setText("Enter new passcode");
        }
    }


}
