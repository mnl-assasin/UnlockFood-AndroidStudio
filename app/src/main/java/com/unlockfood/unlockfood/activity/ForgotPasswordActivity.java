package com.unlockfood.unlockfood.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.api.ApiClient;
import com.unlockfood.unlockfood.api.ApiInterface;
import com.unlockfood.unlockfood.api.ResetPasswordRequest;
import com.unlockfood.unlockfood.builder.ToastBuilder;
import com.unlockfood.unlockfood.widgets.ButtonMyriad;
import com.unlockfood.unlockfood.widgets.TextViewMyriad;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {

    @Bind(R.id.textView)
    TextViewMyriad textView;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.btnSubmit)
    ButtonMyriad btnSubmit;
    @Bind(R.id.activity_forgot_password)
    LinearLayout activityForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSubmit)
    public void onClick() {

        String email = etEmail.getText().toString().trim();

        if (email.equals(""))
            ToastBuilder.shortToast(this, "Please enter your email");
        else {

            startProgressDialog("Resetting your password...");
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            Call<Void> call = api.postResetPassword(new ResetPasswordRequest(email));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    stopProgressDialog();
                    if(response.isSuccessful()){
                        ToastBuilder.shortToast(getApplicationContext(), "We have e-mailed your password reset link!");
                        finish();
                    }else{
                        ToastBuilder.shortToast(getApplicationContext(), "Email not found, please try again");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    stopProgressDialog();
                    ToastBuilder.shortToast(getApplicationContext(), "Something went wrong, please try again.");
                }
            });
        }
    }
}
