package com.zhuzichu.uikit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.zhuzichu.library.utils.DensityUtils;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.utils.UserInfoUtils;

/**
 * Created by wb.zhuzichu18 on 2018/11/23.
 */
public class HeadImageView extends FrameLayout {

    private ImageView avatar;
    //尺寸大小
    private int size;

    public HeadImageView(@NonNull Context context) {
        this(context, null);
    }

    public HeadImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_header_image, this);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        avatar = findViewById(R.id.avatar);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HeadImageView);
        //默认尺寸为中号 总共有large-0，medium-1，small-2
        int sizeValue = typedArray.getInt(R.styleable.HeadImageView_avatar_size, 1);
        switch (sizeValue) {
            case 0:
                size = DensityUtils.dip2px(getContext(), 60);
                break;
            case 1:
                size = DensityUtils.dip2px(getContext(), 50);
                break;
            case 2:
                size = DensityUtils.dip2px(getContext(), 40);
                break;
            default:
                size = DensityUtils.dip2px(getContext(), 40);
                break;
        }
        typedArray.recycle();
    }

    private void loadTeamGroupAvatar(Team team) {
        doLoadImage(team != null ? team.getIcon() : null, R.mipmap.avatar_group, size);
    }

    private void loadAvatar(String account) {
        final UserInfo userInfo = UserInfoUtils.getUserInfo(account).get();
        doLoadImage(userInfo != null ? userInfo.getAvatar() : null, R.mipmap.avatar_default, size);
    }

    /**
     * ImageLoader异步加载
     */
    private void doLoadImage(final String url, final int defaultResId, final int thumbSize) {
        RequestOptions requestOptions = RequestOptions.bitmapTransform(new CircleCrop())
                .centerCrop()
                .placeholder(defaultResId)
                .error(defaultResId)
                .override(thumbSize, thumbSize);
        Glide.with(getContext().getApplicationContext()).asBitmap()
                .load(url)
                .apply(requestOptions)
                .into(avatar);
    }
}