package com.zhuzichu.library.widget.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wb.zhuzichu18 on 2018/10/12.
 */
public class Rotate extends Transition {

    private static String PROP_ROTATION = "iosched:rotate:rotation";

    private final String[] TRANSITION_PROPERTIES = new String[]{PROP_ROTATION};

    public Rotate() {
    }

    public Rotate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Nullable
    @Override
    public String[] getTransitionProperties() {
        return TRANSITION_PROPERTIES;
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Nullable
    @Override
    public Animator createAnimator(@NonNull ViewGroup sceneRoot, @Nullable TransitionValues startValues, @Nullable TransitionValues endValues) {
        if (startValues == null || endValues == null) return null;

        Float startRotation = (Float) startValues.values.get(PROP_ROTATION);
        Float endRotation = (Float) endValues.values.get(PROP_ROTATION);

        if (startRotation == endRotation) return null;
        View view = endValues.view;
        view.setPivotX(view.getWidth() / 2f);
        view.setPivotY(view.getHeight() / 2f);

        return ObjectAnimator.ofFloat(endValues.view, View.ROTATION, startRotation, endRotation);
    }


    public void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (view == null || view.getWidth() <= 0 || view.getHeight() <= 0) return;
        transitionValues.values.put(PROP_ROTATION, view.getRotation());
    }
}
