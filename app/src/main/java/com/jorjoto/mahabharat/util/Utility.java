package com.jorjoto.mahabharat.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.jorjoto.mahabharat.R;
import com.jorjoto.mahabharat.async.DownloadImageShareAsync;
import com.jorjoto.mahabharat.database.DataBaseClass;
import com.jorjoto.mahabharat.model.CategoryModel;
import com.jorjoto.mahabharat.model.ResponseModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.xml.sax.helpers.LocatorImpl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.content.FileProvider.getUriForFile;

public class Utility {
    private static CategoryModel notificationModel;
    private static boolean finish = false;
    private static DataBaseClass db;

    public static void setFCMRegId(Context activity, String regId) {
        SharedPreferences pref = activity.getSharedPreferences("DEVICETOKEN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (regId != null) {
            editor.putString("FCMregId", regId);
        } else {
            editor.putString("FCMregId", "");
        }
        editor.commit();
    }

    public static boolean checkInternetConnection(Activity context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null && cm.getActiveNetworkInfo() != null) {
                return cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
            }
        }
        return false;
    }


    public static String getIsDeviceRegistor(Context activity) {
        if (activity != null) {
            SharedPreferences pref = activity.getSharedPreferences("IsDeviceRegistor", 0);
            String token = pref.getString("IsDeviceRegistor", "");
            return token;
        }
        return "";
    }

    public static void setIsDeviceRegistor(Context activity, String LatLng) {
        SharedPreferences pref = activity.getSharedPreferences("IsDeviceRegistor", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("IsDeviceRegistor", LatLng);
        editor.commit();
    }


    public static String getTodayDate(Context activity) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        String regId = pref.getString("TODAYDATE", "");
        return regId;
    }

    public static void setTodayDate(Context activity, String token) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("TODAYDATE", token);
        editor.commit();
    }


    public static String getFCMRegId(Context activity) {
        String regId = "";
        if (activity != null) {
            SharedPreferences pref = activity.getSharedPreferences("DEVICETOKEN", Context.MODE_PRIVATE);
            if (pref != null) {
                regId = pref.getString("FCMregId", "");
            } else {
                regId = "";
            }
        } else {
            regId = "";
        }
        return regId;
    }

    public static void setFromNotification(Context activity, boolean isfrom) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
        editor.putBoolean("isfrom", isfrom);
        editor.commit();
    }

    public static Boolean getFromNotification(Context activity) {
        return Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("isfrom", false));
    }

    public static void setNotificationData(Context activity, String NDATA) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
        editor.putString("NDATA", NDATA);
        editor.commit();
    }

    public static String getAppShareImage(Context activity) {
        SharedPreferences pref = activity.getSharedPreferences("appShareImage", 0);
        String token = pref.getString("appShareImage", "");
        return token;
    }

    public static void setAppShareImage(Context activity, String flag) {
        SharedPreferences pref = activity.getSharedPreferences("appShareImage", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("appShareImage", flag);
        editor.commit();
    }

    public static int getClickCount(Context activity) {
        SharedPreferences pref = activity.getSharedPreferences("ClickCount", 0);
        int token = pref.getInt("ClickCount", 0);
        return token;
    }

    public static void setClickCount(Context activity, int flag) {
        SharedPreferences pref = activity.getSharedPreferences("ClickCount", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ClickCount", flag);
        editor.commit();
    }


    public static String getAppShareMessage(Context activity) {
        SharedPreferences pref = activity.getSharedPreferences("AppShareMessage", 0);
        String token = pref.getString("AppShareMessage", "");
        return token;
    }

    public static void setAppShareMessage(Context activity, String flag) {
        SharedPreferences pref = activity.getSharedPreferences("AppShareMessage", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("AppShareMessage", flag);
        editor.commit();
    }

    public static String getAppMessage(Context activity) {
        SharedPreferences pref = activity.getSharedPreferences("AppMessage", 0);
        String token = pref.getString("AppMessage", "");
        return token;
    }

    public static void setAppMessage(Context activity, String flag) {
        SharedPreferences pref = activity.getSharedPreferences("AppMessage", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("AppMessage", flag);
        editor.commit();
    }


    public static int getAppShareCount(Context activity) {
        SharedPreferences pref = activity.getSharedPreferences("AppShareCount", 0);
        int token = pref.getInt("AppShareCount", 0);
        return token;
    }

    public static void setAppShareCount(Context activity, int flag) {
        SharedPreferences pref = activity.getSharedPreferences("AppShareCount", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("AppShareCount", flag);
        editor.commit();
    }

    public static CategoryModel getNotificationData(Context activity) {
        try {
            JSONObject jSONObject = new JSONObject(PreferenceManager.getDefaultSharedPreferences(activity).getString("NDATA", ""));
            String image = jSONObject.getString("image");
            String title = jSONObject.getString("title");
            String message = jSONObject.getString("message");
            String videoId = jSONObject.getString("videoId");
            String redirectScreen = jSONObject.getString("redirectScreen");
            String notificationID = jSONObject.getString("notificationID");
            notificationModel = new CategoryModel();
            if (image != null) {
                notificationModel.setImage(image);
            } else {
                notificationModel.setImage("");
            }
            if (title != null) {
                notificationModel.setTitle(title);
            } else {
                notificationModel.setTitle("");
            }
            if (message != null) {
                notificationModel.setMessage(message);
            } else {
                notificationModel.setMessage("");
            }
            if (videoId != null) {
                notificationModel.setVideoId(videoId);
            } else {
                notificationModel.setVideoId("");
            }
            if (redirectScreen != null) {
                notificationModel.setRedirectScreen(redirectScreen);
            } else {
                notificationModel.setRedirectScreen("");
            }
            if (notificationID != null) {
                notificationModel.setNotificationID(notificationID);
            } else {
                notificationModel.setNotificationID("");
            }
            return notificationModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void Notify(final Activity activity, String title, String message) {
        if (activity != null) {
            final Dialog dialog1 = new Dialog(activity);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog1.setContentView(R.layout.dialogue_notify);
            dialog1.setCancelable(false);
            Button btnOk = (Button) dialog1.findViewById(R.id.btnSubmit);
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
                }
            });
            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int hight = display.getHeight();
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

    public static void NotifyFinish(final Activity activity, String title, String message) {
        if (activity != null) {
            final Dialog dialog1 = new Dialog(activity);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog1.setContentView(R.layout.dialogue_notify);
            dialog1.setCancelable(false);
            Button btnOk = (Button) dialog1.findViewById(R.id.btnSubmit);
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
                        activity.finish();
                    }
                }
            });
            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int hight = display.getHeight();
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


    public static void shareImageData(Activity activity) {
        Intent share;
        if (getAppShareImage(activity).trim().length() > 0) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/mahabharat/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String[] str = getAppShareImage(activity).trim().split("/");
            File file = new File(dir, str[str.length - 1] + ".png");
            if (file.exists()) {
                try {
                    Uri uri = Uri.fromFile(file);
                    share = new Intent(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_SUBJECT, Global_App.APPNAME);
                    share.putExtra(Intent.EXTRA_TEXT, getAppShareMessage(activity));
                    if (Build.VERSION.SDK_INT >= 24) {
                        Uri contentUri = getUriForFile(activity, activity.getPackageName(), file);
                        share.putExtra(Intent.EXTRA_STREAM, contentUri);
                    } else {
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                    }
                    share.setType("image/*");
                    activity.startActivity(Intent.createChooser(share, "Share"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Utility.checkInternetConnection(activity)) {
                new DownloadImageShareAsync(activity, file, getAppShareImage(activity)).execute(new String[0]);
            } else {
                Utility.Notify(activity, Global_App.APPNAME, "");
            }
        } else {
            try {
                share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_SUBJECT, "");
                share.putExtra(Intent.EXTRA_TEXT, getAppShareMessage(activity));
                share.setType("text/plain");
                activity.startActivity(Intent.createChooser(share, "Share"));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void setEnableDisablebtn(final Activity activity, final View view) {
        view.setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.setEnabled(true);
                    }
                });
            }
        }, 1500);
    }

    public static boolean isAppInstalled(Activity activity, String packageName) {
        if (activity.getPackageManager().getLaunchIntentForPackage(packageName) != null) {
            return true;
        }
        return false;
    }

    public static void setVideoId(Context activity, String regId) {
        SharedPreferences pref = activity.getSharedPreferences("VideoId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("VideoId", regId);
        editor.commit();
    }

    public static String getVideoId(Context activity) {
        SharedPreferences pref = activity.getSharedPreferences("VideoId", Context.MODE_PRIVATE);
        String regId = pref.getString("VideoId", "");
        return regId;
    }


    public static void setshowadd(Context activity, String flag) {
        SharedPreferences pref = activity.getSharedPreferences("showadd", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("showadd", flag);
        editor.commit();
    }


    public static String getshowadd(Context activity) {
        SharedPreferences pref = activity.getSharedPreferences("showadd", 0);
        String showadd = pref.getString("showadd", "");
        return showadd;
    }

    public static void adclick(final Activity activity, String ad_id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String device_id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        Call<ResponseModel> call = apiService.Insertadclick("ad_click", ad_id, device_id);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }
        });
    }

    public static void advertisement(Activity activity, ResponseModel responseModel) {
        if (responseModel != null) {
            ArrayList<CategoryModel> list = new ArrayList<>();
            list.addAll(responseModel.getView_ad());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getAd_type().equals("Full Page")) {
                    fulladvertise(activity, list, i);
                } else if (list.get(i).getAd_type().equals("Native")) {
                    nativead(activity, responseModel, i);
                } else if (list.get(i).getAd_type().equals("Banner")) {
                    bannerad(activity, responseModel, i);
                }
            }
        }
    }

    public static void nativead(final Activity activity, ResponseModel responseModel, int position) {
        //LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.nativead);
        //linearLayout.removeAllViews();
        ArrayList<CategoryModel> list = new ArrayList<>();
        list.addAll(responseModel.getView_ad());
        View view = LayoutInflater.from(activity).inflate(R.layout.row_native_advertise, null);

        ImageView imglogo = (ImageView) view.findViewById(R.id.imglogo);
        ImageView adimage = (ImageView) view.findViewById(R.id.adimage);
        TextView texttitle = (TextView) view.findViewById(R.id.texttitle);
        TextView textdescription = (TextView) view.findViewById(R.id.textdescription);
        Button btninstall = (Button) view.findViewById(R.id.btninstall);
        final CategoryModel model = list.get(position);

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
        if (model.getApplication_name() != null) {
            texttitle.setText(model.getApplication_name());
        }

        if (model.getDescription() != null) {
            textdescription.setText(model.getDescription());
        }
        adimage.setVisibility(View.VISIBLE);
        Picasso.with(activity)
                .load(model.getSource_path())
                .into(adimage);

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
        // linearLayout.addView(view);
    }

    public static void bannerad(final Activity activity, ResponseModel responseModel, int position) {
        db = new DataBaseClass(activity);
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.bannerad);
        linearLayout.removeAllViews();
        ArrayList<CategoryModel> list = new ArrayList<>();
        list.addAll(responseModel.getView_ad());
        View view = LayoutInflater.from(activity).inflate(R.layout.row_banner_advertisement, null);
        ImageView imglogo = (ImageView) view.findViewById(R.id.imglogo);
        ImageView imgclose = (ImageView) view.findViewById(R.id.imgclose);
        TextView texttitle = (TextView) view.findViewById(R.id.texttitle);
        Button btninstall = (Button) view.findViewById(R.id.btninstall);
        final RelativeLayout relativebanner = (RelativeLayout) view.findViewById(R.id.relativebanner);
        final CategoryModel model = list.get(position);
        relativebanner.setVisibility(View.VISIBLE);
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String todaydate = df.format(c);
                SharedPreferences date = activity.getSharedPreferences("todaydate", activity.MODE_PRIVATE);
                SharedPreferences.Editor dateeditor = date.edit();
                dateeditor.putString("date", todaydate);
                dateeditor.commit();
                String storedate = date.getString("date", null);
                if (!storedate.equals(todaydate)) {
                    db.deletecloseadsData();
                    dateeditor.putString("date", todaydate);
                    dateeditor.commit();
                    db.insertcloseadsData(model.getAd_id(), model.getAd_type());
                    relativebanner.setVisibility(View.GONE);
                } else {
                    db.insertcloseadsData(model.getAd_id(), model.getAd_type());
                    relativebanner.setVisibility(View.GONE);
                }
            }
        });
        if (model.getApplication_name() != null) {
            texttitle.setText(model.getApplication_name());
        }
        Picasso.with(activity)
                .load(model.getApplication_logo())
                .into(imglogo);
        btninstall.setText(model.getButton_title());
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
        final Cursor cursor = db.iscloseadsadded(model.getAd_id());
        if (cursor.getCount() != 0) {
            relativebanner.setVisibility(View.GONE);
        }
        linearLayout.addView(view);
    }

    public static void fulladvertise(final Activity activity, ArrayList<CategoryModel> list, int position) {
        final Dialog dialog;
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.row_full_advertisement);
        ImageView imgclose = (ImageView) dialog.findViewById(R.id.imgclose);
        ImageView imgad = (ImageView) dialog.findViewById(R.id.imgad);
        RatingBar imgratting = (RatingBar) dialog.findViewById(R.id.imgratting);
        ImageView imgmain = (ImageView) dialog.findViewById(R.id.imgmain);
        ImageView imgdrawable = (ImageView) dialog.findViewById(R.id.imgdrawable);
        ImageView imginfo = (ImageView) dialog.findViewById(R.id.imginfo);
        LinearLayout adbtninstall = (LinearLayout) dialog.findViewById(R.id.adbtninstall);
        TextView adtextsubtitle = (TextView) dialog.findViewById(R.id.adtextsubtitle);
        TextView adtextsubdescription = (TextView) dialog.findViewById(R.id.adtextsubdescription);
        TextView textbtn = (TextView) dialog.findViewById(R.id.textbtn);
        TextView adtexttitle = (TextView) dialog.findViewById(R.id.adtexttitle);
        RelativeLayout relativefull = (RelativeLayout) dialog.findViewById(R.id.relativefull);

        final TextView textpoweredby = (TextView) dialog.findViewById(R.id.textpoweredby);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final CategoryModel model = list.get(position);
        adtextsubdescription.setMovementMethod(new ScrollingMovementMethod());
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        if (model.getApplication_name() != null) {
            adtexttitle.setText(model.getApplication_name());
        }
        if (model.getRate() != null) {
            imgratting.setRating(Float.parseFloat(model.getRate()));
        }
        if (model.getApplication_caption() != null) {
            adtextsubtitle.setText(model.getApplication_caption());
        }
        if (model.getDescription() != null) {
            adtextsubdescription.setText(model.getDescription());
        }
        imginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getPowered_by() != null) {
                    textpoweredby.setVisibility(View.VISIBLE);
                    textpoweredby.setText("Powered by : " + model.getPowered_by());
                }
            }
        });

        imgmain.setVisibility(View.VISIBLE);
        Picasso.with(activity)
                .load(model.getSource_path())
                .into(imgmain);

        textbtn.setText(model.getButton_title());
        Picasso.with(activity)
                .load(model.getButton_icon_path())
                .into(imgdrawable);

        Picasso.with(activity)
                .load(model.getApplication_logo())
                .into(imgad);

        adbtninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adclick(activity, model.getAd_id());
                String market_uri = model.getRedirect_url();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(market_uri));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                        .FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        Display display = activity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int hight = display.getHeight();
        WindowManager.LayoutParams lp;
        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setCancelable(false);
        dialog.getWindow().setAttributes(lp);
        if (!activity.isFinishing()) {
            dialog.show();
        }
    }
}
