package com.zhuzichu.library.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.databinding.Observable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhuzichu.library.comment.color.ColorConfig;
import com.zhuzichu.library.comment.color.ColorManager;

import java.lang.ref.WeakReference;

public class StatusDelegate extends Observable.OnPropertyChangedCallback implements LifecycleObserver {
    private static final String TAG = "StatusDelegate";
    private Context mContext;

    private View mStatusBar;
    private boolean mIsAuto = true;

    /**
     * 初始化状态栏
     *
     * @param root
     * @param ref
     * @return
     */
    public View init(View root, WeakReference<BaseFragment> ref) {
        mContext = ref.get().getContext();
        LinearLayout parentView = new LinearLayout(mContext);
        parentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        parentView.setOrientation(LinearLayout.VERTICAL);
        parentView.addView(root);

        //添加状态栏
        mStatusBar = new View(mContext);
        mStatusBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, QMUIStatusBarHelper.getStatusbarHeight(mContext)));
        mStatusBar.setBackgroundColor(ColorManager.getInstance().color.colorPrimary);
        //android api19 以下不支持沉浸式。
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
            parentView.addView(mStatusBar, 0);
        ref.get().getLifecycle().addObserver(this);

        return parentView;
    }


    public void hide() {
        mStatusBar.setVisibility(View.GONE);
    }

    public void show() {
        mStatusBar.setVisibility(View.VISIBLE);
    }

    public void setColor(int id) {
        mStatusBar.setBackgroundColor(mContext.getResources().getColor(id));
    }

    public void autoColorPrimary(boolean isAuto) {
        this.mIsAuto = isAuto;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        ColorManager.getInstance().getColorConfig().addOnPropertyChangedCallback(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        ColorManager.getInstance().getColorConfig().removeOnPropertyChangedCallback(this);
    }

    @Override
    public void onPropertyChanged(Observable sender, int propertyId) {
        if (!mIsAuto)
            return;
        mStatusBar.setBackgroundColor(((ColorConfig) sender).colorPrimary);
    }
}
