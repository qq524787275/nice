package com.zhuzichu.library.widget;

import android.view.View;

public abstract class OnClickListener implements View.OnClickListener {
    public static Long lastClickTime = 0L;
    public static final long minTime = 200;

    public abstract void onNoDoubleClick(View view);

    @Override
    public void onClick(View view) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > minTime) {
            lastClickTime = currentTime;
            onNoDoubleClick(view);
            return;
        }
    }
}
