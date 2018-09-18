package com.zhuzichu.library.utils;

import android.content.Context;
import android.text.format.Formatter;

import java.io.File;

public class NiceCacheUtils {
    private final static String GLIDE_DISCACHE_DIR = "/glide_cache_dir";
    /**
     * 获取glide缓存目录
     *
     * @param context context
     * @return 缓存目录
     */
    public static File getGlideDiskCacheDir(Context context) {
        String path;
        if (SDCardUtils.isSDCardMounted()) {
            path = context.getExternalCacheDir() + GLIDE_DISCACHE_DIR;
        } else {
            path = context.getCacheDir() + GLIDE_DISCACHE_DIR;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsoluteFile();
    }



    /**
     * 获取Glide缓存大小
     *
     * @param context context
     * @return 缓存大小
     */
    public static String getGlidecacheFileSizeStr(Context context) {
        long fileSize = getGlidecacheFileSizeNum(context);
        return Formatter.formatFileSize(context, fileSize);
    }

    public static long getGlidecacheFileSizeNum(Context context) {
        long fileSize = 0;
        File file = getGlideDiskCacheDir(context);
        for (File childFile : file.listFiles()) {
            fileSize += childFile.length();
        }
        return fileSize;
    }
}
