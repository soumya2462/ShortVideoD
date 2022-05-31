package com.example.shortvideod.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.daasuu.gpuv.egl.filter.GlFilter;
import com.daasuu.gpuv.player.GPUPlayerView;
import com.example.shortvideod.R;
import com.example.shortvideod.adapter.VideoEffectListAdapter;
import com.example.shortvideod.databinding.ActivityFilterBinding;
import com.example.shortvideod.effect.FilterType;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.File;
import java.util.List;


public class FilterActivity extends BaseActivity {

    public static final String EXTRA_SONG = "song";
    public static final String EXTRA_VIDEO = "video";
    static final String TAG = "FilterActivity";

    FilterActivityViewModel mModel;
    SimpleExoPlayer mPlayer;
    GPUPlayerView mPlayerView;
    String mSong;
    String mVideo;
    ActivityFilterBinding binding;
    GlFilter filter = new GlFilter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        mModel = new ViewModelProvider(this).get(FilterActivityViewModel.class);
        mSong = getIntent().getStringExtra(EXTRA_SONG);
        mVideo = getIntent().getStringExtra(EXTRA_VIDEO);
        Log.d(TAG, "onCreate:songid " + mSong);
        Log.d(TAG, "onCreate:video  " + mVideo);

        setupViews();

    }


    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
        mPlayer.setPlayWhenReady(false);
        mPlayer.stop(true);
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayer = new SimpleExoPlayer.Builder(this).build();
        mPlayer = ExoPlayerFactory.newSimpleInstance(this);
        DefaultDataSourceFactory factory =
                new DefaultDataSourceFactory(this, getString(R.string.app_name));
        ProgressiveMediaSource source = new ProgressiveMediaSource.Factory(factory)
                .createMediaSource(Uri.fromFile(new File(mVideo)));
        mPlayer.setPlayWhenReady(true);
        mPlayer.prepare(source);
        mPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        mPlayerView = findViewById(R.id.player);
        mPlayerView.setSimpleExoPlayer(mPlayer);
        mPlayerView.onResume();
    }



    private void closeFinally(File clip) {
        Log.d(TAG, "Filter was successfully applied to " + clip);

    }


    public void setupViews() {
        final List<FilterType> filterTypes = FilterType.createFilterList();

        VideoEffectListAdapter adapter = new VideoEffectListAdapter(this, filterTypes, filterType -> {

            filter = null;
            filter = FilterType.createGlFilter(filterType, this);


            mModel.filter = filterType;
            binding.player.setGlFilter(filter);

        });

        binding.filters.setItemAnimator(new DefaultItemAnimator());
        binding.filters.setAdapter(adapter);
    }


    public static class FilterActivityViewModel extends ViewModel {
        FilterType filter = FilterType.DEFAULT;
    }

}
