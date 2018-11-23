package com.zhuzichu.library.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.LinearInterpolator;

import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.R;
import com.zhuzichu.library.utils.DensityUtils;
import com.zhuzichu.library.view.layout.StateLayout;

import java.text.DecimalFormat;

/**
 * Created by wb.zhuzichu18 on 2018/11/19.
 */
public class DownLoadView extends View {

    public interface State {
        //正常状态
        int NORMAL = 0;
        //下载状态
        int LOADING = 1;
        //下载完成状态
        int FINISH = 2;
    }

    private Canvas canvas;
    private int colorPrimary;
    private int backgroundColor;
    private Paint mOutlinePaint;
    private Paint mColorPrimaryPaint;
    private TextPaint mColorText;
    private int w;
    private int h;
    private int radius;
    private float progress;
    private ObjectAnimator progressAnimator;
    //当前状态
    private int state;
    private DecimalFormat df;

    public DownLoadView(Context context) {
        this(context, null);
    }

    public DownLoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownLoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        df = new DecimalFormat("#.##");
        state = State.NORMAL;
        progressAnimator = ObjectAnimator.ofFloat(this, "progress", 0f, 1f);
        progressAnimator.setDuration(10 * 1000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(animation -> {
            progress = (float) (animation.getAnimatedValue());
            if (progress == 1f)
                state = State.FINISH;
            invalidate();
        });

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DownLoadView);
        radius = DensityUtils.dip2px(getContext(), typedArray.getDimension(R.styleable.DownLoadView_radius, 25));
        progress = 0;
        colorPrimary = typedArray.getColor(R.styleable.DownLoadView_android_color, Nice.getColor(R.color.app_color_theme_0));
        backgroundColor = QMUIColorHelper.computeColor(colorPrimary, Nice.getColor(R.color.white), 0.5f);

        mOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutlinePaint.setColor(typedArray.getColor((R.styleable.DownLoadView_android_strokeColor), 0xff999999));
        mOutlinePaint.setStrokeWidth(typedArray.getDimension(R.styleable.DownLoadView_outlineWidth, 3));
        mOutlinePaint.setStyle(Paint.Style.STROKE);

        mColorPrimaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorPrimaryPaint.setColor(colorPrimary);

        mColorText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mColorText.setColor(typedArray.getColor(R.styleable.DownLoadView_android_textColor, Nice.getColor(R.color.white)));

        int textSize = DensityUtils.dip2px(getContext(), typedArray.getDimension(R.styleable.DownLoadView_android_textSize, 16));
        mColorText.setTextSize(textSize);

        typedArray.recycle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setClipToOutline(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = getW(widthMeasureSpec);
        h = getH(heightMeasureSpec);
        setMeasuredDimension(w, h);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, w, h, radius);
                }
            });
    }

    public void start() {
        state = State.LOADING;
        progressAnimator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        switch (state) {
            case State.NORMAL:
                canvas.drawColor(colorPrimary);
                drawTextCenter("开始下载");
                setOnClickListener(v -> {
                    start();
                });
                break;
            case State.FINISH:
                canvas.drawColor(colorPrimary);
                drawTextCenter("下载完成");
                setOnClickListener(v -> {
                    start();
                });
                break;
            case State.LOADING:
                //画边框
                canvas.drawRoundRect(new RectF(0, 0, w, h), radius, radius, mOutlinePaint);
                //画底色
                canvas.drawColor(backgroundColor);
                //画进度条
                mColorPrimaryPaint.setColor(colorPrimary);
                canvas.drawRoundRect(new RectF(0, 0, w * progress, h), radius, radius, mColorPrimaryPaint);
                //画百分比
                String text = df.format(progress * 100) + "%";
                drawTextCenter(text);
                setOnClickListener(v -> {

                });
                break;
            default:
                break;
        }
    }

    private void drawTextCenter(String text) {
        canvas.save();
        canvas.translate(w / 2, h / 2);
        float textWidth = mColorText.measureText(text);
        float baseLineY = Math.abs(mColorText.ascent() + mColorText.descent()) / 2;
        canvas.drawText(text, -textWidth / 2, baseLineY, mColorText);
        canvas.restore();
    }

    /**
     * 获取控件高
     *
     * @param heightMeasureSpec
     * @return
     */
    private int getH(int heightMeasureSpec) {
        int h;
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                h = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                //父控件是wrap_content
                h = DensityUtils.dip2px(getContext(), 50);
                break;
            case MeasureSpec.UNSPECIFIED:
                h = Integer.MAX_VALUE;
                break;
            default:
                h = Integer.MAX_VALUE;
                break;
        }
        return h;
    }

    /**
     * 获取控件宽
     *
     * @param widthMeasureSpec
     * @return
     */
    private int getW(int widthMeasureSpec) {
        int w;
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                w = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                //父控件是wrap_content
                w = DensityUtils.dip2px(getContext(), 200);
                break;
            case MeasureSpec.UNSPECIFIED:
                w = Integer.MAX_VALUE;
                break;
            default:
                w = Integer.MAX_VALUE;
                break;
        }
        return w;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setColorPrimary(int colorPrimary) {
        this.colorPrimary = colorPrimary;
        backgroundColor = QMUIColorHelper.computeColor(colorPrimary, Nice.getColor(R.color.white), 0.5f);
        invalidate();
    }

    @BindingAdapter("colorPrimary")
    public static void setColorPrimary(DownLoadView downLoadView, int color) {
        downLoadView.setColorPrimary(color);
    }
}
