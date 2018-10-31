package com.zhuzichu.library.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.zhuzichu.library.R;

public class StateLayout extends FrameLayout {

    //animation duration
    private int mDuration = 0;

    //radius
    private float mRadius = 0;
    private boolean mRound;

    //stroke
    private float mStrokeDashWidth = 0;
    private float mStrokeDashGap = 0;
    private int mNormalStrokeWidth = 0;
    private int mPressedStrokeWidth = 0;
    private int mUnableStrokeWidth = 0;
    private int mNormalStrokeColor = 0;
    private int mPressedStrokeColor = 0;
    private int mUnableStrokeColor = 0;

    //background color
    private int mNormalBackgroundColor = 0;
    private int mPressedBackgroundColor = 0;
    private int mUnableBackgroundColor = 0;

    private GradientDrawable mNormalBackground;
    private GradientDrawable mPressedBackground;
    private GradientDrawable mUnableBackground;

    private int[][] states;

    StateListDrawable mStateBackground;
    StateListDrawable mStateForeground;

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    private void setup(AttributeSet attrs) {

        states = new int[4][];

        Drawable drawable = getBackground();
        if (drawable != null && drawable instanceof StateListDrawable) {
            mStateBackground = (StateListDrawable) drawable;
        } else {
            mStateBackground = new StateListDrawable();
        }

        Drawable foreground = getForeground();
        if (foreground != null && foreground instanceof StateListDrawable) {
            mStateForeground = (StateListDrawable) foreground;
        } else {
            mStateForeground = new StateListDrawable();
        }

        mNormalBackground = new GradientDrawable();
        mPressedBackground = new GradientDrawable();
        mUnableBackground = new GradientDrawable();

        //pressed, focused, normal, unable
        states[0] = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[3] = new int[]{-android.R.attr.state_enabled};
        states[2] = new int[]{android.R.attr.state_enabled};

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StateLayout);

        //set animation duration
        mDuration = a.getInteger(R.styleable.StateLayout_sl_animationDuration, mDuration);
        mStateBackground.setEnterFadeDuration(mDuration);
        mStateBackground.setExitFadeDuration(mDuration);

        //set background color
        mNormalBackgroundColor = a.getColor(R.styleable.StateLayout_sl_normalBackgroundColor, 0);
        mPressedBackgroundColor = a.getColor(R.styleable.StateLayout_sl_pressedBackgroundColor, 0);
        mUnableBackgroundColor = a.getColor(R.styleable.StateLayout_sl_unableBackgroundColor, 0);
        mNormalBackground.setColor(mNormalBackgroundColor);
        mPressedBackground.setColor(mPressedBackgroundColor);
        mUnableBackground.setColor(mUnableBackgroundColor);

        //set radius
        mRadius = a.getDimensionPixelSize(R.styleable.StateLayout_sl_radius, 0);
        mRound = a.getBoolean(R.styleable.StateLayout_sl_round, false);
        mNormalBackground.setCornerRadius(mRadius);
        mPressedBackground.setCornerRadius(mRadius);
        mUnableBackground.setCornerRadius(mRadius);

        //set stroke
        mStrokeDashWidth = a.getDimensionPixelSize(R.styleable.StateLayout_sl_strokeDashWidth, 0);
        mStrokeDashGap = a.getDimensionPixelSize(R.styleable.StateLayout_sl_strokeDashWidth, 0);
        mNormalStrokeWidth = a.getDimensionPixelSize(R.styleable.StateLayout_sl_normalStrokeWidth, 0);
        mPressedStrokeWidth = a.getDimensionPixelSize(R.styleable.StateLayout_sl_pressedStrokeWidth, 0);
        mUnableStrokeWidth = a.getDimensionPixelSize(R.styleable.StateLayout_sl_unableStrokeWidth, 0);
        mNormalStrokeColor = a.getColor(R.styleable.StateLayout_sl_normalStrokeColor, 0);
        mPressedStrokeColor = a.getColor(R.styleable.StateLayout_sl_pressedStrokeColor, 0);
        mUnableStrokeColor = a.getColor(R.styleable.StateLayout_sl_unableStrokeColor, 0);
        setStroke();

        //set background
        mStateForeground.addState(states[0], mPressedBackground);
        mStateForeground.addState(states[1], mPressedBackground);
        mStateBackground.addState(states[3], mUnableBackground);
        mStateBackground.addState(states[2], mNormalBackground);

