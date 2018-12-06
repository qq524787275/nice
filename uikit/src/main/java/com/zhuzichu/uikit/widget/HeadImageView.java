package com.zhuzichu.uikit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.zhuzichu.library.utils.DensityUtils;
import com.zhuzichu.library.widget.CircleImageView;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.utils.UserInfoUtils;

/**
 * Created by wb.zhuzichu18 on 2018/11/23.
 */
public class HeadImageView extends FrameLayout {

    private CircleImageView avatar;
    //尺寸大小
    private int w;
    private int h;

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
        avatar = findViewById(R.id.view_avatar);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HeadImageView);
        w = typedArray.getLayoutDimension(R.styleable.HeadImageView_android_layout_width, DensityUtils.dip2px(getContext(), 50));
        h = typedArray.getLayoutDimension(R.styleable.HeadImageView_android_layout_height, DensityUtils.dip2px(getContext(), 50));
        typedArray.recycle();

        ViewGroup.LayoutParams avatarParams = avatar.getLayoutParams();
        avatarParams.width = w;
        avatarParams.height = h;
    }

    public void loadTeamGroupAvatar(Team team) {
        doLoadImage(team != null ? team.getIcon() : null, R.mipmap.avatar_group);
    }

    public void loadAvatar(String account) {
        final UserInfo userInfo = UserInfoUtils.getUserInfo(account).get();
        doLoadImage(userInfo != null ? userInfo.getAvatar() : null, R.mipmap.avatar_default);
    }

    public void loadAvatarByUrl(String url) {
        doLoadImage(url, R.mipmap.avatar_default);
    }

    /**
     * ImageLoader异步加载
     */
    private void doLoadImage(final String url, final int defaultResId) {
        RequestOptions requestOptions = RequestOptions.overrideOf(w, h)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(defaultResId)
                .error(defaultResId);

        Glide.with(getContext().getApplicationContext())
                .load(url)
                .apply(requestOptions)
                .into(avatar);
    }

    @BindingAdapter("loadAvatarByUrl")
    public static void loadAvatarByUrl(HeadImageView headImageView, String url) {
        headImageView.loadAvatarByUrl(url);
    }
}