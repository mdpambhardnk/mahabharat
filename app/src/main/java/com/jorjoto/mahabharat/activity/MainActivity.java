package com.jorjoto.mahabharat.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jorjoto.mahabharat.MyApplication;
import com.jorjoto.mahabharat.R;
import com.jorjoto.mahabharat.adapter.HomeVideoListAdapter;
import com.jorjoto.mahabharat.async.GetVideoListAsync;
import com.jorjoto.mahabharat.model.CategoryModel;
import com.jorjoto.mahabharat.model.RequestModel;
import com.jorjoto.mahabharat.model.ResponseModel;
import com.jorjoto.mahabharat.util.AnalyticsTrackers;
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
    public static final int REQUEST_WRITE_PERMISSION = 73;
    public static boolean isFirstBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setView();
        MyApplication.getInstance().trackScreenView("MainActivity");
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
        if (page.equals("1")) {
            rcList.setVisibility(View.GONE);
            txtMessage.setVisibility(View.GONE);
            probr.setVisibility(View.VISIBLE);
        }
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
                share();
            }
        });
    }


    public static void setVideoData(Activity activity, ResponseModel responseModel) {
        if (responseModel != null) {
            probr.setVisibility(View.GONE);
            txtMessage.setVisibility(View.GONE);
            rcList.setVisibility(View.VISIBLE);
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
            } else {
                if (Utility.getVideoId(activity).trim().length() > 0) {
                    Intent in = new Intent(activity, YouTubeVideoActivity.class);
                    in.putExtra("videoId", Utility.getVideoId(activity).trim());
                    Utility.setVideoId(activity, "");
                    activity.startActivity(in);
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

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == 0 && grantResults[1] == 0) {
            Utility.shareImageData(MainActivity.this);
        } else {
            Utility.Notify(MainActivity.this, Global_App.APPNAME, "permission denied.");
        }

    }

    public void share() {
        if (Utility.getAppShareImage(MainActivity.this).trim().length() > 0) {
            if (Build.VERSION.SDK_INT >= 23) {
                int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int hasWriteContactsPermission1 = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED || hasWriteContactsPermission1 != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 73);
                } else {
                    Utility.shareImageData(MainActivity.this);
                }
            } else {
                Utility.shareImageData(MainActivity.this);
            }
        } else {
            Utility.shareImageData(MainActivity.this);

        }
    }

    @Override
    public void onBackPressed() {
        if (isFirstBack) {
            super.onBackPressed();
            return;
        }
        this.isFirstBack = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isFirstBack = false;
            }
        }, 2000);
    }
}
