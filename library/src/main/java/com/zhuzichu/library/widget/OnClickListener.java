package com.zhuzichu.library.widget;

import android.os.SystemClock;
import android.view.View;

public abstract class OnClickListener implements View.OnClickListener {
    public static Long lastClickTime = 0L;
    public static final long minTime = 750L;

    public abstract void onNoDoubleClick(View view);


    @Override
    public void onClick(View view) {
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastClickTime > minTime) {
            lastClickTime = currentTime;
            onNoDoubleClick(view);
            return;
        }
    }

    public static void noDoubleClick(OnNoClickListener listener) {
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastClickTime > minTime) {
            lastClickTime = currentTime;
            listener.onClick();
            return;
        }
    }

    public interface OnNoClickListener {
        void onClick();
    }
}
