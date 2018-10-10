package com.zhuzichu.library.ui.scaner;

import android.view.View;

import com.zhuzichu.library.base.BasePresenter;

public interface ScannerPresenter extends BasePresenter {

    /**
     * 是否开启闪光灯
     *
     * @param view
     */
    void light(View view);

    /**
     * 返回
     *
     * @param view
     */
    void back(View view);

    /**
     * 选择图片
     *
     * @param view
     */
    void select(View view);

}
