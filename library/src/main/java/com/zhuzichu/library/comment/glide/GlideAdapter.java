package com.zhuzichu.library.comment.glide;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.zhuzichu.library.R;

public class GlideAdapter {
    private static RequestOptions mCircleOptions = RequestOptions.bitmapTransform(new CircleCrop());

    @BindingAdapter({"avatarUserUrl"})
    public static void loadUserAvatar(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(!TextUtils.isEmpty(url) ? url : R.mipmap.avatar_default)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(mCircleOptions).into(imageView);

    }

    @BindingAdapter({"avatarGroupUrl"})
    public static void loadGroupAvatar(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(!TextUtils.isEmpty(url) ? url : R.mipmap.avatar_group)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(mCircleOptions).into(imageView);
    }

    @BindingAdapter({"imageUrl"})
    public static void imageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    @BindingAdapter({"imageUrl"})
    public static void imageUrl(ImageView imageView, Integer url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }
}
