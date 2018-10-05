package com.zhuzichu.library.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.zhuzichu.library.comment.color.ColorManager;

public class DialogUtils {
    public static void showInfoDialog(Context context, String title, String info, MaterialDialog.SingleButtonCallback callback) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(info)
                .theme(ColorManager.getInstance().getColorConfig().isDark ? Theme.DARK : Theme.LIGHT)
                .positiveText("确定")
                .onPositive(callback)
                .build()
                .show();
    }

    public static void showInfoDialog(Context context, String title, String info) {
        showInfoDialog(context, title, info, null);
    }

    public static void showInfoDialog(Context context, String info) {
        showInfoDialog(context, null, info, null);
    }
}
