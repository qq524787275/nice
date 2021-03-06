package com.zhuzichu.nice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

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

        long end = System.currentTimeMillis();
        Log.i(TAG, "performInit: 初始化消耗时间:" + (end - start));
    }
}
