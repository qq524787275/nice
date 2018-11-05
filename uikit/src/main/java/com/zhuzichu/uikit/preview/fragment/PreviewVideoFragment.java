package com.zhuzichu.uikit.preview.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.observer.action.ActionAttachmentProgress;
import com.zhuzichu.uikit.observer.action.ActionMessageStatus;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wb.zhuzichu18 on 2018/11/5.
 */
public class PreviewVideoFragment extends PreViewItemFragment {
    private static final String TAG = "PreviewVideoFragment";
    private PlayerView mPlayer;
    private SimpleExoPlayer simpleExoPlayer;

    public static PreViewItemFragment newInstance(IMMessage message) {

        Bundle args = new Bundle();
        args.putSerializable(Extra.EXTRA_MESSGAE, message);
        PreViewItemFragment fragment = new PreviewVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initView();
        initExo();
        initObserver();
        img.setSingleTapListener(() -> {
            mPlayer.setVisibility(View.VISIBLE);
//            img.setVisibility(View.GONE);
        });
    }


    private void initObserver() {
        Disposable dispProgress = RxBus.getIntance().toObservable(ActionAttachmentProgress.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(action -> action.data)
                .filter(progress -> progress.getUuid().equals(message.getUuid()))
                .subscribe(progress -> {
                    Log.i(TAG, "initObserver: ");
                    Log.i(TAG, "initObserver ------Total: " + progress.getTotal() + ";-----Transferred:" + progress.getTransferred());
                });

        Disposable dispMessageStatus = RxBus.getIntance().toObservable(ActionMessageStatus.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(action -> action.data)
                .filter(msg -> msg.getUuid().equals(message.getUuid()))
                .subscribe(msg -> {
                    message = msg;
                    AttachStatusEnum status = message.getAttachStatus();
                    switch (status) {
                        case def:
                            Log.i(TAG, "initObserver: def");
                            break;
                        case transferring:
                            Log.i(TAG, "initObserver: transferring");
                            break;
                        case transferred:
                            Log.i(TAG, "initObserver: transferred");
                            break;
                        case fail:
                            Log.i(TAG, "initObserver: fail");
                            break;
                    }
                });

        RxBus.getIntance().addSubscription(this.getClass().getSimpleName() + message.getUuid(), dispMessageStatus, dispProgress);
    }

    private void initExo() {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        mPlayer.setPlayer(simpleExoPlayer);

        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.i(TAG, "onPlayerStateChanged-----playWhenReady:" + playWhenReady + ",playbackState:" + playbackState);
                switch (playbackState) {
                    case Player.STATE_ENDED:
                        img.setVisibility(View.VISIBLE);
                        mPlayer.setVisibility(View.GONE);
                        break;
                    case Player.STATE_READY:
                        break;
                    case Player.STATE_BUFFERING:
                        break;
                    case Player.STATE_IDLE:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
                Log.i(TAG, "onPositionDiscontinuity: ");
            }

            @Override
            public void onPlayerError(ExoPlaybackException e) {
                Log.i(TAG, "onPlayerError: ");
            }

            @Override
            @SuppressWarnings("ReferenceEquality")
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.i(TAG, "onTracksChanged: ");
            }
        });
        VideoAttachment attachment = (VideoAttachment) message.getAttachment();
        String path = attachment.getPath();
        if (path != null) {
            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(new FileDataSourceFactory()).createMediaSource(Uri.fromFile(new File(path)));
            simpleExoPlayer.prepare(mediaSource);
            mPlayer.setVisibility(View.GONE);
        } else {
            NIMClient.getService(MsgService.class).downloadAttachment(message, false);
        }
    }

    public void initView() {
        img.setScaleEnabled(false);
        img.setDoubleTapEnabled(false);
        mPlayer = container.findViewById(R.id.player);
    }

    @Override
    public int getContainer() {
        return R.layout.layout_preview_video_control;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        if (simpleExoPlayer != null) {
            mPlayer.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            mPlayer.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        releasePlayer();
        RxBus.getIntance().unSubscribe(this.getClass().getSimpleName() + message.getUuid());
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        return super.onBackPressedSupport();
    }
}
