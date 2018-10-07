package com.zhuzichu.library.comment.glide;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.base.Optional;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
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

    public static void loadAvatar(ImageView ivHeader, TextView tvName, String contactId, SessionTypeEnum sessionType) {
        if (sessionType == SessionTypeEnum.P2P || sessionType == null) {
            Optional<NimUserInfo> userInfoOptional = Optional.fromNullable(NIMClient.getService(UserService.class).getUserInfo(contactId));
            if (userInfoOptional.isPresent()) {
                NimUserInfo user = userInfoOptional.get();
                if (tvName != null)
                    tvName.setText(user.getName());
                GlideAdapter.loadUserAvatar(ivHeader, user.getAvatar());
            }
        } else if (sessionType == SessionTypeEnum.Team) {
            Optional<Team> teamOptional = Optional.fromNullable(NIMClient.getService(TeamService.class).queryTeamBlock(contactId));
            if (teamOptional.isPresent()) {
                Team team = teamOptional.get();
                if (tvName != null)
                    tvName.setText(team.getName());
                GlideAdapter.loadGroupAvatar(ivHeader, team.getIcon());
            }
        }
    }

    public static void loadAvatar(ImageView ivHeader, String contactId, SessionTypeEnum sessionType) {
        loadAvatar(ivHeader, null, contactId, sessionType);
    }

    public static void loadAvatar(ImageView ivHeader, String contactId) {
        loadAvatar(ivHeader, null, contactId, null);
    }
}