package com.zhuzichu.library.comment.color;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhuzichu.library.view.bottom.BottomBar;
import com.zhuzichu.library.view.recyclerview.RecyclerFastScroller;

public class ColorAdapter {

    @BindingAdapter({"textColor"})
    public static void textColor(TextView textView, int color) {
        textView.setTextColor(color);
    }

    @BindingAdapter({"bottomSelectColor"})
    public static void bottomSelectColor(BottomBar bottomBar, int color) {
        bottomBar.setSelectColor(color);
        bottomBar.setCurrentItem(bottomBar.getCurrentItemPosition());
    }

    @BindingAdapter({"bottomBgColor"})
    public static void bottomBgColor(BottomBar bottomBar, int color) {
        bottomBar.setBottomBgColor(color);
    }

    @BindingAdapter({"handleNormalColor"})
    public static void handleNormalColor(RecyclerFastScroller scroller, int color) {
        scroller.setHandleNormalColor(color);
    }

    @BindingAdapter({"handlePressedColor"})
    public static void handlePressedColor(RecyclerFastScroller scroller, int color) {
        scroller.setHandlePressedColor(color);
    }

    @BindingAdapter({"srlPrimaryColor"})
    public static void srlPrimaryColor(SmartRefreshLayout smartRefreshLayout, int color) {
        smartRefreshLayout.setPrimaryColors(color);
    }
}
