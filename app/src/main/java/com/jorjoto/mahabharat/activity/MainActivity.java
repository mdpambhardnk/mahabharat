package com.jorjoto.mahabharat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jorjoto.mahabharat.R;
import com.jorjoto.mahabharat.adapter.HomeVideoListAdapter;
import com.jorjoto.mahabharat.async.GetVideoListAsync;
import com.jorjoto.mahabharat.model.CategoryModel;
import com.jorjoto.mahabharat.model.RequestModel;
import com.jorjoto.mahabharat.model.ResponseModel;
import com.jorjoto.mahabharat.util.Global_App;
import com.jorjoto.mahabharat.util.Utility;

import java.util.ArrayList;

import static com.jorjoto.mahabharat.activity.YouTubeVideoActivity.getVideoList;
import static com.jorjoto.mahabharat.activity.YouTubeVideoActivity.responseModel;

public class MainActivity extends AppCompatActivity {

    private static ProgressBar probr;
    private static TextView txtMessage;
    private static RecyclerView rcList;
    static HomeVideoListAdapter homeVideoListAdapter;
    private static ArrayList<CategoryModel> data;
    public static int currenPage = 1, totalRecord = 0;
    private static CategoryModel notiModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setView();
    }


    private void setView() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHeaderView("Home");
        probr = (ProgressBar) findViewById(R.id.probr);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        rcList = (RecyclerView) findViewById(R.id.rcList);
        rcList.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        getVideoList(MainActivity.this, "1");
    }

    public static void getVideoList(Activity activity, String page) {
        RequestModel requestModel = new RequestModel();
        requestModel.setPage(page);
        new GetVideoListAsync(activity, requestModel);
    }

    public void setHeaderView(String title) {
        View v = LayoutInflater.from(this).inflate(R.layout.actionbar_home, null);
        TextView action_bar_title = (TextView) v.findViewById(R.id.action_bar_title);
        action_bar_title.setText(title);
        ImageView imgShare = (ImageView) v.findViewById(R.id.imgShare);
        imgShare.setImageResource(R.drawable.icon_share);
        getSupportActionBar().setCustomView(v);
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public static void setVideoData(Activity activity, ResponseModel responseModel) {
        if (responseModel != null) {
            probr.setVisibility(View.GONE);
            txtMessage.setVisibility(View.GONE);
            if (responseModel.getMainData() != null && responseModel.getMainData().size() > 0) {
                currenPage = Integer.parseInt(responseModel.getCurrentPage());
                totalRecord = Integer.parseInt(responseModel.getTotalItems());
                if (currenPage == 1) {
                    data = new ArrayList<CategoryModel>();
                    data = responseModel.getMainData();
                    homeVideoListAdapter = new HomeVideoListAdapter(activity, data);
                    rcList.setAdapter(homeVideoListAdapter);
                } else {
                    data.addAll(responseModel.getMainData());
                    homeVideoListAdapter.notifyDataSetChanged();
                }
            } else {
                probr.setVisibility(View.GONE);
                txtMessage.setVisibility(View.VISIBLE);
            }
        } else {
            rcList.setVisibility(View.GONE);
            probr.setVisibility(View.GONE);
            txtMessage.setVisibility(View.VISIBLE);
        }

        try {
            if (Utility.getFromNotification(activity)) {
                Utility.setFromNotification(activity, false);
                MainActivity.notiModel = Utility.getNotificationData(activity);
                if (MainActivity.notiModel != null) {
                    ((MainActivity) activity).FromNotification(MainActivity.notiModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FromNotification(CategoryModel notificationModel) {
        if (notificationModel.getRedirectScreen() != null && notificationModel.getRedirectScreen().equals(Global_App.SCREEN_VIDEODETAILS)) {
            Intent in = new Intent(MainActivity.this, YouTubeVideoActivity.class);
            in.putExtra("videoId", notificationModel.getVideoId());
            startActivity(in);
        }
    }
}
