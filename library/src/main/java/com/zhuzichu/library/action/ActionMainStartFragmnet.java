package com.zhuzichu.library.action;

import com.zhuzichu.library.base.BaseFragment;

public class ActionMainStartFragmnet {
    public BaseFragment data;

    public BaseFragment getData() {
        return data;
    }

    public void setData(BaseFragment data) {
        this.data = data;
    }

    public ActionMainStartFragmnet(BaseFragment data) {
        this.data = data;
    }
}
