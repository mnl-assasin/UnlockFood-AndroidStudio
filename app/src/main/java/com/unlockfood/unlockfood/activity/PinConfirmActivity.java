package com.unlockfood.unlockfood.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.data.EZSharedPreferences;
import com.unlockfood.unlockfood.widgets.TextViewMed;
import com.unlockfood.unlockfood.widgets.TextViewMyriad;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PinConfirmActivity extends AppCompatActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvHeader)
    TextViewMyriad tvHeader;
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
    @Bind(R.id.tvDelete)
    TextViewMed tvDelete;
    @Bind(R.id.container)
    RelativeLayout container;

    String tempPin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominate_pin);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {

        tvHeader.setText("Confirm PIN");

    }

    @OnClick({R.id.iv1, R.id.iv2, R.id.iv3, R.id.iv4, R.id.iv5, R.id.iv6, R.id.iv7,
            R.id.iv8, R.id.iv9, R.id.iv0, R.id.tvSettings, R.id.tvCancel, R.id.tvDelete})
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
            case R.id.tvDelete:
                onDeleteClick();
                break;
        }
    }

    private void onCancelClick() {
        finish();
    }

    private void onDeleteClick() {

        if (tempPin.length() > 0)
            tempPin = tempPin.substring(0, tempPin.length() - 1);


        if (tempPin.length() == 0) {
            tvCancel.setVisibility(View.VISIBLE);
            tvDelete.setVisibility(View.GONE);
        }

        switch (tempPin.length()) {
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
        tempPin += String.valueOf(i);

        tvCancel.setVisibility(View.GONE);
        tvDelete.setVisibility(View.VISIBLE);

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
        String pin = getIntent().getStringExtra("pin");
        if (pin.equals(tempPin)) {
            setResult(RESULT_OK);
            EZSharedPreferences.setMasterPin(this, pin);
        }
        else
            setResult(10000);
        finish();
    }
}
