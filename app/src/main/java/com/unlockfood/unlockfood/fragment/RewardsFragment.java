package com.unlockfood.unlockfood.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.api.UserDetailsData;
import com.unlockfood.unlockfood.data.EZSharedPreferences;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardsFragment extends Fragment {


    @Bind(R.id.ivLevel1)
    ImageView ivLevel1;
    @Bind(R.id.ivLevel2)
    ImageView ivLevel2;
    @Bind(R.id.ivLevel3)
    ImageView ivLevel3;
    @Bind(R.id.ivLevel4)
    ImageView ivLevel4;
    @Bind(R.id.ivLevel5)
    ImageView ivLevel5;
    @Bind(R.id.ivLevel6)
    ImageView ivLevel6;
    @Bind(R.id.ivLevel7)
    ImageView ivLevel7;
    @Bind(R.id.ivLevel8)
    ImageView ivLevel8;
    @Bind(R.id.ivLevel9)
    ImageView ivLevel9;
    @Bind(R.id.ivLevel10)
    ImageView ivLevel10;

    public RewardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        ButterKnife.bind(this, view);

        initData();
        return view;
    }

    private void initData() {
        UserDetailsData data = EZSharedPreferences.getUserDetails(getActivity());
        switch (getLevel(data.getLevel())) {
            case 10:
                Picasso.with(getActivity()).load(R.drawable.img_level10).into(ivLevel10);
            case 9:
                Picasso.with(getActivity()).load(R.drawable.img_level9).into(ivLevel9);
            case 8:
                Picasso.with(getActivity()).load(R.drawable.img_level8).into(ivLevel8);
            case 7:
                Picasso.with(getActivity()).load(R.drawable.img_level7).into(ivLevel7);
            case 6:
                Picasso.with(getActivity()).load(R.drawable.img_level6).into(ivLevel6);
            case 5:
                Picasso.with(getActivity()).load(R.drawable.img_level5).into(ivLevel5);
            case 4:
                Picasso.with(getActivity()).load(R.drawable.img_level4).into(ivLevel4);
            case 3:
                Picasso.with(getActivity()).load(R.drawable.img_level3).into(ivLevel3);
            case 2:
                Picasso.with(getActivity()).load(R.drawable.img_level2).into(ivLevel2);
            case 1:
                Picasso.with(getActivity()).load(R.drawable.img_level1).into(ivLevel1);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private int getLevel(String level) {
        if (level.equals("0")) {
            return 0;
        }
        if (level.equals("Level 1")) {
            return 1;
        }
        if (level.equals("Level 2")) {
            return 2;
        }
        if (level.equals("Level 3")) {
            return 3;
        }
        if (level.equals("Level 4")) {
            return 4;
        }
        if (level.equals("Level 5")) {
            return 5;
        }
        if (level.equals("Level 6")) {
            return 6;
        }
        if (level.equals("Level 7")) {
            return 7;
        }
        if (level.equals("Level 8")) {
            return 8;
        }
        if (level.equals("Level 9")) {
            return 9;
        }
        if (level.equals("Level 10")) {
            return 10;
        }

        return 10;
    }
}
