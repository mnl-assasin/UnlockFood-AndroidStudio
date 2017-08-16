package com.unlockfood.unlockfood.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mykelneds on 17/04/2017.
 */

public class BlackActivity extends AppCompatActivity {

    ProgressDialog pDialog;

    public void startProgessDialog(String message){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(message);
        pDialog.show();
    }

    public void stopProgressDialog(){
        pDialog.dismiss();
    }

}
