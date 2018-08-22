package com.jorjoto.mahabharat.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.jorjoto.mahabharat.MyApplication;
import com.jorjoto.mahabharat.R;
import com.jorjoto.mahabharat.adapter.SuggestAppListAdapter;
import com.jorjoto.mahabharat.adapter.SuggestVideoListAdapter;
import com.jorjoto.mahabharat.async.GetAdAsync;
import com.jorjoto.mahabharat.async.GetVideoDetailsAsync;
import com.jorjoto.mahabharat.model.CategoryModel;
import com.jorjoto.mahabharat.model.RequestModel;
import com.jorjoto.mahabharat.model.ResponseModel;
import com.jorjoto.mahabharat.util.Global_App;
import com.jorjoto.mahabharat.util.Utility;
import com.sa90.materialarcmenu.ArcMenu;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class YouTubeVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    Toast toast = null;
    private static YouTubePlayerView youTubeView;
    private static final int RECOVERY_REQUEST = 1;
    private static boolean fullScreen = false;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    public static YouTubePlayer youTubePlayer;
    private static LinearLayout loutMian;
    private static ProgressBar prpobr;
    private static TextView txtMessage;
    private String videoId = "",videotitle  = " ";
    private RecyclerView rcSuggestList;
    public static TextView txtTitle;
    public static TextView txtDescription;
    public static String currentVideo = "";
    static ResponseModel responseModel;
    SuggestVideoListAdapter suggestVideoListAdapter;
    public static final int REQUEST_WRITE_PERMISSION = 73;
    private LinearLayout loutApps;
    private RecyclerView rcApps;
    SuggestAppListAdapter suggestAppListAdapter;
    private ArcMenu arcMenu;
    ArrayList<CategoryModel> appList;
    private RequestModel requestModel = new RequestModel();
    ImageView imgback;
    public static  TextView tooltext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        videoId = intent.getStringExtra("videoId");
        videotitle = intent.getStringExtra("videotitle");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = YouTubeVideoActivity.this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                requestModel.setAd_type("1");
                new GetAdAsync(YouTubeVideoActivity.this, requestModel);
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 15 * 1000);

        requestModel.setAd_type("2");
        new GetAdAsync(YouTubeVideoActivity.this, requestModel);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Global_App.YOUTUBE_API_KEY, this);
        prpobr = (ProgressBar) findViewById(R.id.prpobr);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        loutMian = (LinearLayout) findViewById(R.id.loutMian);
        loutApps = (LinearLayout) findViewById(R.id.loutApps);
        rcSuggestList = (RecyclerView) findViewById(R.id.rcSuggestList);
        rcApps = (RecyclerView) findViewById(R.id.rcApps);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        tooltext = (TextView) findViewById(R.id.tooltext);
        imgback = (ImageView) findViewById(R.id.imgback);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tooltext.setText(videotitle);

        arcMenu = (ArcMenu) findViewById(R.id.arcMenu);
        arcMenu.setRadius(getResources().getDimension(R.dimen.radius));
        arcMenu.setVisibility(View.GONE);
        findViewById(R.id.imgShare).setOnClickListener(imgShare);
        findViewById(R.id.imgVideo).setOnClickListener(imgVideo);
        findViewById(R.id.imgContactUs).setOnClickListener(imgContactUs);
        MyApplication.getInstance().trackScreenView("YouTubeVideoActivity");

    }

    private View.OnClickListener imgShare = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            share();
            arcMenu.toggleMenu();

        }
    };

    private View.OnClickListener imgVideo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            arcMenu.toggleMenu();
            MainActivity.getVideoList(YouTubeVideoActivity.this, "1");
            finish();
        }
    };

    private View.OnClickListener imgContactUs = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Utility.checkInternetConnection(YouTubeVideoActivity.this)) {
                Utility.setEnableDisablebtn(YouTubeVideoActivity.this, v);
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(YouTubeVideoActivity.this.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String DEVICE_VERSION = android.os.Build.VERSION.RELEASE;
                String DEVICE_NAME = android.os.Build.MODEL;
                String to = Global_App.FEEDBACK_EMAIL;
                String subject = "Feedback";
                String message = "\n\n\n-------------------------------------------------------\n " + Global_App.APPNAME + " V " + pInfo.versionName + " on " + DEVICE_NAME + " running android " + DEVICE_VERSION;
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setPackage("com.google.android.gm");
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Send Email Using: "));
            }
            arcMenu.toggleMenu();

        }
    };


    public void setVideoData(Activity activity, ResponseModel responseModeldata) {
        if (responseModeldata != null) {
            prpobr.setVisibility(View.GONE);
            txtMessage.setVisibility(View.GONE);
            loutMian.setVisibility(View.VISIBLE);
            arcMenu.setVisibility(View.VISIBLE);
            responseModel = responseModeldata;
            youTubePlayer.cueVideo(responseModel.getVideoData().getVideoLink());
            currentVideo = responseModel.getVideoData().getVideoLink();
            txtTitle.setText(responseModel.getVideoData().getVideoTitle());
            txtDescription.setText(responseModel.getVideoData().getVideoDescription());
            rcSuggestList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            suggestVideoListAdapter = new SuggestVideoListAdapter(activity, responseModel.getRelatedVideos());
            rcSuggestList.setAdapter(suggestVideoListAdapter);
            // fab.setVisibility(View.VISIBLE);


            if (responseModel.getSuggestedApps() != null && responseModel.getSuggestedApps().size() > 0) {
                appList = new ArrayList<CategoryModel>();
                for (int i = 0; i < responseModel.getSuggestedApps().size(); i++) {
                    if (!Utility.isAppInstalled(activity, responseModel.getSuggestedApps().get(i).getAppPackage())) {
                        appList.add(responseModel.getSuggestedApps().get(i));
                    }
                }
                if (appList != null && appList.size() > 0) {
                    loutApps.setVisibility(View.VISIBLE);
                    rcApps.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    suggestAppListAdapter = new SuggestAppListAdapter(activity, appList);
                    rcApps.setAdapter(suggestAppListAdapter);
                } else {
                    loutApps.setVisibility(View.GONE);
                }

            } else {
                loutApps.setVisibility(View.GONE);
            }

        } else {
            prpobr.setVisibility(View.GONE);
            loutMian.setVisibility(View.GONE);
            txtMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer = player;
            // player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
//            YouTubePlayer.PlayerStyle style = YouTubePlayer.PlayerStyle.MINIMAL;
//            player.setPlayerStyle(style   );
            youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {

                @Override
                public void onFullscreen(boolean _isFullScreen) {
                    fullScreen = _isFullScreen;
                }
            });
            getVideoList(YouTubeVideoActivity.this, videoId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestModel.setAd_type("2");
        new GetAdAsync(YouTubeVideoActivity.this, requestModel);
    }

    public static void getVideoList(Activity activity, String id) {
        RequestModel requestModel = new RequestModel();
        requestModel.setVideo_id(id);
        new GetVideoDetailsAsync(activity, requestModel);
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Global_App.YOUTUBE_API_KEY, this);
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            showMessage("Playing");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            showMessage("Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
        }
    }


    @Override
    public void onBackPressed() {
        if (fullScreen) {
            youTubePlayer.setFullscreen(false);

        } else {
            super.onBackPressed();
        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == 0 && grantResults[1] == 0) {
            Utility.shareImageData(YouTubeVideoActivity.this);
            Utility.setClickCount(YouTubeVideoActivity.this, 0);
        } else {
            Utility.Notify(YouTubeVideoActivity.this, Global_App.APPNAME, "permission denied.");
        }

    }

    public void share() {
        if (Utility.getAppShareImage(YouTubeVideoActivity.this).trim().length() > 0) {
            if (Build.VERSION.SDK_INT >= 23) {
                int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int hasWriteContactsPermission1 = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED || hasWriteContactsPermission1 != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 73);
                } else {
                    Utility.shareImageData(YouTubeVideoActivity.this);
                    Utility.setClickCount(YouTubeVideoActivity.this, 0);
                }
            } else {
                Utility.shareImageData(YouTubeVideoActivity.this);
                Utility.setClickCount(YouTubeVideoActivity.this, 0);
            }
        } else {
            Utility.shareImageData(YouTubeVideoActivity.this);
            Utility.setClickCount(YouTubeVideoActivity.this, 0);
        }
    }

    public void NotifyMessage(final Activity activity, String title) {
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
            txtMessage.setText(Utility.getAppMessage(activity));
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog1 != null) {
                        if (!activity.isFinishing()) {
                            dialog1.dismiss();
                        }
                    }
                    share();

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
