package com.zhuzichu.library.utils;

import android.os.Environment;

public class SDCardUtils {
    /**
     * 存储卡是否挂载
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
}
