package com.zhuzichu.nice;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.squareup.leakcanary.LeakCanary;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.utils.NiceCacheUtils;
import com.zhuzichu.library.utils.UserPreferences;
import com.zhuzichu.uikit.message.provider.MsgProviderImage;


public class NiceApp extends Application {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Nice.init(this);
        NIMClient.config(this, loginInfo(), options());
        if (NIMUtil.isMainProcess(this)) {
            InitalizeService.start(this);
        }
//        setupLeakCanary();
    }

    private SDKOptions options() {
        SDKOptions Default = SDKOptions.DEFAULT;
        Default.asyncInitSDK = true;
        // 在线多端同步未读数
        Default.sessionReadAck = true;
        // 配置是否需要预下载附件缩略图
        Default.preloadAttach = true;
        // 配置附件缩略图的尺寸大小
        Default.thumbnailSize = MsgProviderImage.getImageMaxEdge();
        // 动图的缩略图直接下载原图
        Default.animatedImageThumbnailEnabled = true;
        // 是否是弱IM场景
        Default.reducedIM = false;
        // 配置数据库加密秘钥
        Default.databaseEncryptKey = "NETEASE";
        // 配置 APP 保存图片/语音/文件/log等数据的目录
        Default.sdkStorageRootPath = NiceCacheUtils.getNiceDiskCacheDir(getApplicationContext()).toString(); // 可以不设置，那么将采用默认路径
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

    protected void setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
