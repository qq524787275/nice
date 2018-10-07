package com.zhuzichu.uikit.face;

import android.content.Context;
import android.util.AttributeSet;

import com.qmuiteam.qmui.qqface.QMUIQQFaceCompiler;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

public class NiceFaceView extends QMUIQQFaceView {
    public NiceFaceView(Context context) {
        this(context, null);
    }

    public NiceFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCompiler(QMUIQQFaceCompiler.getInstance(FaceManager.getInstance()));
    }
}