        setBackgroundDrawable(mStateBackground);
        setForeground(mStateForeground);
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setRound(mRound);
    }

    /****************** stroke color *********************/

    public void setNormalStrokeColor(@ColorInt int normalStrokeColor) {
        this.mNormalStrokeColor = normalStrokeColor;
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
    }

    public void setPressedStrokeColor(@ColorInt int pressedStrokeColor) {
        this.mPressedStrokeColor = pressedStrokeColor;
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
    }

    public void setUnableStrokeColor(@ColorInt int unableStrokeColor) {
        this.mUnableStrokeColor = unableStrokeColor;
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    public void setStateStrokeColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable) {
        mNormalStrokeColor = normal;
        mPressedStrokeColor = pressed;
        mUnableStrokeColor = unable;
        setStroke();
    }

    /****************** stroke width *********************/

    public void setNormalStrokeWidth(int normalStrokeWidth) {
        this.mNormalStrokeWidth = normalStrokeWidth;
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
    }

    public void setPressedStrokeWidth(int pressedStrokeWidth) {
        this.mPressedStrokeWidth = pressedStrokeWidth;
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
    }

    public void setUnableStrokeWidth(int unableStrokeWidth) {
        this.mUnableStrokeWidth = unableStrokeWidth;
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    public void setStateStrokeWidth(int normal, int pressed, int unable) {
        mNormalStrokeWidth = normal;
        mPressedStrokeWidth = pressed;
        mUnableStrokeWidth = unable;
        setStroke();
    }

    public void setStrokeDash(float strokeDashWidth, float strokeDashGap) {
        this.mStrokeDashWidth = strokeDashWidth;
        this.mStrokeDashGap = strokeDashWidth;
        setStroke();
    }

    private void setStroke() {
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    private void setStroke(GradientDrawable mBackground, int mStrokeColor, int mStrokeWidth) {
        mBackground.setStroke(mStrokeWidth, mStrokeColor, mStrokeDashWidth, mStrokeDashGap);
    }

    /********************   radius  *******************************/

    public void setRadius(@FloatRange(from = 0) float radius) {
        this.mRadius = radius;
        mNormalBackground.setCornerRadius(mRadius);
        mPressedBackground.setCornerRadius(mRadius);
        mUnableBackground.setCornerRadius(mRadius);
    }

    public void setRound(boolean round) {
        this.mRound = round;
        int height = getMeasuredHeight();
        if (mRound) {
            setRadius(height / 2f);
        }
    }

    public void setRadius(float[] radii) {
        mNormalBackground.setCornerRadii(radii);
        mPressedBackground.setCornerRadii(radii);
        mUnableBackground.setCornerRadii(radii);
    }

    /********************  background color  **********************/

    public void setStateBackgroundColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable) {
        mNormalBackgroundColor = normal;
        mPressedBackgroundColor = pressed;
        mUnableBackgroundColor = unable;
        mNormalBackground.setColor(mNormalBackgroundColor);
        mPressedBackground.setColor(mPressedBackgroundColor);
        mUnableBackground.setColor(mUnableBackgroundColor);
    }

    public void setNormalBackgroundColor(@ColorInt int normalBackgroundColor) {
        this.mNormalBackgroundColor = normalBackgroundColor;
        mNormalBackground.setColor(mNormalBackgroundColor);
    }

    public void setPressedBackgroundColor(@ColorInt int pressedBackgroundColor) {
        this.mPressedBackgroundColor = pressedBackgroundColor;
        mPressedBackground.setColor(mPressedBackgroundColor);
    }

    public void setUnableBackgroundColor(@ColorInt int unableBackgroundColor) {
        this.mUnableBackgroundColor = unableBackgroundColor;
        mUnableBackground.setColor(mUnableBackgroundColor);
    }

    /*******************alpha animation duration********************/
    public void setAnimationDuration(@IntRange(from = 0) int duration) {
        this.mDuration = duration;
        mStateBackground.setEnterFadeDuration(mDuration);
    }

    @BindingAdapter("sl_normalBackgroundColor")
    public static void setNormalBackgroundColor(StateLayout stateLayout, int color) {
        stateLayout.setNormalBackgroundColor(color);
    }

    @BindingAdapter("sl_pressedBackgroundColor")
    public static void setPressedBackgroundColor(StateLayout stateLayout, int color) {
        stateLayout.setPressedBackgroundColor(color);
    }
}
