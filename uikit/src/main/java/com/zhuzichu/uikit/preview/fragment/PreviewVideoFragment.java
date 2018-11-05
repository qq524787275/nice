package com.zhuzichu.uikit.preview.fragment;


import android.os.Bundle;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.uikit.R;

/**
 * Created by wb.zhuzichu18 on 2018/11/5.
 */
public class PreviewVideoFragment extends PreViewItemFragment {

    public static PreViewItemFragment newInstance(IMMessage message) {

        Bundle args = new Bundle();
        args.putSerializable(Extra.EXTRA_MESSGAE, message);
        PreViewItemFragment fragment = new PreviewVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        img.setScaleEnabled(false);
    }

    @Override
    public int getContainer() {
        return R.layout.layout_preview_video_control;
    }
}
