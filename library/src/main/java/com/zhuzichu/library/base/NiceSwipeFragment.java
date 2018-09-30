package com.zhuzichu.library.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class NiceSwipeFragment extends BaseSwipeFragment {
    public abstract void init(ViewDataBinding binding);

    public final StatusDelegate _status = new StatusDelegate();

    @Override
    public View getRootView(View rootView) {
        super.getRootView(rootView);
        ViewDataBinding binding = DataBindingUtil.bind(rootView);
        rootView = _status.init(binding.getRoot(), this);
        init(binding);
        return rootView;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
