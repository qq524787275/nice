package com.zhuzichu.library.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class NiceFragment<T extends ViewDataBinding> extends BaseFragment {
    public final StatusDelegate mStatus = new StatusDelegate(this);
    public T mBind;

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mBind = DataBindingUtil.bind(rootView);
        rootView = mStatus.bind(mBind.getRoot());
        return rootView;
    }
}
