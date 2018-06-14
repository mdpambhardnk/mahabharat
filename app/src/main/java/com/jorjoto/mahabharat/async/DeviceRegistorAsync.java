package com.jorjoto.mahabharat.async;

import android.app.Activity;
import android.content.Intent;

import com.jorjoto.mahabharat.activity.MainActivity;
import com.jorjoto.mahabharat.activity.YouTubeVideoActivity;
import com.jorjoto.mahabharat.model.RequestModel;
import com.jorjoto.mahabharat.model.ResponseModel;
import com.jorjoto.mahabharat.util.ApiClient;
import com.jorjoto.mahabharat.util.ApiInterface;
import com.jorjoto.mahabharat.util.Global_App;
import com.jorjoto.mahabharat.util.Utility;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceRegistorAsync {
    private Activity activity;
    private RequestModel requestModel;
    private JSONObject jObject;

    public DeviceRegistorAsync(final Activity activity, RequestModel requestModel) {
        this.activity = activity;
        this.requestModel = requestModel;

        try {
            jObject = new JSONObject();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseModel> call = apiService.DeviceRegistor("get_device", requestModel.getDevice_id(), requestModel.getDevice_name(), requestModel.getDevice_token(), requestModel.getDevice_version(), requestModel.getApp_version());
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response != null) {
                        onPostExecute(response.body());
                    } else {
                        Utility.NotifyFinish(activity, Global_App.APPNAME, Global_App.msg_Service_Error);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Utility.NotifyFinish(activity, Global_App.APPNAME, Global_App.msg_Service_Error);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onPostExecute(ResponseModel responseModel) {
        try {
            if (responseModel.getStatus().equals(Global_App.STATUS_SUCCESS)) {
                Utility.setIsDeviceRegistor(activity, "1");
                Intent in = new Intent(activity, MainActivity.class);
                activity.startActivity(in);
                activity.finish();
            } else if (responseModel.getStatus().equals(Global_App.STATUS_ERROR)) {
                Utility.Notify(activity, Global_App.APPNAME, responseModel.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
