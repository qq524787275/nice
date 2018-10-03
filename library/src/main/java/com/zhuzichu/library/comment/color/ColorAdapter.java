package com.zhuzichu.library.comment.color;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhuzichu.library.view.bottom.BottomBar;

public class ColorAdapter {

    @BindingAdapter({"textColor"})
    public static void textColor(TextView textView, String color){

    }

    @BindingAdapter({"topBgColor"})
    public static  void topBgColor(QMUITopBar qmuiTopBar,int color){
        qmuiTopBar.setBackgroundColor(color);
    }

    @BindingAdapter({"bottomSelectColor"})
    public static  void bottomSelectColor(BottomBar bottomBar, int color){
        bottomBar.setSelectColor(color);
        bottomBar.setCurrentItem(bottomBar.getCurrentItemPosition());
    }

    @BindingAdapter({"bottomBgColor"})
    public static  void bottomBgColor(BottomBar bottomBar, int color){
        bottomBar.setBottomBgColor(color);
    }
}
