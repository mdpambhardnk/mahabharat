package com.jorjoto.mahabharat.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jorjoto.mahabharat.MyApplication;
import com.jorjoto.mahabharat.R;
import com.jorjoto.mahabharat.adapter.HomeVideoListAdapter;
import com.jorjoto.mahabharat.async.GetAdAsync;
import com.jorjoto.mahabharat.async.GetVideoListAsync;
import com.jorjoto.mahabharat.database.DataBaseClass;
import com.jorjoto.mahabharat.model.CategoryModel;
import com.jorjoto.mahabharat.model.RequestModel;
import com.jorjoto.mahabharat.model.ResponseModel;
import com.jorjoto.mahabharat.util.AnalyticsTrackers;
import com.jorjoto.mahabharat.util.Global_App;
import com.jorjoto.mahabharat.util.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.jorjoto.mahabharat.activity.YouTubeVideoActivity.getVideoList;
import static com.jorjoto.mahabharat.activity.YouTubeVideoActivity.responseModel;

public class MainActivity extends AppCompatActivity {

    private static ProgressBar probr,probrpage;
    private static TextView txtMessage;
    static HomeVideoListAdapter homeVideoListAdapter;
    private static ArrayList<CategoryModel> data;
    public static int currenPage = 1, totalRecord = 0;
    private static CategoryModel notiModel;
    public static final int REQUEST_WRITE_PERMISSION = 73;
    public static boolean isFirstBack = false;
    private RequestModel requestModel = new RequestModel();
    static int totalpages;
    NestedScrollView nestedscroll;
    static LinearLayout inflate;
    private static DataBaseClass db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setView();
        MyApplication.getInstance().trackScreenView("MainActivity");
        db  =  new DataBaseClass(MainActivity.this);

