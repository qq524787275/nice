package com.zhuzichu.library.view.chip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Checkable;

import com.zhuzichu.library.R;

/**
 * Todo 兼容性
 * chipview 最少5.0 以上。否则会崩溃
 */
public class ChipView extends View implements Checkable {
    private static final String TAG = "ChipView";
    private static long SELECTING_DURATION = 350L;
    private static long DESELECTING_DURATION = 200L;
    private Context mContext;
    private Paint mOutlinePaint;
    private int mDefaultTextColor;
    private TextPaint mTextPaint;
    private Paint mDotPaint;
    private Drawable mClear;
    private Drawable mTouchFeedback;
    private boolean mShowIcons;
    private int mPadding;
    private StaticLayout mTextLayout;
    private ValueAnimator mProgressAnimator;
    private float mProgress = 0f;
    private int mSelectedTextColor;
    private CharSequence mText = "测试";
    private Interpolator mInterp;
    private int mColor = 0xffff00ff;

    public ChipView(Context context) {
        this(context, null);
    }

    public ChipView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mInterp = AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ChipView, R.attr.chipViewStyle, R.style.Widget_ChipView);
        mOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutlinePaint.setColor(typedArray.getColor((R.styleable.ChipView_android_strokeColor), 0xff999999));
        mOutlinePaint.setStrokeWidth(typedArray.getDimension(R.styleable.ChipView_outlineWidth, 3));
        mOutlinePaint.setStyle(Paint.Style.STROKE);

        mDefaultTextColor = typedArray.getColor(R.styleable.ChipView_android_textColor, 0xff333333);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mDefaultTextColor);
        mTextPaint.setTextSize(typedArray.getDimension(R.styleable.ChipView_android_textSize, 20));

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setColor(mColor);

        mClear = typedArray.getDrawable(R.styleable.ChipView_clearIcon);
        mClear.setBounds(-mClear.getIntrinsicWidth() / 2, -mClear.getIntrinsicHeight() / 2, mClear.getIntrinsicWidth() / 2, mClear.getIntrinsicHeight() / 2);

        mTouchFeedback = typedArray.getDrawable(R.styleable.ChipView_foreground);
        mTouchFeedback.setCallback(this);

        mPadding = typedArray.getDimensionPixelSize(R.styleable.ChipView_android_padding, 20);
        setChecked(typedArray.getBoolean(R.styleable.ChipView_android_checked, false));
        mShowIcons = typedArray.getBoolean(R.styleable.ChipView_showIcons, true);
        typedArray.recycle();
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setClipToOutline(true);
        }
    }

    public int getSelectedTextColor() {
        return mSelectedTextColor;
    }

    public void setSelectedTextColor(int mSelectedTextColor) {
        this.mSelectedTextColor = mSelectedTextColor;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int nonTextWidth = (int) ((4 * mPadding) + (2 * mOutlinePaint.getStrokeWidth()));
        if (mShowIcons) {
            nonTextWidth = mClear.getIntrinsicWidth() + nonTextWidth;
        } else {
            nonTextWidth = nonTextWidth + 0;
        }

        int availableTextWidth;
        Log.i(TAG, "onMeasure: nonTextWidth:" + nonTextWidth);
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                availableTextWidth = MeasureSpec.getSize(widthMeasureSpec) - nonTextWidth;
                break;
            case MeasureSpec.AT_MOST:
                availableTextWidth = (int) mTextPaint.measureText(mText.toString());
                break;
            case MeasureSpec.UNSPECIFIED:
                availableTextWidth = Integer.MAX_VALUE;
                break;
            default:
                availableTextWidth = Integer.MAX_VALUE;
                break;
        }
        Log.i(TAG, "onMeasure: availableTextWidth:" + availableTextWidth);
        mTextLayout = createLayout(availableTextWidth);

        Log.i(TAG, "onMeasure:------字体宽: " + mTextLayout.getWidth());
        int w = nonTextWidth + mTextLayout.getWidth();
        int h = mPadding + mTextLayout.getHeight() + mPadding;
        Log.i(TAG, "onMeasure: w:" + w);
        Log.i(TAG, "onMeasure: h:" + h);
        setMeasuredDimension(w, h);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        outline.setRoundRect(0, 0, w, h, h / 2f);
                    }
                }
            });
        }
        mTouchFeedback.setBounds(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float strokeWidth = mOutlinePaint.getStrokeWidth();
        float iconRadius = mClear.getIntrinsicWidth() / 2f;
        float halfStroke = strokeWidth / 2f;
        float rounding = (getHeight() - strokeWidth) / 2f;

        //Outline
        if (mProgress < 1f) {
            canvas.drawRoundRect(new RectF(halfStroke, halfStroke, getWidth() - halfStroke, getHeight() - halfStroke), rounding, rounding, mOutlinePaint);
        }

        if (mShowIcons) {
            float dotRadius = lerp(
                    strokeWidth + iconRadius,
                    getWidth(),
                    mProgress
            );
            canvas.drawCircle(strokeWidth + mPadding + iconRadius, getHeight() / 2f, dotRadius, mDotPaint);
        } else {
            canvas.drawRoundRect(
                    new RectF(halfStroke,
                            halfStroke,
                            getWidth() - halfStroke,
                            getHeight() - halfStroke),
                    rounding,
                    rounding,
                    mDotPaint
            );
        }

        float textX;
        if (mShowIcons) {
            textX = lerp(strokeWidth + mPadding + mClear.getIntrinsicWidth() + mPadding,
                    strokeWidth + mPadding * 2f,
                    mProgress);
        } else {
            textX = strokeWidth + mPadding * 2f;
        }

        Integer selectedColor = mSelectedTextColor;
        if (selectedColor != null && selectedColor != 0 && mProgress > 0) {
            mTextPaint.setColor(ColorUtils.blendARGB(mDefaultTextColor, selectedColor, mProgress));
        } else {
            mTextPaint.setColor(mDefaultTextColor);
        }

        canvas.save();
        canvas.translate(textX, (getHeight() - mTextLayout.getHeight()) / 2f);
        mTextLayout.draw(canvas);
        canvas.restore();

        // Clear icon
        if (mShowIcons && mProgress > 0f) {
            canvas.save();
            canvas.translate(getWidth() - strokeWidth - mPadding - iconRadius, getHeight() / 2f);
            canvas.scale(mProgress, mProgress);
            mClear.draw(canvas);
            canvas.restore();
        }

        mTouchFeedback.draw(canvas);
    }


    private StaticLayout createLayout(int textWidth) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return StaticLayout.Builder.obtain(mText, 0, mText.length(), mTextPaint, textWidth).build();
        } else {
            return new StaticLayout(mText, mTextPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true);
        }
    }

    /**
     * Linearly interpolate between two values.
     */
    public float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            mProgress = 1f;
        } else {
            mProgress = 0f;
        }
    }

    @Override
    public boolean isChecked() {
        return mProgress == 1f;
    }

    @Override
    public void toggle() {
        if (mProgress == 0f) {
            mProgress = 1f;
        } else {
            mProgress = 0f;
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || who == mTouchFeedback;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        mTouchFeedback.setState(getDrawableState());
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        mTouchFeedback.jumpToCurrentState();
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTouchFeedback.setHotspot(x, y);
        }
    }

    public void animateCheckedAndInvoke(boolean checked) {
        float newProgress;
        if (checked) {
            newProgress = 1f;
        } else {
            newProgress = 0f;
        }
        if (newProgress != mProgress) {
            if (mProgressAnimator != null)
                mProgressAnimator.cancel();
            mProgressAnimator = ValueAnimator.ofFloat(mProgress, newProgress);
            mProgressAnimator.addUpdateListener(animation -> {
                mProgress = (float) animation.getAnimatedValue();
                invalidate();
            });

            mProgressAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgress = newProgress;
                }
            });
            mProgressAnimator.setInterpolator(mInterp);
            mProgressAnimator.setDuration(checked ? SELECTING_DURATION : DESELECTING_DURATION);
            mProgressAnimator.start();
        }
    }
}
