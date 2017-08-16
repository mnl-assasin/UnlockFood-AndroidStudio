package com.unlockfood.unlockfood.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.fragment.AccountFragment;
import com.unlockfood.unlockfood.fragment.HOFFragment;
import com.unlockfood.unlockfood.fragment.RewardsFragment;
import com.unlockfood.unlockfood.fragment.SettingsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends AppCompatActivity {

    @Bind(R.id.container)
    FrameLayout container;
    @Bind(R.id.ivAccount)
    ImageView ivAccount;
    @Bind(R.id.ivRewards)
    ImageView ivRewards;
    @Bind(R.id.ivHOF)
    ImageView ivHOF;
    @Bind(R.id.ivSettings)
    ImageView ivSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        initView(0);
    }

    private void initView(int i) {

        Fragment fragment = null;
        switch (i) {
            case 0:
                fragment = new AccountFragment();

                ivAccount.setBackgroundColor(getResources().getColor(R.color.menuAccount));
                ivRewards.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                ivHOF.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                ivSettings.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                break;
            case 1:
                fragment = new RewardsFragment();

                ivAccount.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                ivRewards.setBackgroundColor(getResources().getColor(R.color.menuReward));
                ivHOF.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                ivSettings.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                break;
            case 2:
                fragment = new HOFFragment();

                ivAccount.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                ivRewards.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                ivHOF.setBackgroundColor(getResources().getColor(R.color.menuHOF));
                ivSettings.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                break;
            case 3:
                fragment = new SettingsFragment();

                ivAccount.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                ivRewards.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                ivHOF.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                ivSettings.setBackgroundColor(getResources().getColor(R.color.menuSettings));

                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.commit();
        }


    }

    @OnClick({R.id.ivAccount, R.id.ivRewards, R.id.ivHOF, R.id.ivSettings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivAccount:
                initView(0);
                break;
            case R.id.ivRewards:
                initView(1);
                break;
            case R.id.ivHOF:
                initView(2);
                break;
            case R.id.ivSettings:
                initView(3);
                break;
        }
    }
}
