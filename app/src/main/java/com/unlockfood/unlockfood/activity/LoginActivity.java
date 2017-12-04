package com.unlockfood.unlockfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.api.ApiClient;
import com.unlockfood.unlockfood.api.ApiInterface;
import com.unlockfood.unlockfood.api.LoginEmail;
import com.unlockfood.unlockfood.api.LoginSocialMedia;
import com.unlockfood.unlockfood.api.RegisterSocialMedia;
import com.unlockfood.unlockfood.api.UserDetailsData;
import com.unlockfood.unlockfood.api.UserDetailsResponse;
import com.unlockfood.unlockfood.builder.ToastBuilder;
import com.unlockfood.unlockfood.data.Const;
import com.unlockfood.unlockfood.data.EZSharedPreferences;
import com.unlockfood.unlockfood.widgets.ButtonMyriad;
import com.unlockfood.unlockfood.widgets.TextViewMyriad;

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

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.btnSigin)
    Button btnSigin;
    @Bind(R.id.tvForgotPw)
    TextViewMyriad tvForgotPw;
    @Bind(R.id.fbLogin)
    LinearLayout containerFB;
    @Bind(R.id.fbLogout)
    LinearLayout containerLogout;
    @Bind(R.id.btnGoogleSignIn)
    SignInButton btnGoogleSignIn;
    @Bind(R.id.googleLogout)
    LinearLayout googleLogout;
    @Bind(R.id.btnSignup)
    ButtonMyriad btnSignup;

    ApiInterface api;
    GoogleApiClient mGoogleApiClient;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initData();
        initGoogleLogin();
        initFBLogin();

    }

    private void initData() {

        api = ApiClient.getClient().create(ApiInterface.class);
//        logoutGoogle();

    }


    private void initGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    private void initFBLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String userId = loginResult.getAccessToken().getUserId();
                loginUsingFacebook(userId);
                updateFacebookUI();


            }


            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, error.toString());
                error.printStackTrace();
            }
        });
    }

    private void loginUsingFacebook(String userId) {
        Log.d(TAG, "login using facebook: " + userId);
        startProgressDialog("Logging in...");
        Call<UserDetailsResponse> call = api.loginSocialMedia(new LoginSocialMedia(Const.SOCIAL_FACEBOOK, userId));
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Successful");
                    saveUserDetails(response.body().getData());
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
            registerSocial(Const.SOCIAL_FACEBOOK, username, fName, lName, email, picture);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick({R.id.btnSigin, R.id.fbLogin, R.id.fbLogout, R.id.btnGoogleSignIn, R.id.googleLogout, R.id.btnSignup, R.id.tvForgotPw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSigin:
                onSignInClick();
                break;
            case R.id.fbLogin:
                signInFB();
                break;
            case R.id.fbLogout:
                logoutFB();
                break;
            case R.id.btnGoogleSignIn:
                googleSignIn();
                break;
            case R.id.googleLogout:
                logoutGoogle();
                break;
            case R.id.btnSignup:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.tvForgotPw:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void onSignInClick() {
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
        startProgressDialog("Logging in...");
        Log.d(TAG, "email: " + email + " : password: " + password);
        Call<UserDetailsResponse> call = api.postEmail(new LoginEmail(email, password));
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                if (response.isSuccessful()) {
                    saveUserDetails(response.body().getData());
                } else {
                    stopProgressDialog();
                    int code = response.code();
                    Log.d(TAG, "error code: " + code);

                    if (Const.NOT_FOUND == code) {
                        // REGISTER FACEBOOK;
                        ToastBuilder.shortToast(getApplicationContext(), "Incorrect username or password, please try again");
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

    private void signInFB() {
        Collection<String> permissions = Arrays.asList("public_profile", "email");
        LoginManager.getInstance().logInWithReadPermissions(this, permissions);
    }

    private void logoutFB() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();
                Log.d(TAG, "Logout successful");

                updateFacebookUI();

            }
        }).executeAsync();
    }

    private void googleSignIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, Const.REQUEST_GOOGLE);
    }

    private void logoutGoogle() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateGoogleUI(false);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.REQUEST_GOOGLE && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            startProgressDialog("processing...");
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    stopProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String username = acct.getId();
            String firstName = acct.getGivenName();
            String lastName = acct.getFamilyName();
            String email = acct.getEmail();
            String picture = "";
            if (acct.getPhotoUrl() != null)
                picture = acct.getPhotoUrl().toString();

            loginUsingGoogle(username, result);


            updateGoogleUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateGoogleUI(false);
        }
    }

    private void loginUsingGoogle(String userId, final GoogleSignInResult result) {
        Log.d(TAG, "UserId: " + userId);
        startProgressDialog("Logging in...");
        Call<UserDetailsResponse> call = api.loginSocialMedia(new LoginSocialMedia(Const.SOCIAL_GOOGLE, userId));
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                if (response.isSuccessful()) {
                    saveUserDetails(response.body().getData());
                } else {
                    if (response.code() == Const.NOT_FOUND)
                        requestGoogleProfile(result);
                }
            }


            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                stopProgressDialog();
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void requestGoogleProfile(GoogleSignInResult result) {
        Log.d(TAG, "requestGoogleProfile");
        GoogleSignInAccount acct = result.getSignInAccount();
        String username = acct.getId();
        String firstName = acct.getGivenName();
        String lastName = acct.getFamilyName();
        String email = acct.getEmail();
        String picture = "";
        if (acct.getPhotoUrl() != null)
            picture = acct.getPhotoUrl().toString();

        registerSocial(Const.SOCIAL_GOOGLE, username, firstName, lastName, email, picture);

    }

    private void registerSocial(String socialType, String username, String firstName, String lastName, String email, String picture) {

        Call<UserDetailsResponse> call = api.registerSocialMedia(new RegisterSocialMedia(socialType, username, firstName, lastName, email, picture));
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                if (response.isSuccessful())
                    saveUserDetails(response.body().getData());
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                stopProgressDialog();
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void saveUserDetails(UserDetailsData data) {
        Log.d(TAG, "Save user details");
        stopProgressDialog();
        EZSharedPreferences.setLogin(this, true);
        EZSharedPreferences.setUserDetails(this, data);
        startActivity(new Intent(this, DashboardActivity.class));

    }

    private void updateFacebookUI() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null) {
            // naka login;
            containerFB.setVisibility(View.GONE);
            containerLogout.setVisibility(View.VISIBLE);
//            logoutFB();
        } else {
            // hindi naka login;
            containerFB.setVisibility(View.VISIBLE);
            containerLogout.setVisibility(View.GONE);
        }
    }

    private void updateGoogleUI(boolean b) {
        Log.d(TAG, "updateGoogleUI");
        if (b) {
            btnGoogleSignIn.setVisibility(View.GONE);
            googleLogout.setVisibility(View.VISIBLE);
            logoutGoogle();
        } else {
            btnGoogleSignIn.setVisibility(View.VISIBLE);
            googleLogout.setVisibility(View.GONE);

        }
    }

}