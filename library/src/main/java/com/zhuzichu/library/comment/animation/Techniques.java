
/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 daimajia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.zhuzichu.library.comment.animation;


import com.zhuzichu.library.comment.animation.attention.BounceAnimator;
import com.zhuzichu.library.comment.animation.attention.FlashAnimator;
import com.zhuzichu.library.comment.animation.attention.PulseAnimator;
import com.zhuzichu.library.comment.animation.attention.RubberBandAnimator;
import com.zhuzichu.library.comment.animation.attention.ShakeAnimator;
import com.zhuzichu.library.comment.animation.attention.StandUpAnimator;
import com.zhuzichu.library.comment.animation.attention.SwingAnimator;
import com.zhuzichu.library.comment.animation.attention.TadaAnimator;
import com.zhuzichu.library.comment.animation.attention.WaveAnimator;
import com.zhuzichu.library.comment.animation.attention.WobbleAnimator;
import com.zhuzichu.library.comment.animation.bouncing_entrances.BounceInAnimator;
import com.zhuzichu.library.comment.animation.bouncing_entrances.BounceInDownAnimator;
import com.zhuzichu.library.comment.animation.bouncing_entrances.BounceInLeftAnimator;
import com.zhuzichu.library.comment.animation.bouncing_entrances.BounceInRightAnimator;
import com.zhuzichu.library.comment.animation.bouncing_entrances.BounceInUpAnimator;
import com.zhuzichu.library.comment.animation.fading_entrances.FadeInAnimator;
import com.zhuzichu.library.comment.animation.fading_entrances.FadeInDownAnimator;
import com.zhuzichu.library.comment.animation.fading_entrances.FadeInLeftAnimator;
import com.zhuzichu.library.comment.animation.fading_entrances.FadeInRightAnimator;
import com.zhuzichu.library.comment.animation.fading_entrances.FadeInUpAnimator;
import com.zhuzichu.library.comment.animation.fading_exits.FadeOutAnimator;
import com.zhuzichu.library.comment.animation.fading_exits.FadeOutDownAnimator;
import com.zhuzichu.library.comment.animation.fading_exits.FadeOutLeftAnimator;
import com.zhuzichu.library.comment.animation.fading_exits.FadeOutRightAnimator;
import com.zhuzichu.library.comment.animation.fading_exits.FadeOutUpAnimator;
import com.zhuzichu.library.comment.animation.flippers.FlipInXAnimator;
import com.zhuzichu.library.comment.animation.flippers.FlipInYAnimator;
import com.zhuzichu.library.comment.animation.flippers.FlipOutXAnimator;
import com.zhuzichu.library.comment.animation.flippers.FlipOutYAnimator;
import com.zhuzichu.library.comment.animation.rotating_entrances.RotateInAnimator;
import com.zhuzichu.library.comment.animation.rotating_entrances.RotateInDownLeftAnimator;
import com.zhuzichu.library.comment.animation.rotating_entrances.RotateInDownRightAnimator;
import com.zhuzichu.library.comment.animation.rotating_entrances.RotateInUpLeftAnimator;
import com.zhuzichu.library.comment.animation.rotating_entrances.RotateInUpRightAnimator;
import com.zhuzichu.library.comment.animation.rotating_exits.RotateOutAnimator;
import com.zhuzichu.library.comment.animation.rotating_exits.RotateOutDownLeftAnimator;
import com.zhuzichu.library.comment.animation.rotating_exits.RotateOutDownRightAnimator;
import com.zhuzichu.library.comment.animation.rotating_exits.RotateOutUpLeftAnimator;
import com.zhuzichu.library.comment.animation.rotating_exits.RotateOutUpRightAnimator;
import com.zhuzichu.library.comment.animation.sliders.SlideInDownAnimator;
import com.zhuzichu.library.comment.animation.sliders.SlideInLeftAnimator;
import com.zhuzichu.library.comment.animation.sliders.SlideInRightAnimator;
import com.zhuzichu.library.comment.animation.sliders.SlideInUpAnimator;
import com.zhuzichu.library.comment.animation.sliders.SlideOutDownAnimator;
import com.zhuzichu.library.comment.animation.sliders.SlideOutLeftAnimator;
import com.zhuzichu.library.comment.animation.sliders.SlideOutRightAnimator;
import com.zhuzichu.library.comment.animation.sliders.SlideOutUpAnimator;
import com.zhuzichu.library.comment.animation.specials.RollInAnimator;
import com.zhuzichu.library.comment.animation.specials.RollOutAnimator;
import com.zhuzichu.library.comment.animation.zooming_entrances.ZoomInAnimator;
import com.zhuzichu.library.comment.animation.zooming_entrances.ZoomInDownAnimator;
import com.zhuzichu.library.comment.animation.zooming_entrances.ZoomInLeftAnimator;
import com.zhuzichu.library.comment.animation.zooming_entrances.ZoomInRightAnimator;
import com.zhuzichu.library.comment.animation.zooming_entrances.ZoomInUpAnimator;
import com.zhuzichu.library.comment.animation.zooming_exits.ZoomOutAnimator;
import com.zhuzichu.library.comment.animation.zooming_exits.ZoomOutDownAnimator;
import com.zhuzichu.library.comment.animation.zooming_exits.ZoomOutLeftAnimator;
import com.zhuzichu.library.comment.animation.zooming_exits.ZoomOutRightAnimator;
import com.zhuzichu.library.comment.animation.zooming_exits.ZoomOutUpAnimator;

