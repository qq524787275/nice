package com.zhuzichu.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhuzichu.library.utils.DensityUtils;

/**
 * Created by wb.zhuzichu18 on 2018/10/31.
 */
public class StatusBarView extends View {
    private Context mContext;

    public StatusBarView(Context context) {
        this(context, null);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(DensityUtils.getScreenW(mContext), DensityUtils.getStatuBarH(mContext));
    }
}