        requestModel.setAd_type("2");
        new GetAdAsync(MainActivity.this, requestModel);
    }


    private void setView() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHeaderView("Home");
        probr = (ProgressBar) findViewById(R.id.probr);
        probrpage = (ProgressBar) findViewById(R.id.probrpage);
        txtMessage = (TextView) findViewById(R.id.txtMessage);

        nestedscroll = (NestedScrollView) findViewById(R.id.nestedscroll);
        inflate  =(LinearLayout)findViewById(R.id.inflate);

        getVideoList(MainActivity.this, currenPage + "");

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                requestModel.setAd_type("1");
                new GetAdAsync(MainActivity.this, requestModel);
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 5000);

        nestedscroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nestedscroll.getChildAt(nestedscroll.getChildCount() - 1);
                probrpage.setVisibility(View.VISIBLE);
                int diff = (view.getBottom() - (nestedscroll.getHeight() + nestedscroll
                        .getScrollY()));
                if (diff == 0) {
                    if (currenPage < totalpages) {
                        currenPage = currenPage + 1;
                        getVideoList(MainActivity.this, "" + currenPage);
                    }
                    if (currenPage == totalpages) {
                        if (probrpage != null) {
                            probrpage.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

    }

    public static void getVideoList(Activity activity, String page) {
        if (page.equals("1")) {
            inflate.removeAllViews();
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



    public static void setVideoData(final Activity activity, final ResponseModel responseModel) {
        if (probrpage != null) {
            probrpage.setVisibility(View.GONE);
        }
        if (responseModel != null) {
            probr.setVisibility(View.GONE);
            txtMessage.setVisibility(View.GONE);
            currenPage = Integer.parseInt(responseModel.getCurrentPage());
            totalRecord = Integer.parseInt(responseModel.getTotalItems());
            totalpages = Integer.parseInt(responseModel.getTotalPages());

            final View view = LayoutInflater.from(activity).inflate(R.layout.video_list_inflater, null);
            RecyclerView rcList = (RecyclerView) view.findViewById(R.id.rcList);
            ImageView imglogo = (ImageView) view.findViewById(R.id.imglogo);
            ImageView imgclose = (ImageView) view.findViewById(R.id.imgclose);
            ImageView adimage = (ImageView) view.findViewById(R.id.adimage);
            final ProgressBar probr = (ProgressBar) view.findViewById(R.id.probr);
            TextView texttitle = (TextView) view.findViewById(R.id.texttitle);
            TextView textdescription = (TextView) view.findViewById(R.id.textdescription);
            Button btninstall = (Button) view.findViewById(R.id.btninstall);
            final RelativeLayout relativenative = (RelativeLayout) view.findViewById(R.id.relativenative);
            relativenative.setVisibility(View.VISIBLE);
            if (Utility.getshowadd(activity).equals("1")) {
                relativenative.setVisibility(View.VISIBLE);
            } else {
                relativenative.setVisibility(View.GONE);
            }
            if (responseModel.getAdsData() != null) {
                relativenative.setVisibility(View.VISIBLE);
                final CategoryModel model = responseModel.getAdsData().get(0);
                final Cursor cursor = db.iscloseadsadded(model.getAd_id());
                if (cursor.getCount() != 0) {
                    relativenative.setVisibility(View.GONE);
                }

                final Cursor cursorads = db.isadsadded(model.getAd_id());
                if (cursorads.getCount() == 0) {
                    if (cursor.getCount() == 0) {
                        relativenative.setVisibility(View.VISIBLE);
                    }
                    db.insertadsData(model.getAd_id(), model.getAd_type());
                   //  Toast.makeText(activity, "Add" + model.getAd_id(), Toast.LENGTH_SHORT).show();
                } else {
                    relativenative.setVisibility(View.GONE);
                   //  Toast.makeText(activity, "Already Added" + model.getAd_id(), Toast.LENGTH_SHORT).show();

                }
                Cursor favcursor = db.getadsData();
                favcursor.moveToFirst();
                for (int i = 0; i < favcursor.getCount(); i++) {
                    favcursor.moveToNext();
                }

                imgclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String todaydate = df.format(c);
                        SharedPreferences date = activity.getSharedPreferences("todaydate", MODE_PRIVATE);
                        SharedPreferences.Editor dateeditor = date.edit();
                        dateeditor.putString("date", todaydate);
                        dateeditor.commit();
                        String storedate = date.getString("date", null);
                        if (!storedate.equals(todaydate)) {
                            db.deletecloseadsData();
                            dateeditor.putString("date", todaydate);
                            dateeditor.commit();
                            db.insertcloseadsData(model.getAd_id(), model.getAd_type());
                            relativenative.setVisibility(View.GONE);
                        } else {
                            db.insertcloseadsData(model.getAd_id(), model.getAd_type());
                            relativenative.setVisibility(View.GONE);
                        }
                    }
                });
                if (model.getApplication_name() != null) {
                    texttitle.setText(model.getApplication_name());
                }
                if (model.getDescription() != null && model.getDescription().trim().length() > 0) {
                    textdescription.setVisibility(View.VISIBLE);
                    textdescription.setText(model.getDescription());
                } else {
                    textdescription.setVisibility(View.GONE);
                }
                adimage.setVisibility(View.VISIBLE);

                Picasso.with(activity)
                        .load(model.getSource_path())
                        .into(adimage);

                Picasso.with(activity).load(model.getSource_path()).into(adimage, new Callback() {
                    public void onSuccess() {
                        if (probr != null) {
                            probr.setVisibility(View.GONE);
                        }
                    }

                    public void onError() {
                        if (probr != null) {
                            probr.setVisibility(View.GONE);
                        }

                    }
                });


                btninstall.setText(model.getButton_title());
                Picasso.with(activity)
                        .load(model.getApplication_logo())
                        .into(imglogo);

                btninstall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utility.adclick(activity, model.getAd_id());
                        String market_uri = model.getRedirect_url();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(market_uri));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                                .FLAG_ACTIVITY_NO_ANIMATION);
                        activity.startActivity(intent);
                    }
                });
            } else {
                relativenative.setVisibility(View.GONE);
            }

            final LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false);
            rcList.setLayoutManager(layoutManager);
            rcList.setNestedScrollingEnabled(false);
            rcList.setLayoutManager(layoutManager);
            homeVideoListAdapter = new HomeVideoListAdapter(activity, responseModel.getMainData());
            rcList.setAdapter(homeVideoListAdapter);
            inflate.addView(view);


        } else {
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
    protected void onResume() {
        super.onResume();
        requestModel.setAd_type("2");
        new GetAdAsync(MainActivity.this, requestModel);
        db.deleteadsData();
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