public enum Techniques {


    Flash(FlashAnimator.class),
    Pulse(PulseAnimator.class),
    RubberBand(RubberBandAnimator.class),
    Shake(ShakeAnimator.class),
    Swing(SwingAnimator.class),
    Wobble(WobbleAnimator.class),
    Bounce(BounceAnimator.class),
    Tada(TadaAnimator.class),
    StandUp(StandUpAnimator.class),
    Wave(WaveAnimator.class),

    RollIn(RollInAnimator.class),
    RollOut(RollOutAnimator.class),

    BounceIn(BounceInAnimator.class),
    BounceInDown(BounceInDownAnimator.class),
    BounceInLeft(BounceInLeftAnimator.class),
    BounceInRight(BounceInRightAnimator.class),
    BounceInUp(BounceInUpAnimator.class),

    FadeIn(FadeInAnimator.class),
    FadeInUp(FadeInUpAnimator.class),
    FadeInDown(FadeInDownAnimator.class),
    FadeInLeft(FadeInLeftAnimator.class),
    FadeInRight(FadeInRightAnimator.class),

    FadeOut(FadeOutAnimator.class),
    FadeOutDown(FadeOutDownAnimator.class),
    FadeOutLeft(FadeOutLeftAnimator.class),
    FadeOutRight(FadeOutRightAnimator.class),
    FadeOutUp(FadeOutUpAnimator.class),

    FlipInX(FlipInXAnimator.class),
    FlipOutX(FlipOutXAnimator.class),
    FlipInY(FlipInYAnimator.class),
    FlipOutY(FlipOutYAnimator.class),
    RotateIn(RotateInAnimator.class),
    RotateInDownLeft(RotateInDownLeftAnimator.class),
    RotateInDownRight(RotateInDownRightAnimator.class),
    RotateInUpLeft(RotateInUpLeftAnimator.class),
    RotateInUpRight(RotateInUpRightAnimator.class),

    RotateOut(RotateOutAnimator.class),
    RotateOutDownLeft(RotateOutDownLeftAnimator.class),
    RotateOutDownRight(RotateOutDownRightAnimator.class),
    RotateOutUpLeft(RotateOutUpLeftAnimator.class),
    RotateOutUpRight(RotateOutUpRightAnimator.class),

    SlideInLeft(SlideInLeftAnimator.class),
    SlideInRight(SlideInRightAnimator.class),
    SlideInUp(SlideInUpAnimator.class),
    SlideInDown(SlideInDownAnimator.class),

    SlideOutLeft(SlideOutLeftAnimator.class),
    SlideOutRight(SlideOutRightAnimator.class),
    SlideOutUp(SlideOutUpAnimator.class),
    SlideOutDown(SlideOutDownAnimator.class),

    ZoomIn(ZoomInAnimator.class),
    ZoomInDown(ZoomInDownAnimator.class),
    ZoomInLeft(ZoomInLeftAnimator.class),
    ZoomInRight(ZoomInRightAnimator.class),
    ZoomInUp(ZoomInUpAnimator.class),

    ZoomOut(ZoomOutAnimator.class),
    ZoomOutDown(ZoomOutDownAnimator.class),
    ZoomOutLeft(ZoomOutLeftAnimator.class),
    ZoomOutRight(ZoomOutRightAnimator.class),
    ZoomOutUp(ZoomOutUpAnimator.class);



    private Class animatorClazz;

    private Techniques(Class clazz) {
        animatorClazz = clazz;
    }

    public BaseViewAnimator getAnimator() {
        try {
            return (BaseViewAnimator) animatorClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
