package com.zhuzichu.library.widget;

import android.content.Context;
import android.util.Log;

import com.netease.nimlib.sdk.RequestCallback;
import com.zhuzichu.library.enmu.ErrorEnmu;
import com.zhuzichu.library.utils.DialogUtils;

public abstract class NiceRequestCallback<T> implements RequestCallback<T> {
    private static final String TAG = "NiceRequestCallback";
    private Context mContext;

    public void finish() {
    }

    public abstract void success(T param);

    @Override
    public void onSuccess(T param) {
        success(param);
        finish();
    }

    public NiceRequestCallback(Context context) {
        mContext = context;
    }

    @Override
    public void onFailed(int code) {
        DialogUtils.showInfoDialog(mContext, ErrorEnmu.getErrorEnmu(code).getMsg());
        finish();
    }

    @Override
    public void onException(Throwable exception) {
        exception.printStackTrace();
        Log.i(TAG, "onException: 数据异常");
        finish();
    }
}
