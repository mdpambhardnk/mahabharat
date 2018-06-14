package com.jorjoto.mahabharat.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
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
import com.jorjoto.mahabharat.R;
import com.jorjoto.mahabharat.adapter.SuggestVideoListAdapter;
import com.jorjoto.mahabharat.async.GetVideoDetailsAsync;
import com.jorjoto.mahabharat.async.GetVideoListAsync;
import com.jorjoto.mahabharat.model.CategoryModel;
import com.jorjoto.mahabharat.model.RequestModel;
import com.jorjoto.mahabharat.model.ResponseModel;
import com.jorjoto.mahabharat.util.AnimatorUtils;
import com.jorjoto.mahabharat.util.Global_App;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;

public class YouTubeVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {
    Toast toast = null;
    private static YouTubePlayerView youTubeView;
    private static final int RECOVERY_REQUEST = 1;
    private static boolean fullScreen = false;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    public static YouTubePlayer youTubePlayer;
    private static ImageView fab;
    private static ArcLayout arcLayout;
    private static View menuLayout;
    private static LinearLayout loutMian;
    private static ProgressBar prpobr;
    private static TextView txtMessage;
    private String videoId = "";
    private RecyclerView rcSuggestList;
    private TextView txtTitle;
    private TextView txtDescription;
    public static String currentVideo = "";
    static ResponseModel responseModel;
    SuggestVideoListAdapter suggestVideoListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        videoId = intent.getStringExtra("videoId");
        Log.v("AAAAAA", "" + videoId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = YouTubeVideoActivity.this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Global_App.YOUTUBE_API_KEY, this);
        prpobr = (ProgressBar) findViewById(R.id.prpobr);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        loutMian = (LinearLayout) findViewById(R.id.loutMian);
        rcSuggestList = (RecyclerView) findViewById(R.id.rcSuggestList);
        fab = (ImageView) findViewById(R.id.fab);
        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        menuLayout = findViewById(R.id.menu_layout);
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
        }
        fab.setOnClickListener(this);


    }

    public void setVideoData(Activity activity, ResponseModel responseModeldata) {
        if (responseModeldata != null) {
            prpobr.setVisibility(View.GONE);
            txtMessage.setVisibility(View.GONE);
            loutMian.setVisibility(View.VISIBLE);
            responseModel = responseModeldata;
            youTubePlayer.cueVideo(responseModel.getVideoData().getVideoLink());
            currentVideo = responseModel.getVideoData().getVideoLink();
            txtTitle.setText(responseModel.getVideoData().getVideoTitle());
            txtDescription.setText(responseModel.getVideoData().getVideoDescription());
            rcSuggestList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            suggestVideoListAdapter = new SuggestVideoListAdapter(activity, responseModel.getRelatedVideos());
            rcSuggestList.setAdapter(suggestVideoListAdapter);
            fab.setVisibility(View.VISIBLE);
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
            player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            onFabClick(v);
            return;
        }

        if (v instanceof Button) {
            showToast((Button) v);
        }
    }

    private void showToast(Button btn) {
        if (toast != null) {
            toast.cancel();
        }

        String text = "Clicked: " + btn.getText();
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();

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
        Log.v("AAAAAAA", "back " + fullScreen);
        if (fullScreen) {
            youTubePlayer.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }


    private void onFabClick(View v) {
        if (v.isSelected()) {
            hideMenu();
        } else {
            showMenu();
        }
        v.setSelected(!v.isSelected());
    }
    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    @SuppressWarnings("NewApi")
    private void hideMenu() {

        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }

    private Animator createShowItemAnimator(View item) {

        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }

}
