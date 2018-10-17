package com.zhuzichu.uikit.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuzichu.uikit.R;


/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public class EmptyView extends LinearLayout {
    private TextView mTitle;
    private ImageView mImage;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_empty, this);
        mTitle = findViewById(R.id.empty_titile);
        mImage = findViewById(R.id.empty_image);
    }

    public void setTitle(String text) {
        mTitle.setText(text);
        mTitle.setVisibility(VISIBLE);
    }

    public void setTitle(int res) {
        mTitle.setText(res);
        mTitle.setVisibility(VISIBLE);
    }

    public void setImageResource(int res) {
        mImage.setImageResource(res);
    }
}
