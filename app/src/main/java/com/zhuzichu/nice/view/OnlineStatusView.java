package com.zhuzichu.nice.view;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.StatusCode;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.nice.R;
import com.zhuzichu.uikit.observer.action.ActionOnlineStatus;

import io.reactivex.disposables.Disposable;

public class OnlineStatusView extends LinearLayout implements LifecycleObserver {
    private TextView mTitle;
    private QMUILoadingView mLoading;

    public OnlineStatusView(Context context) {
        this(context, null);
    }

    public OnlineStatusView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OnlineStatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_online_status, this, true);
        mTitle = findViewById(R.id.title);
        mLoading = findViewById(R.id.loading);
        mLoading.setColor(Nice.getColor(R.color.white));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        initObserver();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        RxBus.getIntance().unSubscribe(this);
    }

    private void initObserver() {
        Disposable disposable = RxBus.getIntance().doSubscribe(ActionOnlineStatus.class, action -> {
            StatusCode code = action.getData();
            if (code.wontAutoLogin()) {

            } else {
                if (code == StatusCode.NET_BROKEN) {
                    setVisibility(VISIBLE);
                    mTitle.setText("网络不可用...");
                } else if (code == StatusCode.UNLOGIN) {
                    setVisibility(VISIBLE);
                    mTitle.setText("断开中...");
                } else if (code == StatusCode.CONNECTING) {
                    setVisibility(VISIBLE);
                    mTitle.setText("连接中...");
                } else if (code == StatusCode.LOGINING) {
                    setVisibility(VISIBLE);
                    mTitle.setText("登录中...");
                } else {
                    setVisibility(GONE);
                }
            }
        });
        RxBus.getIntance().addSubscription(this, disposable);
    }


}
