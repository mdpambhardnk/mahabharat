package com.jorjoto.mahabharat.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jorjoto.mahabharat.R;
import com.jorjoto.mahabharat.model.CategoryModel;

import org.json.JSONObject;

public class Utility {
    private static CategoryModel notificationModel;

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
            }if (notificationID != null) {
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


}
