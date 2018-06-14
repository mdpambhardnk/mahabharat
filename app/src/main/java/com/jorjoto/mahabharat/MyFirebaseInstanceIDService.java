package com.jorjoto.mahabharat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jorjoto.mahabharat.activity.SplashScreenActivity;
import com.jorjoto.mahabharat.util.Global_App;
import com.jorjoto.mahabharat.util.Utility;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        if (Utility.getFCMRegId(getBaseContext()).trim().length() > 0) {
            Global_App.refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if (Global_App.refreshedToken != null) {
                Utility.setFCMRegId(getBaseContext(), Global_App.refreshedToken);
            }
            SplashScreenActivity.getFCMData(getBaseContext());
        } else {
            Global_App.refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if (Global_App.refreshedToken == null) {
                Global_App.refreshedToken = "";
            }
            Utility.setFCMRegId(getBaseContext(), Global_App.refreshedToken);
            SplashScreenActivity.getFCMData(getBaseContext());
        }
    }
}
