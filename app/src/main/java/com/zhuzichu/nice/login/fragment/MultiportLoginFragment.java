package com.zhuzichu.nice.login.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.zhuzichu.library.base.NiceSwipeFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.widget.NiceRequestCallback;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentMultiportLoginBinding;
import com.zhuzichu.nice.login.adapter.MultiportAdapter;
import com.zhuzichu.uikit.observer.action.ActionOnlienClient;

import java.io.Serializable;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by wb.zhuzichu18 on 2018/10/26.
 * 多端登录管理
 */
public class MultiportLoginFragment extends NiceSwipeFragment<FragmentMultiportLoginBinding> {
    private MultiportAdapter mAdapter;

    public interface Extra {
        String EXTRA_DATA = "data";
    }

    public static MultiportLoginFragment newInstance(List<OnlineClient> data) {

        Bundle args = new Bundle();
        args.putSerializable(Extra.EXTRA_DATA, (Serializable) data);
        MultiportLoginFragment fragment = new MultiportLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentMultiportLoginBinding mBind;

    @Override
    public Object setLayout() {
        return R.layout.fragment_multiport_login;
    }

    @Override
    public void init(FragmentMultiportLoginBinding binding) {
        mBind = binding;
        mBind.setColor(ColorManager.getInstance().color);
        initView();
        initTopBar();
        initListener();
        initObserver();
    }

    private void initTopBar() {
        mBind.topbar.setTitle("多端登录管理");
        mBind.topbar.addLeftBackImageButton().setOnClickListener(view -> pop());
    }

    private void initListener() {
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> kickOtherOut(mAdapter.getData().get(position)));
    }

    private void initView() {
        //初始化recyclerview
        mBind.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MultiportAdapter();
        mBind.rv.setAdapter(mAdapter);
    }

    private void initObserver() {
        Disposable disposable = RxBus.getIntance().doSubscribe(ActionOnlienClient.class, action -> mAdapter.setNewData(action.data));
        RxBus.getIntance().addSubscription(this, disposable);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        List<OnlineClient> data = (List<OnlineClient>) getArguments().getSerializable(Extra.EXTRA_DATA);
        mAdapter.setNewData(data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getIntance().unSubscribe(this);
    }


    private void kickOtherOut(final OnlineClient client) {
        NIMClient.getService(AuthService.class).kickOtherClient(client).setCallback(new NiceRequestCallback<Void>(getActivity()) {
            @Override
            public void success(Void param) {

            }
        });
    }
}
