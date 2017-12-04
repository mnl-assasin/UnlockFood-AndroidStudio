package com.unlockfood.unlockfood.fragment;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.activity.MainActivity;
import com.unlockfood.unlockfood.api.ApiClient;
import com.unlockfood.unlockfood.api.ApiInterface;
import com.unlockfood.unlockfood.api.ProfileUpdateRequest;
import com.unlockfood.unlockfood.api.UpdatePasswordRequest;
import com.unlockfood.unlockfood.api.UserDetailsData;
import com.unlockfood.unlockfood.api.UserDetailsResponse;
import com.unlockfood.unlockfood.builder.ToastBuilder;
import com.unlockfood.unlockfood.data.EZSharedPreferences;
import com.unlockfood.unlockfood.utils.FileUtils;
import com.unlockfood.unlockfood.utils.Util;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment {

    String TAG = SettingsFragment.class.getSimpleName();

    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.btnUpload)
    Button btnUpload;
    @Bind(R.id.tvId)
    TextView tvId;
    @Bind(R.id.ivProfile)
    ImageView ivProfile;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.etFName)
    EditText etFName;
    @Bind(R.id.etLName)
    EditText etLName;
    @Bind(R.id.btnUpdate)
    Button btnUpdate;
    @Bind(R.id.etOldPw)
    EditText etOldPw;
    @Bind(R.id.etNewPw)
    EditText etNewPw;
    @Bind(R.id.etConfirmPw)
    EditText etConfirmPw;
    @Bind(R.id.btnChangePW)
    Button btnChangePW;
    @Bind(R.id.containerPassword)
    LinearLayout containerPassword;
    @Bind(R.id.btnLogout)
    Button btnLogout;

    ApiInterface api;

    int PICK_IMAGE = 100;
    String selectedImage = "";

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    Uri imageUri = null;

    String id;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        initPermission();

        api = ApiClient.getClient().create(ApiInterface.class);

        if (Util.isInternetAvailable(getActivity()))
            initData();
        else
            ToastBuilder.shortToast(getActivity(), "Please connect to internet and try again");
        return view;
    }

    private void initPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), REQUIRED_PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), REQUIRED_PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[2]) != PackageManager.PERMISSION_GRANTED
                ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])
//                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[2])
                    ) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs read and write storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (EZSharedPreferences.getManifestWriteStorage(getActivity())) { // WRITE STORAGE PERMISSION
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs read and write storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getActivity(), "Go to Permissions to Grant read and write storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSION_CALLBACK_CONSTANT);
            }

            Log.d(TAG, "Permissions required?");
