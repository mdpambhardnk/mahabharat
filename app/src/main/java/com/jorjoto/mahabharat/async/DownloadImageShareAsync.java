package com.jorjoto.mahabharat.async;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import com.jorjoto.mahabharat.util.Global_App;
import com.jorjoto.mahabharat.util.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.v4.content.FileProvider;

public class DownloadImageShareAsync extends AsyncTask<String, Void, Bitmap> {
    private final Activity activity;
    private final File file;
    private final String imageUrl;
    private ProgressDialog progressDialog;

    public DownloadImageShareAsync(Activity activity, File file, String shareImage) {
        this.activity = activity;
        this.imageUrl = shareImage;
        this.file = file;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog = new ProgressDialog(activity);
        this.progressDialog.setTitle(Global_App.APPNAME);
        this.progressDialog.setMessage(Global_App.LABEL_LOADING);
        this.progressDialog.setCancelable(false);
        if (!activity.isFinishing()) {
            this.progressDialog.show();
        }
    }

    protected Bitmap doInBackground(String... params) {
        try {

            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(file);
            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        progressDialog.dismiss();
        try {
            Intent share = new Intent(Intent.ACTION_SEND);
            Uri uri = Uri.fromFile(file);
            share.putExtra(Intent.EXTRA_SUBJECT, Global_App.APPNAME);
            share.setType("image/*");
           // share.putExtra(Intent.EXTRA_STREAM, uri);
            if (Build.VERSION.SDK_INT >= 24) {
                share.setDataAndType(FileProvider.getUriForFile(activity.getApplicationContext(), "com.jorjoto.mahabharat", file), "image/jpg");
            } else {
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
            }
            share.putExtra(Intent.EXTRA_SUBJECT, "");
            share.putExtra(Intent.EXTRA_TEXT, Utility.getAppShareMessage(activity));

            activity.startActivity(Intent.createChooser(share, "Share"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
