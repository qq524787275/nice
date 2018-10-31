package com.zhuzichu.library.view.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by wb.zhuzichu18 on 2018/10/26.
 */
public class FishDrawableSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {


    private SurfaceHolder mHolder;
    private Thread mThread;
    private boolean mFlag;
    private Canvas mCanvas;

    public FishDrawableSurfaceView(Context context) {
        this(context, null);
    }

    public FishDrawableSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FishDrawableSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHolder = getHolder();
        mHolder.addCallback(this);
        setZOrderMediaOverlay(true);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        init();
    }

    private void init() {


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //创建
        mThread = new Thread(this);
        mFlag = true;
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //窗口改变
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //销毁
        mFlag = false;
        mHolder.removeCallback(this);
    }

    @Override
    public void run() {
        while (mFlag) {
            try {
                synchronized (mHolder) {
                    Thread.sleep(30);
                    Draw();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null) {
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }

    private void Draw() {
        mCanvas = mHolder.lockCanvas();
        if (mCanvas == null) ;
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);


    }
}
