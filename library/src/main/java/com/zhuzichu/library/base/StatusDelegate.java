package com.zhuzichu.library.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.databinding.Observable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhuzichu.library.comment.color.ColorConfig;
import com.zhuzichu.library.comment.color.ColorManager;

public class StatusDelegate extends Observable.OnPropertyChangedCallback implements LifecycleObserver {
    private static final String TAG = "StatusDelegate";
    private Context mContext;
    private LinearLayout mParentView;
    private View mStatusBar;
    private boolean mIsAuto=true;
    /**
     * 初始化状态栏
     *
     * @param root
     * @param fragment
     * @return
     */
    public View init(View root, Fragment fragment) {
        mContext = fragment.getContext();
        mParentView = new LinearLayout(mContext);
        mParentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mParentView.setOrientation(LinearLayout.VERTICAL);
        mParentView.addView(root);

        //添加状态栏
        mStatusBar = new View(mContext);
        mStatusBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, QMUIStatusBarHelper.getStatusbarHeight(mContext)));
        mStatusBar.setBackgroundColor(ColorManager.getInstance().color.colorPrimary);
        mParentView.addView(mStatusBar, 0);

        fragment.getLifecycle().addObserver(this);

        return mParentView;
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
        this.mIsAuto=isAuto;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Log.i(TAG, "onCreate: ");
        ColorManager.getInstance().getColorConfig().addOnPropertyChangedCallback(this);
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        ColorManager.getInstance().getColorConfig().removeOnPropertyChangedCallback(this);
    }

    @Override
    public void onPropertyChanged(Observable sender, int propertyId) {
        if(!mIsAuto)
            return;
        mStatusBar.setBackgroundColor(((ColorConfig)sender).colorPrimary);
    }
}
