package com.zhuzichu.uikit.widget;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.observer.action.ActionOnlienClient;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by wb.zhuzichu18 on 2018/10/26.
 * 多端登录
 */
public class MultiportView extends LinearLayout implements LifecycleObserver {
    private TextView mTvDesc;
    private List<OnlineClient> mData;

    public MultiportView(Context context) {
        this(context, null);
    }

    public MultiportView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiportView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_multiport, this);
        mTvDesc = findViewById(R.id.multiport_desc);
        setVisibility(GONE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        initObserver();
    }

    private void initObserver() {
        Disposable disposable = RxBus.getIntance().doSubscribe(ActionOnlienClient.class, action -> {
            if (action.data == null) {
                setVisibility(GONE);
                return;
            }
            mData = action.data;
            if (mData.size() == 0) {
                setVisibility(GONE);
            } else {
                setVisibility(VISIBLE);
                OnlineClient onlineClient = mData.get(0);
                switch (onlineClient.getClientType()) {
                    case ClientType.Windows:
                        mTvDesc.setText("Windows Nice已登录");
                        break;
                    case ClientType.MAC:
                        mTvDesc.setText("MAC Nice已登录");
                        break;
                    case ClientType.Web:
                        mTvDesc.setText("Web Nice已登录");
                        break;
                    case ClientType.iOS:
                        mTvDesc.setText("iOS Nice已登录");
                        break;
                    case ClientType.Android:
                        mTvDesc.setText("Android Nice已登录");
                        break;
                    default:
                        setVisibility(View.GONE);
                        break;
                }
            }
        });
        RxBus.getIntance().addSubscription(this, disposable);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        RxBus.getIntance().unSubscribe(this);
    }

    public List<OnlineClient> getData() {
        return mData;
    }
}
