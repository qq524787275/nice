package com.zhuzichu.library.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class NiceSwipeFragment extends BaseSwipeFragment {
    public abstract void init(ViewDataBinding binding);
    final StatusDelegate _status = new StatusDelegate();

    @Override
    public View getRootView(View rootView) {
        super.getRootView(rootView);
        ViewDataBinding binding = DataBindingUtil.bind(rootView);
        init(binding);
        return _status.init(binding.getRoot(),this);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
