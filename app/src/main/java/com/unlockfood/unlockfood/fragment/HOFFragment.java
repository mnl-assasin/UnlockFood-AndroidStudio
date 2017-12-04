package com.unlockfood.unlockfood.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.unlockfood.unlockfood.HOFAdapter;
import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.api.ApiClient;
import com.unlockfood.unlockfood.api.ApiInterface;
import com.unlockfood.unlockfood.api.HallOfFameData;
import com.unlockfood.unlockfood.api.HallOfFameResponse;
import com.unlockfood.unlockfood.builder.ToastBuilder;
import com.unlockfood.unlockfood.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass
 */
public class HOFFragment extends BaseFragment {


    @Bind(R.id.list)
    ListView list;

    public HOFFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hof, container, false);
        ButterKnife.bind(this, view);

        if (Util.isInternetAvailable(getActivity()))
            initList();
        else
            ToastBuilder.shortToast(getActivity(), "Please connect to internet and try again");
        return view;
    }

    private void initList() {
        startProgressdialog("Processing...");
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<HallOfFameResponse> call = api.getHallOfFame();
        call.enqueue(new Callback<HallOfFameResponse>() {
            @Override
            public void onResponse(Call<HallOfFameResponse> call, Response<HallOfFameResponse> response) {
                stopProgressDialog();
                if (response.isSuccessful())
                    populateList(response.body());
                else {
                    Toast.makeText(getActivity(), response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<HallOfFameResponse> call, Throwable t) {
                stopProgressDialog();
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void populateList(HallOfFameResponse body) {

        List<HallOfFameData> items = body.getData();
        HOFAdapter adapter = new HOFAdapter(getActivity(), items);
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
