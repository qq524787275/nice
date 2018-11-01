package com.zhuzichu.library.utils;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.zhuzichu.library.Nice;

public class DrawableUtils {
    public static Drawable transformColor(Drawable drawable, int color) {
        Drawable mutate = drawable.mutate();
        mutate.setColorFilter(Nice.getColor(color), PorterDuff.Mode.SRC_IN);
        return mutate;
    }
}
