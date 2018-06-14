package com.jorjoto.mahabharat.async;

import android.app.Activity;
import android.util.Log;

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

public class GetVideoListAsync {
    private Activity activity;
    private RequestModel requestModel;
    private JSONObject jObject;

    public GetVideoListAsync(final Activity activity, RequestModel requestModel) {
        this.activity = activity;
        this.requestModel = requestModel;

        try {
            jObject = new JSONObject();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseModel> call = apiService.getVideoList("get_video_list", requestModel.getPage());
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response != null) {
                        onPostExecute(response.body());
                        Log.v("AAAAAA", "" + response.body());
                    } else {
                        Utility.NotifyFinish(activity, Global_App.APPNAME, Global_App.msg_Service_Error);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    if (call != null && !call.isCanceled()) {
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
                MainActivity.setVideoData(activity, responseModel);
            } else if (responseModel.getStatus().equals(Global_App.STATUS_ERROR)) {
                Utility.Notify(activity, Global_App.APPNAME, responseModel.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
