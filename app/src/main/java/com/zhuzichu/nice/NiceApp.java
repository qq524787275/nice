package com.zhuzichu.nice;

import android.app.Application;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.zhuzichu.library.Nice;

public class NiceApp extends Application {
    private static final String TAG = "NiceApp";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: 初始化nimsdk");
        NIMClient.init(this, loginInfo(), options());
        Nice.init(this);
    }

    private SDKOptions options() {
        return null;
    }

    private LoginInfo loginInfo() {
        return null;
    }
}
