package com.zhuzichu.uikit.message.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.library.comment.glide.GlideApp;
import com.zhuzichu.library.utils.FileUtils;
import com.zhuzichu.uikit.R;

import java.io.File;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class WatchPicFragment extends BaseFragment {

    public interface Extra {
        String EXTRA_MESSGAE = "extra_messgae";
    }

    private ImageViewTouch mImage;
    private IMMessage message;

    public static WatchPicFragment newInstance(IMMessage message) {

        Bundle args = new Bundle();
        args.putSerializable(Extra.EXTRA_MESSGAE, message);
        WatchPicFragment fragment = new WatchPicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_watch_pic;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mImage = rootView.findViewById(R.id.image);
        message = (IMMessage) getArguments().getSerializable(Extra.EXTRA_MESSGAE);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        FileAttachment attachment = (FileAttachment) message.getAttachment();
        mImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        String path = attachment.getThumbPath();
        RequestBuilder builder;
        if (FileUtils.isGif(attachment.getExtension())) {
            builder = GlideApp.with(getContext().getApplicationContext()).asGif().load(new File(path));
        } else {
            RequestOptions options = new RequestOptions()
                    .override(Nice.w, Nice.h)
                    .priority(Priority.HIGH)
                    .fitCenter();

            builder = GlideApp.with(getContext().getApplicationContext())
                    .asBitmap()
                    .apply(options)
                    .load(new File(path));
        }
        builder.into(mImage);
        mImage.setSingleTapListener(() -> getActivity().onBackPressed());
    }
}