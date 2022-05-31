package com.example.shortvideod.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.example.shortvideod.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.source.ClippingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;


public class TrimmerActivity extends BaseActivity implements AnalyticsListener {

    public static final String EXTRA_AUDIO = "audio";
    public static final String EXTRA_SONG = "song";
    public static final String EXTRA_VIDEO = "video";
    static final String TAG = "TrimmerActivity";
    final Handler mHandler = new Handler(Looper.getMainLooper());
    String mAudio;
    int mDuration = 0;
    SimpleExoPlayer mPlayer;
    int mTrimEndTime = 0;
    int mTrimStartTime = 0;
    String mSong;
    String mVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimmer);

        ImageView done = findViewById(R.id.check);
        done.setImageResource(R.drawable.ic_baseline_check_24);

        mAudio = getIntent().getStringExtra(EXTRA_AUDIO);
        mSong = getIntent().getStringExtra(EXTRA_SONG);
        mVideo = getIntent().getStringExtra(EXTRA_VIDEO);
        mPlayer = new SimpleExoPlayer.Builder(this).build();
        mPlayer.addAnalyticsListener(this);
        PlayerView player = findViewById(R.id.player);
        player.setPlayer(mPlayer);
        mTrimStartTime = 0;
        Log.v(TAG, "Duration of video is " + mDuration + "ms.");
        startPlayer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.setPlayWhenReady(false);
        if (mPlayer.isPlaying()) {
            mPlayer.stop(true);
        }

        mPlayer.release();
        mPlayer = null;
        File video = new File(mVideo);
        if (!video.delete()) {
            Log.w(TAG, "Could not delete input video: " + video);
        }
    }



    private void startPlayer() {
        DefaultDataSourceFactory factory =
                new DefaultDataSourceFactory(this, getString(R.string.app_name));
        MediaSource source = new ProgressiveMediaSource.Factory(factory)
                .createMediaSource(Uri.fromFile(new File(mVideo)));
        source = new ClippingMediaSource(
                source,
                TimeUnit.MILLISECONDS.toMicros(mTrimStartTime),
                TimeUnit.MILLISECONDS.toMicros(mTrimEndTime)
        );
        mPlayer.setPlayWhenReady(true);
        mPlayer.prepare(source);
    }


}
