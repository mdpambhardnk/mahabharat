package com.jorjoto.mahabharat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.jorjoto.mahabharat.activity.MainActivity;
import com.jorjoto.mahabharat.activity.SplashScreenActivity;
import com.jorjoto.mahabharat.async.GeneratePictureStyleNotificationAsync;
import com.jorjoto.mahabharat.model.CategoryModel;
import com.jorjoto.mahabharat.util.Global_App;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private JSONObject jsonObject;
    private String image = "";
    private String message = "";
    private String title = "";
    private String contenttext = "";
    private int notificationid = 123;
    private Notification notification = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null)
            return;

        Log.v("AAAAAAAA", "getData().size() " + remoteMessage.getData().size());
        if (remoteMessage.getData().size() > 0) {
            Log.v("AAAAAA", "Message data payload: " + remoteMessage.getData());
            handleDataMessage(remoteMessage.getData());
        }
    }
    
    private void handleDataMessage(Map<String, String> intent) {
        String image = intent.get("image");
        String title = intent.get("title");
        String message = intent.get("message");
        String videoId = intent.get("videoId");
        String redirectScreen = intent.get("redirectScreen");
        String notificationID = intent.get("notificationID");
        try {
            jsonObject = new JSONObject();
            if (image != null) {
                jsonObject.put("image", image);
            } else {
                jsonObject.put("image", "");
            }
            if (title != null) {
                jsonObject.put("title", title);
            } else {
                jsonObject.put("title", "");
            }
            if (message != null) {
                jsonObject.put("message", message);
            } else {
                jsonObject.put("message", "");
            }
            if (videoId != null) {
                jsonObject.put("videoId", videoId);
            } else {
                jsonObject.put("videoId", "");
            }
            if (redirectScreen != null) {
                jsonObject.put("redirectScreen", redirectScreen);
            } else {
                jsonObject.put("redirectScreen", "");
            }
            if (notificationID != null) {
                jsonObject.put("notificationID", notificationID);
            } else {
                jsonObject.put("notificationID", "");
            }
            Log.v("AAAAA", "" + jsonObject.toString());
            generateNotification(getBaseContext(), jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void generateNotification(Context context, String data) {
        Intent notificationIntent = new Intent(context, SplashScreenActivity.class);
        notificationIntent.putExtra("bundle", data);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent intent = null;
        try {
            if (new JSONObject(data).getString("notificationID") != null && new JSONObject(data).getString("notificationID").trim().length() > 0) {
                notificationid = Integer.parseInt(new JSONObject(data).getString("notificationID"));
            } else {
                Random r = new Random();
                notificationid = r.nextInt(500);
            }
            intent = PendingIntent.getActivity(context, notificationid, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            image = new JSONObject(data).getString("image").trim();
            title = new JSONObject(data).getString("title").trim();
            message = new JSONObject(data).getString("message").trim();
            if (title.trim().length() == 0) {
                title = Global_App.APPNAME;
            }
            if (image.equalsIgnoreCase("0") || image.equalsIgnoreCase("")) {
                if (message.trim().length() > 40) {
                    contenttext = message.substring(0, 40);
                } else {
                    contenttext = message;
                }
                Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_applogo_notification);
                if (Build.VERSION.SDK_INT < 16) {
                    Notification n = new Notification.Builder(this)
                            .setContentTitle(title)
                            .setContentText(contenttext)
                            .setSmallIcon(R.drawable.icon_applogo_notification)
                            .setContentIntent(intent)
                            .setAutoCancel(true).getNotification();
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(notificationid, n);
                } else {
                    NotificationManager notificationmanager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("channel_01", "Playback Notification", NotificationManager.IMPORTANCE_HIGH);
                        channel.setSound(null, null);
                        notificationmanager.createNotificationChannel(channel);
                        notification = new Notification.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.icon_applogo_notification)
                                .setContentText(contenttext)
                                .setContentIntent(intent)
                                .setAutoCancel(true)
                                .setLargeIcon(icon1)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setChannelId("channel_01")
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setStyle(new Notification.BigTextStyle().bigText(message))
                                .setContentTitle(title).build();
                    } else {
                        notification = new Notification.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.icon_applogo_notification)
                                .setContentText(contenttext)
                                .setContentIntent(intent)
                                .setAutoCancel(true)
                                .setLargeIcon(icon1)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setStyle(new Notification.BigTextStyle().bigText(message))
                                .setContentTitle(title).build();
                    }
                    if (notificationmanager != null) {
                        notificationmanager.notify(notificationid, notification);
                    }
                }
            } else {
                new GeneratePictureStyleNotificationAsync(context, title, message, image, data).execute();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
