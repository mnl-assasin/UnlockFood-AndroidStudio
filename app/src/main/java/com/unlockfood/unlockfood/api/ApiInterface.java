package com.unlockfood.unlockfood.api;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by mykelneds on 16/04/2017.
 */

public interface ApiInterface {

    @POST("login")
    Call<UserDetailsResponse> postEmail(@Body LoginEmail request);

    @POST("login")
    Call<UserDetailsResponse> loginSocialMedia(@Body LoginSocialMedia request);

    @POST("register")
    Call<UserDetailsResponse> registerEmail(@Body RegisterEmail request);

    @POST("register")
    Call<UserDetailsResponse> registerSocialMedia(@Body RegisterSocialMedia request);

    @GET("users/{id}")
    Call<UserDetailsResponse> getUserDetails(@Path("id") String id);

    @GET("hall")
    Call<HallOfFameResponse> getHallOfFame();

    @GET("levels")
    Call<RewardsResponse> getLevels();

    @Multipart
    @POST("users/{id}/avatar")
    Call<UserDetailsResponse> postPicture(@Path("id") String id, @Part MultipartBody.Part file);

    @POST("users/{id}")
    Call<UserDetailsResponse> postUpdateProfile(@Path("id") String id, @Body ProfileUpdateRequest request);

    @POST("clicks")
    Call<Void> postAdClicks(@Body AdClickRequest request);

    @POST("users/password-reset")
    Call<Void> postResetPassword(@Body ResetPasswordRequest request);

    @POST("users/{id}/password")
    Call<Void> postUpdatePassword(@Path("id") String id, @Body UpdatePasswordRequest request);

}
