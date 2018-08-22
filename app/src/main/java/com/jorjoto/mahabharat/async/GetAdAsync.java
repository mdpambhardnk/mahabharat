package com.jorjoto.mahabharat.async;

import android.app.Activity;
import android.util.Log;

import com.jorjoto.mahabharat.model.RequestModel;
import com.jorjoto.mahabharat.model.ResponseModel;
import com.jorjoto.mahabharat.util.ApiClient;
import com.jorjoto.mahabharat.util.ApiInterface;
import com.jorjoto.mahabharat.util.Global_App;
import com.jorjoto.mahabharat.util.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAdAsync {
    private Activity activity;
    RequestModel requestModel;

    public GetAdAsync(final Activity activity, final RequestModel requestModel) {
        this.requestModel = requestModel;
        this.activity = activity;
        try {
            if (Utility.getshowadd(activity).equals("1")) {
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseModel> call = apiService.GetAd("display_ad", requestModel.getAd_type());
                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.body() != null) {
                            onPostExecute(response.body());
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        if (!call.isCanceled()) {
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPostExecute(ResponseModel responseModel) {
        try {
            if (responseModel.getStatus().equals(Global_App.STATUS_SUCCESS)) {
                   Utility.advertisement(activity, responseModel);
            } else if (responseModel.getStatus().equals(Global_App.STATUS_ERROR)) {
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
