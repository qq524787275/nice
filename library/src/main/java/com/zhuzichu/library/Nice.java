package com.zhuzichu.library;

import android.content.Context;

import com.zhuzichu.library.comment.color.ColorManager;

public class Nice {
    public interface Extra {
        String ACTION_START_FRAGMENT = "action_start_fragment";
    }

    public static Context context;
    private volatile static Nice mNice;

    /**
     * 构造方法私有化
     */
    private Nice(Context ctx) {
        context = ctx;
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
}
