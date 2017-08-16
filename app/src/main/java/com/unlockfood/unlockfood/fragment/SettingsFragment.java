package com.unlockfood.unlockfood.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unlockfood.unlockfood.R;
import com.unlockfood.unlockfood.activity.MainActivity;
import com.unlockfood.unlockfood.api.ApiClient;
import com.unlockfood.unlockfood.api.ApiInterface;
import com.unlockfood.unlockfood.api.ProfileUpdateRequest;
import com.unlockfood.unlockfood.api.UserDetailsData;
import com.unlockfood.unlockfood.api.UserDetailsResponse;
import com.unlockfood.unlockfood.builder.ToastBuilder;
import com.unlockfood.unlockfood.data.EZSharedPreferences;
import com.unlockfood.unlockfood.utils.FileUtils;

import java.io.File;
import java.io.IOException;
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
    @Bind(R.id.btnLogout)
    Button btnLogout;

    ApiInterface api;

    int PICK_IMAGE = 100;
    String selectedImage = "";
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

        initData();
        return view;
    }

    private void initData() {

        UserDetailsData details = EZSharedPreferences.getUserDetails(getActivity());
        id = String.valueOf(details.getId());
        tvId.setText(id);
        tvName.setText(details.getFirstName() + " " + details.getLastName());
        etFName.setText(details.getFirstName());
        etLName.setText(details.getLastName());
        Picasso.with(getActivity()).load(details.getProfilePictureUrl()).error(R.drawable.img_profile).into(ivProfile);

//        ivProfile
//        etEmail.setText(details.get);

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
                break;
            case R.id.btnLogout:
                EZSharedPreferences.setLogin(getActivity(), false);
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                break;
        }
    }

    private void updateAccount() {

        api = ApiClient.getClient().create(ApiInterface.class);

        if (imageUri != null) {
            startProgressdialog("Uploading profile picture...");
            MultipartBody.Part body = prepareFilePart(getActivity(), "profile_picture", imageUri);

//            Call<Void> call = api.postPicture(id, body);
//            call.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    if (response.isSuccessful()) {
//                        stopProgressDialog();
//                        ToastBuilder.shortToast(getActivity(), "Profile picture save");
//                    } else {
//                        ToastBuilder.shortToast(getActivity(), "Something went wrong, please try again");
//                    }
//                    updateName();
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    stopProgressDialog();
//                    ToastBuilder.shortToast(getActivity(), "Something went wrong, please try again");
//                    updateName();
//                }
//            });

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
                        ToastBuilder.shortToast(getActivity(), "Something went wrong, please try again");
                    }
                }

                @Override
                public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                    stopProgressDialog();
                    updateName();
                    ToastBuilder.shortToast(getActivity(), "Something went wrong, please try again");
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
                        try {
                            Log.d("mykSettings", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ToastBuilder.shortToast(getActivity(), "Something went wrong, please try again");
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
