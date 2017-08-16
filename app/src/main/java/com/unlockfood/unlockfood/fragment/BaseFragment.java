package com.unlockfood.unlockfood.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    ProgressDialog pDialog;

    public void startProgressdialog(String message) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void stopProgressDialog() {
        pDialog.dismiss();
    }
}
