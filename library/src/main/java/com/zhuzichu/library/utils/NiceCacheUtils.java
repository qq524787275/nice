package com.zhuzichu.library.utils;

import android.content.Context;
import android.text.format.Formatter;

import java.io.File;

public class NiceCacheUtils {
    private final static String GLIDE_DISCACHE_DIR = "/glide_cache_dir";
    private final static String NICE_DISCACHE_DIR = "/nice_cache_dir";
    private final static String WEBVIEW_APPCACHE_DISCACHE_DIR = "/webview_cache_dir/appcache";
    private final static String WEBVIEW_DATABASES_DISCACHE_DIR = "/webview_cache_dir/databases";
    private final static String WEBVIEW_GEOLOCATION_DISCACHE_DIR = "/webview_cache_dir/geolocation";

    public static File getDiskCacheDir(Context context, String last) {
        String path;
        if (SDCardUtils.isSDCardMounted()) {
            path = context.getExternalCacheDir() + last;
        } else {
            path = context.getCacheDir() + last;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsoluteFile();
    }

    public static File getWebViewDatabasesDiskCacheDir(Context context) {
        return getDiskCacheDir(context, WEBVIEW_DATABASES_DISCACHE_DIR);
    }

    public static File getWebViewGeolocationDiskCacheDir(Context context) {
        return getDiskCacheDir(context, WEBVIEW_GEOLOCATION_DISCACHE_DIR);
    }

    public static File getWebViewAppcacheDiskCacheDir(Context context) {
        return getDiskCacheDir(context, WEBVIEW_APPCACHE_DISCACHE_DIR);
    }

    public static File getNiceDiskCacheDir(Context context) {
        return getDiskCacheDir(context, NICE_DISCACHE_DIR);
    }

    /**
     * 获取glide缓存目录
     *
     * @param context context
     * @return 缓存目录
     */
    public static File getGlideDiskCacheDir(Context context) {
        return getDiskCacheDir(context, GLIDE_DISCACHE_DIR);
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
