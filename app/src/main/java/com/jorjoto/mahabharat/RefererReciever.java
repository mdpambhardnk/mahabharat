package com.jorjoto.mahabharat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jorjoto.mahabharat.util.Utility;

/**
 * Created by Milan Pambhar on 19-12-2017.
 */

public class RefererReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String referral = extras.getString("videoId");
                if (referral != null) {
                    Utility.setVideoId(context, "");
                    if (!(referral.equals("utm_source=google-play&utm_medium=organic"))) {
                        Utility.setVideoId(context, referral);
                        Log.v("AAAAAA","VideoId "+referral);
                    }
                }
            }
        }
    }
}
