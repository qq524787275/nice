package com.zhuzichu.uikit.preview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Priority;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.library.comment.glide.GlideApp;
import com.zhuzichu.library.utils.FileUtils;
import com.zhuzichu.uikit.R;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class PreViewItemFragment extends BaseFragment {
    protected IMMessage message;
    protected ImageViewTouch img;
    protected ViewGroup container;

    public int getContainer() {
        return -1;
    }

    public interface Extra {
        String EXTRA_MESSGAE = "extra_messgae";
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_item_preview;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        paseData();
        img = rootView.findViewById(R.id.img);
        container = rootView.findViewById(R.id.container);
        if (getContainer() != -1)
            getLayoutInflater().inflate(getContainer(), container);
        initView();
     }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
    }

    private void paseData() {
        message = (IMMessage) getArguments().getSerializable(Extra.EXTRA_MESSGAE);
    }

    private void initView() {
        FileAttachment attachment = (FileAttachment) message.getAttachment();
        img.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        if (FileUtils.isGif(attachment.getExtension())) {
            GlideApp.with(img)
                    .asGif()
                    .load(attachment.getThumbPath())
                    .override(Nice.w, Nice.h)
                    .priority(Priority.HIGH)
                    .into(img);
        } else {
            GlideApp.with(img)
                    .load(attachment.getThumbPath())
                    .override(Nice.w, Nice.h)
                    .priority(Priority.HIGH)
                    .fitCenter()
                    .into(img);
        }
    }
}
