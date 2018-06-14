package com.jorjoto.mahabharat.async;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;

import com.jorjoto.mahabharat.MyFirebaseMessagingService;
import com.jorjoto.mahabharat.R;
import com.jorjoto.mahabharat.activity.SplashScreenActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class GeneratePictureStyleNotificationAsync extends AsyncTask<String, Void, Bitmap> {
    private final String bundle;
    private Context context;
    private String title, message, imageUrl;
    private Notification notif = null;

    public GeneratePictureStyleNotificationAsync(Context context, String title, String message, String imageUrl, String bundle) {
        super();
        this.context = context;
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
        this.bundle = bundle;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        InputStream in;
        try {
            URL url = new URL(this.imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setConnectTimeout(300000);
            connection.connect();
            in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        try {
            Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_applogo_notification);
            int icon = R.drawable.icon_applogo_notification;
            long when = System.currentTimeMillis();
            Intent notificationIntent = new Intent(context, SplashScreenActivity.class);
            notificationIntent.putExtra("bundle", bundle);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            int notificationid = 0;
            if (new JSONObject(bundle).getString("notificationID") != null && new JSONObject(bundle).getString("notificationID").trim().length() > 0) {
                notificationid = Integer.parseInt(new JSONObject(bundle).getString("notificationID"));
            } else {
                Random r = new Random();
                notificationid = r.nextInt(500);
            }
            PendingIntent   intent = PendingIntent.getActivity(context, notificationid, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("channel_01", title, NotificationManager.IMPORTANCE_HIGH);
                channel.setSound(null, null);
                notificationManager.createNotificationChannel(channel);
                notif = new Notification.Builder(context)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(icon)
                        .setContentIntent(intent)
                        .setLargeIcon(icon1)
                        .setAutoCancel(true)
                        .setChannelId("channel_01")
                        .setStyle(new Notification.BigPictureStyle().bigPicture(result).setSummaryText(message))
                        .build();
            } else {
                notif = new Notification.Builder(context)
                        .setContentTitle(title)
                        .setSmallIcon(icon)
                        .setContentIntent(intent)
                        .setLargeIcon(icon1)
                        .setAutoCancel(true)
                        .setStyle(new Notification.BigPictureStyle().bigPicture(result).setSummaryText(message))
                        .build();
            }

            notif.defaults |= Notification.DEFAULT_VIBRATE;
            notif.defaults |= Notification.DEFAULT_SOUND;
            notificationManager.notify(notificationid, notif);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
