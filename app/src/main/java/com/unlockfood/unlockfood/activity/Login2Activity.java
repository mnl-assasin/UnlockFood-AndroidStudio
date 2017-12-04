package com.unlockfood.unlockfood.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.api.ApiClient;
import com.unlockfood.unlockfood.api.ApiInterface;
import com.unlockfood.unlockfood.api.LoginEmail;
import com.unlockfood.unlockfood.api.LoginSocialMedia;
import com.unlockfood.unlockfood.api.RegisterSocialMedia;
import com.unlockfood.unlockfood.api.UserDetailsResponse;
import com.unlockfood.unlockfood.data.Const;
import com.unlockfood.unlockfood.data.EZSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login2Activity extends BlackActivity implements GoogleApiClient.OnConnectionFailedListener {

    String TAG = Login2Activity.class.getSimpleName();

    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.btnSigin)
    Button btnSigin;
    @Bind(R.id.tvForgotPw)
    TextView tvForgotPw;
    @Bind(R.id.fbLogin)
    LinearLayout containerFB;
    @Bind(R.id.fbLogout)
    LinearLayout containerLogout;
    @Bind(R.id.btnGoogleSignIn)
    SignInButton btnGoogleSignIn;
    @Bind(R.id.googleLogout)
    LinearLayout googleLogout;
    @Bind(R.id.btnSignup)
    Button btnSignup;

    CallbackManager callbackManager;

    int RC_SIGN_IN = 007;
    GoogleApiClient mGoogleApiClient;
    ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initData();
        initFBLogin();
    }

    private void initData() {

        api = ApiClient.getClient().create(ApiInterface.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    private void initFBLogin() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess");
                String userId = loginResult.getAccessToken().getUserId();
                loginUsingFacebook(userId);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Cancel");
            }

            @Override
            public void onError(FacebookException error) {
//                Log.d(TAG, error.getCause().toString());
                Log.d(TAG, error.getMessage());

            }
        });
        checkLogin();
    }

    private void loginUsingFacebook(String userId) {
        Log.d(TAG, "login using facebook: " + userId);

        startProgessDialog("Logging in...");
        Call<UserDetailsResponse> call = api.loginSocialMedia(new LoginSocialMedia(Const.SOCIAL_FACEBOOK, userId));
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Successful");
                    saveUserDetails(Const.LOGIN_FACEBOOK, response.body());
                } else {
                    int code = response.code();
                    Log.d(TAG, "error code: " + code);

                    if (Const.NOT_FOUND == code) {
                        // REGISTER FACEBOOK;
                        requestFbProfile();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure Facebook login");
            }
        });

    }

    private void requestFbProfile() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d(TAG, object.toString());
                Log.d(TAG, "Response: " + response.toString());

                processFBResponse(object.toString());
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void processFBResponse(String response) {

//        this.response = response;

        try {
            JSONObject jsonObject = new JSONObject(response);

            String username = jsonObject.getString("id");
            String fName = jsonObject.getString("first_name");
            String lName = jsonObject.getString("last_name");
            String email = jsonObject.getString("email");
            String picture = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
            EZSharedPreferences.setPicture(this, picture);
            Log.d(TAG, "Picture: " + picture);
            registerUsingFacebook(username, fName, lName, email, picture, Const.SOCIAL_FACEBOOK);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void registerUsingFacebook(String username, String fName, String lName, String email, String picture, String social_type) {

        Call<UserDetailsResponse> call = api.registerSocialMedia(new RegisterSocialMedia(social_type, username, fName, lName, email, picture));
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                Log.d(TAG, "onResponse Facebook register");
                if (response.isSuccessful()) {
                    saveUserDetails(Const.LOGIN_FACEBOOK, response.body());
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure Facebook register");
            }
        });

    }

    private void saveUserDetails(int loginType, UserDetailsResponse body) {
        Log.d(TAG, "saveUserDetails");
        stopProgressDialog();
        EZSharedPreferences.setLogin(this, true);
        EZSharedPreferences.setLoginType(this, loginType);
        EZSharedPreferences.setUserDetails(this, body.getData());
        startActivity(new Intent(this, DashboardActivity.class));
    }

    private void checkLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null) {
            // naka login;
            containerFB.setVisibility(View.GONE);
            containerLogout.setVisibility(View.VISIBLE);
        } else {
            // hindi naka login;
            containerFB.setVisibility(View.VISIBLE);
            containerLogout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.btnSigin, R.id.tvForgotPw, R.id.fbLogin, R.id.fbLogout, R.id.btnSignup, R.id.btnGoogleSignIn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSigin:
                onSigninClick();
                break;
            case R.id.tvForgotPw:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            case R.id.fbLogin:
                loginFacebook();
                break;
            case R.id.fbLogout:
                logoutFacebook();
                break;
//            case R.id.containerGooglePlus:
//                break;
            case R.id.btnSignup:
                startActivity(new Intent(Login2Activity.this, SignupActivity.class));
                break;

            case R.id.btnGoogleSignIn:
                signIn();
        }
    }

    private void onSigninClick() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        boolean isValid = true;
        if (email.equals("")) {
            isValid = false;
            etEmail.setError("Please enter your email address");
        }

        if (password.equals("")) {
            isValid = false;
            etPassword.setError("Please enter your password");
        }

        if (isValid)
            loginUsingEmail(email, password);
    }

    private void loginUsingEmail(String email, String password) {
        startProgessDialog("Logging in...");
        Log.d(TAG, "email: " + email + " : password: " + password);
        Call<UserDetailsResponse> call = api.postEmail(new LoginEmail(email, password));
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                if (response.isSuccessful()) {
                    saveUserDetails(Const.LOGIN_MANUAL, response.body());
                } else {
                    stopProgressDialog();
                    int code = response.code();
                    Log.d(TAG, "error code: " + code);

                    if (Const.NOT_FOUND == code) {
                        // REGISTER FACEBOOK;
                        showRegisterDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure login email");
                Log.d(TAG, t.getMessage());
                stopProgressDialog();
            }
        });
    }

    private void showRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid login credentials");
        builder.setMessage("Did you forgot your password or trying to sign up?");
        builder.setNegativeButton("Forgot Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        }).setNeutralButton("Sign up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Login2Activity.this, SignupActivity.class));
                finish();
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                etEmail.setText("");
                etPassword.setText("");
            }
        }).show();
    }

    public void logoutFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();
                Log.d(TAG, "Logout successful");

                checkLogin();

            }
        }).executeAsync();
    }

    private void loginFacebook() {
        Collection<String> permissions = Arrays.asList("public_profile", "email");
        LoginManager.getInstance().logInWithReadPermissions(this, permissions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        checkLogin();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

//            txtName.setText(personName);
//            txtEmail.setText(email);
//            Glide.with(getApplicationContext()).load(personPhotoUrl)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfilePic);
//
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
