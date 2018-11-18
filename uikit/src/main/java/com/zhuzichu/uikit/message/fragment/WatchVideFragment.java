package com.zhuzichu.uikit.message.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.android.exoplayer2.util.Util;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.observer.action.ActionAttachmentProgress;
import com.zhuzichu.uikit.observer.action.ActionMessageStatus;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WatchVideFragment extends BaseFragment {

    public interface Extra {
        String EXTRA_MESSGAE = "extra_messgae";
    }

    private static final String TAG = "WatchVideFragment";
    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private IMMessage message;

    public static WatchVideFragment newInstance(IMMessage message) {

        Bundle args = new Bundle();
        args.putSerializable(Extra.EXTRA_MESSGAE, message);
        WatchVideFragment fragment = new WatchVideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_watch_video;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mPlayerView = rootView.findViewById(R.id.pv);
        message = (IMMessage) getArguments().getSerializable(Extra.EXTRA_MESSGAE);
        initPlayer();
        initObserver();
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

    private void initPlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        mPlayerView.setPlayer(mPlayer);

        mPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.i(TAG, "onPlayerStateChanged-----playWhenReady:" + playWhenReady + ",playbackState:" + playbackState);
                switch (playbackState) {
                    case Player.STATE_ENDED:
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
            mPlayer.prepare(mediaSource);
        } else {
            NIMClient.getService(MsgService.class).downloadAttachment(message, false);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPlayerView.hideController();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mPlayer != null) {
                mPlayerView.onResume();
            }
        } else {
            if (mPlayer != null) {
                mPlayerView.onPause();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
//            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
//            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (mPlayerView != null) {
                mPlayerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (mPlayerView != null) {
                mPlayerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        RxBus.getIntance().unSubscribe(this.getClass().getSimpleName() + message.getUuid());
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}
