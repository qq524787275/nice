package com.zhuzichu.nice;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.utils.UserPreferences;
import com.zhuzichu.uikit.message.provider.MsgProviderImage;

import java.io.IOException;

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
        QbSdk.initX5Environment(this, null);
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
        Default.sdkStorageRootPath = getAppCacheDir(getApplicationContext()) + "/nice"; // 可以不设置，那么将采用默认路径
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

    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    public String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
            storageRootPath = Environment.getExternalStorageDirectory() + "/" + getApplicationContext().getPackageName();
        }

        return storageRootPath;
    }
}
