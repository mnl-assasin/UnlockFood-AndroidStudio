package com.unlockfood.unlockfood.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.api.ApiClient;
import com.unlockfood.unlockfood.api.ApiInterface;
import com.unlockfood.unlockfood.api.RewardsData;
import com.unlockfood.unlockfood.api.RewardsResponse;
import com.unlockfood.unlockfood.data.EZSharedPreferences;
import com.unlockfood.unlockfood.data.RewardsAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Rewards2Fragment extends BaseFragment {

    String TAG = Rewards2Fragment.class.getSimpleName();

    @Bind(R.id.gridView)
    GridView gridView;

    List<String> items;
    RewardsAdapter adapter;

    public Rewards2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rewards2, container, false);
        ButterKnife.bind(this, view);

        initRewards();
        return view;
    }

    private void initRewards() {

        startProgressdialog("loading...");

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<RewardsResponse> call = api.getLevels();
        call.enqueue(new Callback<RewardsResponse>() {
            @Override
            public void onResponse(Call<RewardsResponse> call, Response<RewardsResponse> response) {
                stopProgressDialog();
                if (response.isSuccessful()) {
                    Log.d(TAG, "succesful");
                    populateRewards(response.body());

                } else {
                    try {
                        Log.d(TAG, response.errorBody().string().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    failRewards();
                }
            }

            @Override
            public void onFailure(Call<RewardsResponse> call, Throwable t) {
                stopProgressDialog();
                Log.d(TAG, t.toString());
                failRewards();
            }
        });
    }

    private void populateRewards(RewardsResponse body) {
        List<RewardsData> list = body.getData();
        items = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            items.add(list.get(i).getBadgeUrl());
        }

        adapter = new RewardsAdapter(getActivity(), items, getLevel());
        gridView.setAdapter(adapter);
    }

    public int getLevel() {

        String level = EZSharedPreferences.getLevel(getActivity());
        Log.d(TAG, "LEVEL: " + level);

        if (level.equals("0"))
            return 0;
        if (!level.equals("")) {
            String levels[] = level.split("\\s+");
            return Integer.parseInt(levels[1]);
        }
        return 0;
    }

    private void failRewards() {
        Log.d(TAG, "fail rewards");
        items = new ArrayList<>();
        adapter = new RewardsAdapter(getActivity(), items, getLevel());
        gridView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
