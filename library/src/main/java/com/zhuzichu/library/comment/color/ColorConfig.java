package com.zhuzichu.library.comment.color;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.zhuzichu.library.BR;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.R;


public class ColorConfig extends BaseObservable {
    private static final String TAG = "ColorConfig";
    public int colorPrimary;
    public int textColorPrimary;
    public int textColorSeconday;
    public int windowBackground;

    //是否夜间模式
    public boolean isDark;

    public ColorConfig() {
    }

    public ColorConfig(int colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    @Bindable
    public int getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(int colorPrimary) {
        this.colorPrimary = colorPrimary;
        notifyPropertyChanged(BR.colorPrimary);
    }

    @Bindable
    public int getTextColorPrimary() {
        return textColorPrimary;
    }

    public void setTextColorPrimary(int textColorPrimary) {
        this.textColorPrimary = textColorPrimary;
        notifyPropertyChanged(BR.textColorPrimary);
    }

    @Bindable
    public int getTextColorSeconday() {
        return textColorSeconday;
    }

    public void setTextColorSeconday(int textColorSeconday) {
        this.textColorSeconday = textColorSeconday;
        notifyPropertyChanged(BR.textColorSeconday);
    }

    @Bindable
    public int getWindowBackground() {
        return windowBackground;
    }

    public void setWindowBackground(int windowBackground) {
        this.windowBackground = windowBackground;
        notifyPropertyChanged(BR.windowBackground);
    }

    public void setDark(boolean dark) {
        Log.i(TAG, "setDark: "+dark);
        if (dark) {
            //夜间模式
            setTextColorPrimary(Nice.getColor(R.color.qmui_config_color_white));
            setTextColorSeconday(Nice.getColor(R.color.color_grey_eaeaea));
            setWindowBackground(Nice.getColor(R.color.color_grey_555555));

        } else {
            //非夜间模式
            setTextColorPrimary(Nice.getColor(R.color.color_grey_333333));
            setTextColorSeconday(Nice.getColor(R.color.color_grey_999999));
            setWindowBackground(Nice.getColor(R.color.qmui_config_color_white));
        }
        isDark = dark;
    }
}
