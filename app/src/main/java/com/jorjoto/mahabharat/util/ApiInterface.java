package com.jorjoto.mahabharat.util;

import com.jorjoto.mahabharat.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("mahabharat_api.php")
    Call<ResponseModel> DeviceRegistor(@Field("method") String method,
                                       @Field("device_id") String device_id,
                                       @Field("device_name") String device_name,
                                       @Field("device_token") String device_token,
                                       @Field("device_version") String device_version,
                                       @Field("app_version") String app_version);

    @FormUrlEncoded
    @POST("mahabharat_api.php")
    Call<ResponseModel> getVideoList(@Field("method") String method,
                                       @Field("page") String device_id);


    @FormUrlEncoded
    @POST("mahabharat_api.php")
    Call<ResponseModel> getVideodetail(@Field("method") String method,
                                     @Field("video_id") String device_id);
}
