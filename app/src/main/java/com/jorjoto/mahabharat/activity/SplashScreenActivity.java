package com.jorjoto.mahabharat.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.jorjoto.mahabharat.R;
import com.jorjoto.mahabharat.async.DeviceRegistorAsync;
import com.jorjoto.mahabharat.model.RequestModel;
import com.jorjoto.mahabharat.util.Global_App;
import com.jorjoto.mahabharat.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SplashScreenActivity extends AppCompatActivity {
    public static RequestModel requestModel;
    public static SplashScreenActivity act;
    MediaPlayer mySong;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        act = this;

        try {
            mySong = MediaPlayer.create(SplashScreenActivity.this, R.raw.audio);
            mySong.start();
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
            anim.reset();
            imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.clearAnimation();
            imageView.startAnimation(anim);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getIntent().getExtras().getString("bundle") != null) {
                Utility.setNotificationData(SplashScreenActivity.this, getIntent().getExtras().getString("bundle"));
                Utility.setFromNotification(SplashScreenActivity.this, true);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Utility.setFromNotification(SplashScreenActivity.this, false);
        }

        if (Utility.checkInternetConnection(SplashScreenActivity.this)) {
            String currentDateTimeString = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            if ((Utility.getTodayDate(SplashScreenActivity.this).equals(currentDateTimeString))) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent in = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(in);
                        finish();
                    }
                }, 3900);
            } else {
                Utility.setTodayDate(SplashScreenActivity.this, currentDateTimeString);
                if (Utility.getFCMRegId(SplashScreenActivity.this).trim().length() > 0) {
                    getFCMData(SplashScreenActivity.this);
                } else {
                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Global_App.refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    if (Global_App.refreshedToken != null && Global_App.refreshedToken.trim().length() > 0) {
                        Utility.setFCMRegId(getBaseContext(), Global_App.refreshedToken);
                        getFCMData(this);
                    }
                }
            }
        } else {
            NotifyAndRetry(SplashScreenActivity.this, Global_App.APPNAME, Global_App.msgINTERNET);
        }


    }

    public static void NotifyAndRetry(final Activity activity, String title, String message) {
        if (activity != null) {
            final Dialog dialog1 = new Dialog(activity);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog1.setContentView(R.layout.dialogue_notify);
            dialog1.setCancelable(false);
            Button btnOk = (Button) dialog1.findViewById(R.id.btnSubmit);
            btnOk.setText("Retry");
            TextView txtTitle = (TextView) dialog1.findViewById(R.id.txtTitle);
            txtTitle.setText(title);
            TextView txtMessage = (TextView) dialog1.findViewById(R.id.txtMessage);
            txtMessage.setText(message);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog1 != null) {
                        if (!activity.isFinishing()) {
                            dialog1.dismiss();
                        }
                    }
                    if (Utility.checkInternetConnection(activity)) {
                        if (Utility.getFCMRegId(activity).trim().length() > 0) {
                            getFCMData(activity);
                        } else {
                            try {
                                FirebaseInstanceId.getInstance().deleteInstanceId();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        NotifyAndRetry(activity, Global_App.APPNAME, Global_App.msgINTERNET);
                    }
                }
            });
            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            WindowManager.LayoutParams lp;
            lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog1.getWindow().getAttributes());
            lp.width = (int) (width - (width * 0.07));
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog1.getWindow().setAttributes(lp);
            if (!activity.isFinishing()) {
                dialog1.show();
            }
        }
    }

    public static void getFCMData(Context activity) {
        requestModel = new RequestModel();
        requestModel.setDevice_id(Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
        requestModel.setDevice_version(Build.VERSION.RELEASE);
        requestModel.setDevice_name(Build.MODEL);
        Log.v("AAAAAAA", "=> " + Utility.getFCMRegId(activity));
        requestModel.setDevice_token(Utility.getFCMRegId(activity));
        PackageInfo pInfo = null;
        try {
            pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            if (pInfo != null) {
                requestModel.setApp_version(pInfo.versionName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new DeviceRegistorAsync(SplashScreenActivity.act, requestModel);
    }


}