//            txtPermissions.setText("Permissions Required");
//            SharedPreferences.Editor editor = permissionStatus.edit();
//            editor.putBoolean(REQUIRED_PERMISSIONS[0],true);
//            editor.commit();
        } else {

            Log.d(TAG, "Proceed to after permission process");
//            initData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        final String mTAG = "onRequestPermission: ";
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;

                } else {
                    allgranted = false;
                    break;
                }
            }


            if (allgranted) {
                Log.d(TAG, mTAG + "Proceed to after permission process");
                EZSharedPreferences.setManifestWriteStorage(getActivity(), true);
//                proceedAfterPermission();
//                initData();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {
//                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[2])) {
                //txtPermissions.setText("Permissions Required");
                Log.d(TAG, mTAG + "All Permissions required");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs read and write storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getActivity(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void initData() {


        UserDetailsData details = EZSharedPreferences.getUserDetails(getActivity());
        id = String.valueOf(details.getId());
        tvId.setText(id);
        tvName.setText(details.getFirstName() + " " + details.getLastName());
        etFName.setText(details.getFirstName());
        etLName.setText(details.getLastName());

        String path = details.getProfilePictureUrl();
        if (path != null)
            if (!path.equals(""))
                Picasso.with(getActivity()).load(details.getProfilePictureUrl()).error(R.drawable.img_profile).into(ivProfile);

//        ivProfile
//        etEmail.setText(details.get);
        String social = details.getSocialType();
        if (social.equals("manual"))
            containerPassword.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.btnUpload, R.id.btnUpdate, R.id.btnChangePW, R.id.btnLogout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpload:
                getProfilePicture();
                break;
            case R.id.btnUpdate:
                updateAccount();
                break;
            case R.id.btnChangePW:
                Log.d(TAG, "CHANGE PASS");
                if (Util.isInternetAvailable(getActivity()))
                    changePass();
                else
                    ToastBuilder.shortToast(getActivity(), "Please connect to internet and try again");
                break;
            case R.id.btnLogout:
                EZSharedPreferences.setLogin(getActivity(), false);
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                break;
        }
    }

    private void changePass() {
        String oldPass = etOldPw.getText().toString().trim();
        String newPass = etNewPw.getText().toString().trim();
        String confirmPass = etConfirmPw.getText().toString().trim();

        boolean isValid = true;
        if (oldPass.equals("")) {
            isValid = false;
            ToastBuilder.shortToast(getActivity(), "Please enter your current password");
        } else if (newPass.equals("")) {
            isValid = false;
            ToastBuilder.shortToast(getActivity(), "Please enter a new password");
        } else if (confirmPass.equals("")) {
            isValid = false;
            ToastBuilder.shortToast(getActivity(), "Please confirm your new password");
        }

        if (!newPass.equals(confirmPass)) {
            isValid = false;
            ToastBuilder.shortToast(getActivity(), "Your new password does not match");
        }

        if (isValid) {
            updatePassword(oldPass, newPass);
        }

    }

    private void updatePassword(String oldPass, String newPass) {
        startProgressdialog("Updating password...");
        Call<Void> call = api.postUpdatePassword(id, new UpdatePasswordRequest(oldPass, newPass));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                stopProgressDialog();
                if (response.isSuccessful()) {
                    ToastBuilder.shortToast(getActivity(), "Password updated successfully!");
                } else {
                    Util.showResponseError(getActivity(), response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                stopProgressDialog();
                ToastBuilder.shortToast(getActivity(), t.toString());
            }
        });
    }

    private void updateAccount() {
        if (imageUri != null) {
            startProgressdialog("Uploading profile picture...");
            MultipartBody.Part body = prepareFilePart(getActivity(), "profile_picture", imageUri);
            Call<UserDetailsResponse> call = api.postPicture(id, body);
            call.enqueue(new Callback<UserDetailsResponse>() {
                @Override
                public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                    stopProgressDialog();
                    updateName();
                    if (response.isSuccessful()) {
                        stopProgressDialog();
                        EZSharedPreferences.setUserDetails(getActivity(), response.body().getData());
                        ToastBuilder.shortToast(getActivity(), "Profile picture save");
                    } else {
                        Util.showResponseError(getActivity(), response);
                    }
                }

                @Override
                public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                    stopProgressDialog();
                    updateName();
                    ToastBuilder.shortToast(getActivity(), t.toString());
                }
            });

        } else {
            updateName();
        }

    }

    private void updateName() {

        imageUri = null;
        HashMap<String, String> map;
        String fName = etFName.getText().toString().trim();
        String lName = etLName.getText().toString().trim();

        boolean isValid = true;

        if (fName.equals("")) {
            ToastBuilder.shortToast(getActivity(), "Please enter your first name");
            isValid = false;
        }

        if (lName.equals("")) {
            ToastBuilder.shortToast(getActivity(), "Please enter your last name");
            isValid = false;
        }

        if (isValid) {
            startProgressdialog("Updating profile...");
            Call<UserDetailsResponse> call = api.postUpdateProfile(id, new ProfileUpdateRequest(fName, lName));
            call.enqueue(new Callback<UserDetailsResponse>() {
                @Override
                public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                    stopProgressDialog();
                    if (response.isSuccessful()) {
                        EZSharedPreferences.setUserDetails(getActivity(), response.body().getData());
                        ToastBuilder.shortToast(getActivity(), "Profile changed successfully!");
                    } else {
                        Util.showResponseError(getActivity(), response);
                    }
                }

                @Override
                public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                    stopProgressDialog();
                    ToastBuilder.shortToast(getActivity(), t.toString());
                }
            });
            Log.d("mykSettings", call.request().url().toString());

        }


    }

    public void getProfilePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            imageUri = uri;
            Picasso.with(getActivity()).load(uri).into(ivProfile);
        }
    }

    public static MultipartBody.Part prepareFilePart(Context ctx, String partName, Uri fileUri) {

        if (fileUri == null)
            return null;

        File file = FileUtils.getFile(ctx, fileUri);
        Log.d("FileHelper", "File: " + file.getAbsolutePath());
        RequestBody requestFile = RequestBody.create(MediaType.parse(ctx.getContentResolver().getType(fileUri)), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
