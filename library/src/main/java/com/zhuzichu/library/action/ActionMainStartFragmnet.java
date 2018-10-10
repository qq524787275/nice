package com.zhuzichu.library.action;

import com.google.common.base.Optional;
import com.zhuzichu.library.R;
import com.zhuzichu.library.base.BaseFragment;


public class ActionMainStartFragmnet {
    public BaseFragment data;
    public Optional<Animations> animations;

    /**
     * @AnimatorRes @AnimRes int targetFragmentEnter,
     * @AnimatorRes @AnimRes int currentFragmentPopExit,
     * @AnimatorRes @AnimRes int currentFragmentPopEnter,
     * @AnimatorRes @AnimRes int targetFragmentExit
     */
    public static class Animations {
        public int targetFragmentEnter;
        public int currentFragmentPopExit;
        public int currentFragmentPopEnter;
        public int targetFragmentExit;
    }


    public ActionMainStartFragmnet(BaseFragment data) {
        this(data, null);
    }

    public ActionMainStartFragmnet(BaseFragment data, Animations animations) {
        this.data = data;
        this.animations = Optional.fromNullable(animations);
    }

    public Optional<Animations> getAnimations() {
        return animations;
    }

    public void setAnimations(Animations animations) {
        this.animations = Optional.fromNullable(animations);
    }

    public BaseFragment getData() {
        return data;
    }

    public void setData(BaseFragment data) {
        this.data = data;
    }

    public static Animations getNoAnimations() {
        Animations animations = new Animations();
        animations.targetFragmentEnter = 0;
        animations.currentFragmentPopExit = 0;
        animations.currentFragmentPopEnter = 0;
        animations.targetFragmentExit = 0;
        return animations;
    }

    public static Animations getModalAnimations() {
        Animations animations = new Animations();
        animations.targetFragmentEnter = R.anim.v_fragment_enter;
        animations.currentFragmentPopExit = 0;
        animations.currentFragmentPopEnter = 0;
        animations.targetFragmentExit = R.anim.v_fragment_exit;
        return animations;
    }
}
