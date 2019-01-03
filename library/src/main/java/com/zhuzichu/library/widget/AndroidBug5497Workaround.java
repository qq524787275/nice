package com.zhuzichu.library.widget;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.action.ActionSoftKeyboard;
import com.zhuzichu.library.comment.bus.RxBus;

@Deprecated
public class AndroidBug5497Workaround {
    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
    public static void assistActivity(Activity activity) {
        new AndroidBug5497Workaround(activity);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    private AndroidBug5497Workaround(Activity activity) {
        FrameLayout content = activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(() -> possiblyResizeChildOfContent());
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        //如果两次高度不一致
        if (usableHeightNow != usableHeightPrevious) {
            //将计算的可视高度设置成视图的高度  修复在Android 19 版本下 布局异常
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                frameLayoutParams.height = usableHeightNow;
            } else {
                frameLayoutParams.height = usableHeightNow - QMUIStatusBarHelper.getStatusbarHeight(Nice.context);
            }
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            mChildOfContent.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                //键盘弹起来了
                RxBus.getIntance().post(new ActionSoftKeyboard());
            }
        }
    }

    private int computeUsableHeight() {
        //计+算视图可视高度
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom);
    }

}