package com.example.shortvideod.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.example.shortvideod.R;
import com.example.shortvideod.databinding.FragmentHomeBinding;
import com.example.shortvideod.databinding.ItemReelsBinding;
import com.example.shortvideod.design.Reels;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class HomeFragment extends Fragment implements Player.EventListener {

    FragmentHomeBinding binding;

    NavController navController;
    int like;

    BottomSheetDialogFragment bottomSheetDialogFragment;
    Animation animation;
    Reels reels;
    SimpleExoPlayer player;
    ItemReelsBinding playerBinding;
    int lastPosition = 0;
    Animation rotateanimation;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        bottomSheetDialogFragment = new GiftBottomSheetFrgament();


        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        rotateanimation = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.slow_rotate);

        navController = Navigation.findNavController(requireActivity(), R.id.host);
        new PagerSnapHelper().attachToRecyclerView(binding.rvReels);


        binding.getSuggested.setOnClickListener(v ->
                navController.navigate(R.id.action_homeFragment_to_followingFragment));


        binding.wallet.setOnClickListener(v ->
                navController.navigate(R.id.action_homeFragment_to_walletFragment));

    }



    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        //
    }

    public void showWallet() {
        navController = Navigation.findNavController(requireActivity(), R.id.host);
        navController.navigate(R.id.action_homeActivityFragment_to_walletFragment);
    }

    @Override
    public void onResume() {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop();
            player.release();
        }
        super.onDestroy();
    }

}
