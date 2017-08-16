package com.unlockfood.unlockfood.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.api.ApiClient;
import com.unlockfood.unlockfood.api.ApiInterface;
import com.unlockfood.unlockfood.api.RegisterEmail;
import com.unlockfood.unlockfood.api.UserDetailsResponse;
import com.unlockfood.unlockfood.data.Const;
import com.unlockfood.unlockfood.data.EZSharedPreferences;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BlackActivity {

    String TAG = SignupActivity.class.getSimpleName();

    CallbackManager callbackManager;
    @Bind(R.id.etFName)
    EditText etFName;
    @Bind(R.id.etLName)
    EditText etLName;
    @Bind(R.id.etUsername)
    EditText etUsername;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.etRetypePassword)
    EditText etRetypePassword;
    @Bind(R.id.btnSubmit)
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        initFBLogin();
    }

    private void initFBLogin() {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btnSubmit)
    public void onClick() {
        String fName = etFName.getText().toString().trim();
        String lName = etLName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String rePassword = etRetypePassword.getText().toString().trim();
        boolean isValid = true;

        if (fName.equals("")) {
            isValid = false;
            etFName.setError("Enter your first name");
        }
        if (lName.equals("")) {
            isValid = false;
            etLName.setError("Enter your last name");
        }
        if (username.equals("")) {
            isValid = false;
            etUsername.setError("Enter your username");
        }
        if (email.equals("")) {
            isValid = false;
            etEmail.setError("Enter your email");
        }
        if (password.equals("")) {
            isValid = false;
            etPassword.setError("Enter your password");
        }

        if (rePassword.equals("")) {
            isValid = false;
            etRetypePassword.setError("Please retype your password");
        }

        if (isValid) {
            if (password.equals(rePassword)) {
                processSignup(username, fName, lName, email, password);
            } else {
                Toast.makeText(this, "PASSWORD MISMATCH", Toast.LENGTH_LONG).show();
                etPassword.setText("");
                etRetypePassword.setText("");
            }
        }
    }

    private void processSignup(String username, String fName, String lName, String email, String password) {
        startProgessDialog("Processing...");
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<UserDetailsResponse> call = api.registerEmail(new RegisterEmail(Const.SOCIAL_MANUAL, username, fName, lName, email, password));
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                stopProgressDialog();
                if (response.isSuccessful()) {
                    saveUserDetails(Const.LOGIN_MANUAL, response.body());
                } else {
                    int code = response.code();

                    if (Const.FORBIDDEN == code) {
                        showLoginDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                stopProgressDialog();
            }
        });
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid Login Credentials");
        builder.setMessage("Did you forgot your password or trying to Login?");
        builder.setNegativeButton("Forgot Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNeutralButton("Log in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                etFName.setText("");
                etLName.setText("");
                etUsername.setText("");
                etEmail.setText("");
                etPassword.setText("");
                etRetypePassword.setText("");
            }
        }).show();
    }

    private void saveUserDetails(int loginType, UserDetailsResponse body) {
        Log.d(TAG, "saveUserDetails");
        stopProgressDialog();
        EZSharedPreferences.setLogin(this, true);
        EZSharedPreferences.setLoginType(this, loginType);
        EZSharedPreferences.setUserDetails(this, body.getData());
        startActivity(new Intent(this, DashboardActivity.class));
    }
}
