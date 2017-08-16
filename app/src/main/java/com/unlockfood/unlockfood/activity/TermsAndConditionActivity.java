package com.unlockfood.unlockfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.data.EZSharedPreferences;
import com.unlockfood.unlockfood.widgets.ButtonMyriad;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermsAndConditionActivity extends AppCompatActivity {

    @Bind(R.id.btnCancel)
    ButtonMyriad btnCancel;
    @Bind(R.id.btnAgree)
    ButtonMyriad btnAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);
        ButterKnife.bind(this);

        checkAgreed();
    }

    private void checkAgreed() {

        if (EZSharedPreferences.isAgreed(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    @OnClick({R.id.btnCancel, R.id.btnAgree})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                EZSharedPreferences.setAgreed(this, false);
                finish();
                break;
            case R.id.btnAgree:
                EZSharedPreferences.setAgreed(this, true);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}
