package com.zhuzichu.library;

import android.Manifest;
import android.content.Context;

import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.utils.DensityUtils;

public class Nice {

    public static String account;
    public static Context context;
    public static int w;
    public static int h;
    private volatile static Nice mNice;


    /**
     * 构造方法私有化
     */
    private Nice(Context ctx) {
        context = ctx;
        w = DensityUtils.getScreenW(ctx);
        h = DensityUtils.getScreenH(ctx);
        //初始化主题颜色
        ColorManager.getInstance();
    }

    /**
     * DCL方式获取单例
     *
     * @return
     */
    public static Nice init(Context context) {
        if (mNice == null) {
            synchronized (Nice.class) {
                mNice = new Nice(context);
            }
        }
        return mNice;
    }

    public static Nice getNice() {
        return mNice;
    }

    /**
     * 通过id获取颜色的16进制数
     *
     * @param id 颜色id
     * @return
     */
    public static int getColor(int id) {
        return context.getResources().getColor(id);
    }

    public static String getString(int id) {
        return context.getResources().getString(id);
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        Nice.account = account;
    }

    public static Context getContext() {
        return context;
    }


    public interface PermissionCode {
        //进入app的基本权限 code
        int BASIC_PERMISSION_REQUEST_CODE = 110;
        //二维码扫描权限 code
        int SCANNER_PERMISSION_REQUEST_CODE = 120;
        //定位权限
        int LOCATION_PERMISSION_REQUEST_CODE = 130;
    }

    public interface Permission {
        //进入app的基本权限
        String[] BASIC_PERMISSIONS = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        //二维码扫描权限
        String[] SCANNER_PERMISSION = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.VIBRATE};
        //定位权限
        String[] LOCATION_PERMISSIONS = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
        };
    }
}
