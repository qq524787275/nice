package com.zhuzichu.library.widget;

import android.content.Context;

import com.netease.nimlib.sdk.RequestCallback;
import com.zhuzichu.library.enmu.ErrorEnmu;
import com.zhuzichu.library.utils.DialogUtils;

public abstract class NiceRequestCallback<T> implements RequestCallback<T> {
    private Context mContext;

    public NiceRequestCallback(Context context) {
        mContext = context;
    }

    @Override
    public void onFailed(int code) {
        DialogUtils.showInfoDialog(mContext, ErrorEnmu.getErrorEnmu(code).getMsg());
    }

    @Override
    public void onException(Throwable exception) {
        DialogUtils.showInfoDialog(mContext, "数据异常");
    }
}
