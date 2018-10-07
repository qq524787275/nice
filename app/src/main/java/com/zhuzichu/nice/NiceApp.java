package com.zhuzichu.nice;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.utils.UserPreferences;

public class NiceApp extends Application {
    private static final String TAG = "NiceApp";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Nice.init(this);
        Log.i(TAG, "onCreate: 初始化nimsdk");
        NIMClient.init(this, loginInfo(), options());
    }

    private SDKOptions options() {
        return null;
    }

    private LoginInfo loginInfo() {
        String account = UserPreferences.getUserAccount();
        String token = UserPreferences.getUserToken();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            Nice.setAccount(account);
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }
}
