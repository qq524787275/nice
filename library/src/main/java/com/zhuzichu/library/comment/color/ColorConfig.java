package com.zhuzichu.library.comment.color;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.zhuzichu.library.BR;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.R;
import com.zhuzichu.library.utils.UserPreferences;


public class ColorConfig extends BaseObservable {
    private static final String TAG = "ColorConfig";
    public int colorPrimary;
    public int colorPrimaryDark;
    public int textColorPrimary;
    public int textColorSeconday;
    public int bottomBackgroud;
    public int windowBackground;
    public int itemBackground;
    public int itemBackgroundDark;
    public int itemMenuBackground;
    //是否夜间模式
    public boolean isDark;

    public ColorConfig() {
    }

    @Bindable
    public int getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(int colorPrimary) {
        this.colorPrimary = colorPrimary;
        setColorPrimaryDark(QMUIColorHelper.computeColor(colorPrimary, Nice.getColor(R.color.black), 0.3f));
        notifyPropertyChanged(BR.colorPrimary);
        UserPreferences.saveColorConfig(this);
    }

    @Bindable
    public int getColorPrimaryDark() {
        return colorPrimaryDark;
    }

    public void setColorPrimaryDark(int colorPrimaryDark) {
        this.colorPrimaryDark = colorPrimaryDark;
        notifyPropertyChanged(BR.colorPrimaryDark);
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

    @Bindable
    public int getItemBackground() {
        return itemBackground;
    }

    public void setItemBackground(int itemBackground) {
        this.itemBackground = itemBackground;
        notifyPropertyChanged(BR.itemBackground);
    }

    @Bindable
    public int getItemMenuBackground() {
        return itemMenuBackground;
    }

    public void setItemMenuBackground(int itemMenuBackground) {
        this.itemMenuBackground = itemMenuBackground;
        notifyPropertyChanged(BR.itemMenuBackground);
    }

    @Bindable
    public int getBottomBackgroud() {
        return bottomBackgroud;
    }

    public void setBottomBackgroud(int bottomBackgroud) {
        this.bottomBackgroud = bottomBackgroud;
        notifyPropertyChanged(BR.bottomBackgroud);
    }

    @Bindable
    public int getItemBackgroundDark() {
        return itemBackgroundDark;
    }

    public void setItemBackgroundDark() {
        if (isDark) {
            this.itemBackgroundDark = QMUIColorHelper.computeColor(itemBackground, Nice.getColor(R.color.white), 0.075f);
        } else {
            this.itemBackgroundDark = QMUIColorHelper.computeColor(itemBackground, Nice.getColor(R.color.black), 0.075f);
        }
        notifyPropertyChanged(BR.itemBackgroundDark);
    }


    public void setDark(boolean dark) {
        Log.i(TAG, "setDark: " + dark);
        isDark = dark;
        if (dark) {
            //夜间模式
            setTextColorPrimary(Nice.getColor(R.color.qmui_config_color_white));
            setTextColorSeconday(Nice.getColor(R.color.color_grey_eaeaea));
            setWindowBackground(Nice.getColor(R.color.color_black_23242E));
            setItemBackground(Nice.getColor(R.color.color_black_2A2B3A));
            setItemMenuBackground(Nice.getColor(R.color.color_grey_424242));
            setBottomBackgroud(Nice.getColor(R.color.color_black_313335));
        } else {
            //非夜间模式
            setTextColorPrimary(Nice.getColor(R.color.color_grey_333333));
            setTextColorSeconday(Nice.getColor(R.color.color_grey_999999));
            setWindowBackground(Nice.getColor(R.color.qmui_config_color_white));
            setItemBackground(Nice.getColor(R.color.qmui_config_color_white));
            setItemMenuBackground(Nice.getColor(R.color.qmui_config_color_white));
            setBottomBackgroud(Nice.getColor(R.color.color_white_f8f8f8));
        }
        setItemBackgroundDark();
        UserPreferences.saveColorConfig(this);
    }
}
