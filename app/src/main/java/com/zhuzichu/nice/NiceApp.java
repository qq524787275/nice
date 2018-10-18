package com.zhuzichu.nice;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.utils.UserPreferences;

public class NiceApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Nice.init(this);
        NIMClient.config(this, loginInfo(), options());
        if (NIMUtil.isMainProcess(this)) {
            InitalizeService.start(this);
        }
    }

    private SDKOptions options() {
        SDKOptions Default = SDKOptions.DEFAULT;
        Default.asyncInitSDK = true;
        return Default;
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
