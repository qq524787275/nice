package com.zhuzichu.nice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;
import com.tencent.smtt.sdk.TbsListener;
import com.zhuzichu.library.utils.NetworkUtil;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public class InitalizeService extends IntentService {
    private static final String TAG = "InitalizeService";
    private static final String ACTION_INIT_WHEN_APP_CREATE = "com.zhuzichu.nice.InitalizeService";

    public InitalizeService() {
        super(TAG);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitalizeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit();
            }
        }
    }

    private void performInit() {
        long start = System.currentTimeMillis();

        QbSdk.preInit(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.i(TAG, "onCoreInitFinished: ");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                Log.i(TAG, "onViewInitFinished: " + b);
                Log.i(TAG, "performInit-----getTBSInstalling: "+QbSdk.getTBSInstalling());
                Log.i(TAG, "performInit------getTbsVersion: "+QbSdk.getTbsVersion(getApplicationContext()));
                Log.i(TAG, "performInit------isTbsCoreInited: "+QbSdk.isTbsCoreInited());
            }
        });
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.i(TAG, "onDownloadFinish: " + i);
//                TbsDownloader.startDownload(InitalizeService.this);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.i(TAG, "onInstallFinish: " + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.i(TAG, "onDownloadProgress: " + i);
            }
        });
        boolean needDownload = TbsDownloader.needDownload(getApplicationContext(), false);
        Log.i(TAG, "performInit: " + needDownload);
        TbsDownloader.startDownload(this);
        if (needDownload && NetworkUtil.isWifi(getApplicationContext())) {
            Log.i(TAG, "performInit: 开始下载x5内核");
            TbsDownloader.startDownload(getApplicationContext());
        }
        Log.i(TAG, "performInit-----getTBSInstalling: "+QbSdk.getTBSInstalling());
        Log.i(TAG, "performInit------getTbsVersion: "+QbSdk.getTbsVersion(this));
        Log.i(TAG, "performInit------isTbsCoreInited: "+QbSdk.isTbsCoreInited());
        long end = System.currentTimeMillis();
        Log.i(TAG, "performInit: 初始化消耗时间:" + (end - start));
    }
}
