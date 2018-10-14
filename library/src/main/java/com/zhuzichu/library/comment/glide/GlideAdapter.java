package com.zhuzichu.library.comment.glide;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

public class GlideAdapter {
    private static RequestOptions mCircleOptions = RequestOptions.bitmapTransform(new CircleCrop());

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