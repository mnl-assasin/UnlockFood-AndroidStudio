package com.unlockfood.unlockfood.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.picasso.Picasso;
import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.api.ApiClient;
import com.unlockfood.unlockfood.api.ApiInterface;
import com.unlockfood.unlockfood.api.UserDetailsData;
import com.unlockfood.unlockfood.api.UserDetailsResponse;
import com.unlockfood.unlockfood.builder.ToastBuilder;
import com.unlockfood.unlockfood.data.EZSharedPreferences;
import com.unlockfood.unlockfood.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends BaseFragment {

    String TAG = AccountFragment.class.getSimpleName();


    @Bind(R.id.civProfile)
    CircleImageView civProfile;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvPoints)
    TextView tvPoints;
    @Bind(R.id.tvLevel)
    TextView tvLevel;
    @Bind(R.id.tvPeopleFed)
    TextView tvPeopleFed;
    @Bind(R.id.tvTotalPeopleFed)
    TextView tvTotalPeopleFed;
    @Bind(R.id.ivLevel)
    ImageView ivLevel;
    @Bind(R.id.tvLevelDesc)
    TextView tvLevelDesc;

    int levelDrawables[] = {R.drawable.img_level0, R.drawable.img_level1, R.drawable.img_level2,
            R.drawable.img_level3, R.drawable.img_level4, R.drawable.img_level5, R.drawable.img_level6,
            R.drawable.img_level7, R.drawable.img_level8, R.drawable.img_level9, R.drawable.img_level10};

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);

//        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isInternetAvailable(getActivity()))
            getUserDetails();
        else
            ToastBuilder.shortToast(getActivity(), "Please connect to internet and try again");
    }

    private void getUserDetails() {
        startProgressdialog("loading...");
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<UserDetailsResponse> call = api.getUserDetails(String.valueOf(EZSharedPreferences.getId(getActivity())));
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                stopProgressDialog();
                if (response.isSuccessful())
                    saveDetails(response.body());
                else {
                    Util.showResponseError(getActivity(), response);
                }
            }


            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                stopProgressDialog();
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveDetails(UserDetailsResponse body) {
        EZSharedPreferences.setUserDetails(getActivity(), body.getData());
        initData();
    }

    private void initData() {

        UserDetailsData data = EZSharedPreferences.getUserDetails(getActivity());
        tvName.setText(data.getName());
        tvPoints.setText(data.getPoints() + " Points");

        if (data.getLevel().equals("0"))
            tvLevel.setText("Level 0");
        else
            tvLevel.setText(data.getLevel());

        tvPeopleFed.setText(String.valueOf((int) data.getPeopleFed()));
        tvTotalPeopleFed.setText(String.valueOf((int) data.getTotalPeopleFed()));

        Log.d(TAG, "Picture: " + data.getProfilePictureUrl());
        if (data.getProfilePictureUrl() != null)
            if (!data.getProfilePictureUrl().equals(""))
                Picasso.with(getActivity()).load(data.getProfilePictureUrl()).error(R.drawable.img_profile).into(civProfile);
        String levelBadge = data.getLevelBadge();

        Log.d(TAG, "Level badge:" + levelBadge);
        if (!levelBadge.equals(""))
            Picasso.with(getActivity()).load(levelBadge).error(R.drawable.img_level0).into(ivLevel);
        else
            Picasso.with(getActivity()).load(levelDrawables[getLevel(data.getLevel())]).into(ivLevel);

//        int loginType = EZSharedPreferences.getLoginType(getActivity());
//        if (loginType == Const.LOGIN_FACEBOOK)
//            requestFbProfile();
    }

    private void requestFbProfile() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {

                    String path = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    Log.d(TAG, "Path: " + path);
                    Picasso.with(getActivity()).load(path).into(civProfile);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();

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

        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
