package com.zhuzichu.library.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

public abstract class NiceFragment<T> extends BaseFragment {
    public StatusDelegate _status;

    public abstract void init(T binding);

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.bind(rootView);
        _status = new StatusDelegate();
        rootView = _status.init(binding.getRoot(), new WeakReference<>(this));
        init((T) binding);
        return rootView;
    }
}
