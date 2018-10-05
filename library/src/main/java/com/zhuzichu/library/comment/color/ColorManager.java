package com.zhuzichu.library.comment.color;

import com.google.common.base.Optional;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.R;
import com.zhuzichu.library.utils.UserPreferences;

public class ColorManager {
    //主题颜色
    public ColorConfig color;

    private volatile static ColorManager mInstance;

    /**
     * 构造方法私有化
     */
    private ColorManager() {
        color = initDefaultColorConfig();
    }

    /**
     * DCL方式获取单例
     *
     * @return
     */
    public static ColorManager getInstance() {
        if (mInstance == null) {
            synchronized (ColorManager.class) {
                mInstance = new ColorManager();
            }
        }
        return mInstance;
    }


    /**
     * 初始化默认主题颜色
     *
     * @return
     */
    private ColorConfig initDefaultColorConfig() {
        Optional<ColorConfig> colorConfigOptional = Optional.fromNullable(UserPreferences.getColorConfig());
        if (colorConfigOptional.isPresent()) {
            return colorConfigOptional.get();
        }
        return getDefaultColorConfig();
    }

    public ColorConfig getDefaultColorConfig() {
        ColorConfig colorConfig = new ColorConfig();
        colorConfig.setColorPrimary(Nice.getColor(R.color.app_color_theme_2));
        colorConfig.setDark(false);
        return colorConfig;
    }

    public ColorConfig getColorConfig() {
        return color;
    }
}
