package com.zhuzichu.uikit.preview.fragment;


import android.os.Bundle;

import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by wb.zhuzichu18 on 2018/11/5.
 */
public class PreviewImageFragment extends PreViewItemFragment {
    private static final String TAG = "PreviewImageFragment";

    public static PreViewItemFragment newInstance(IMMessage message) {

        Bundle args = new Bundle();
        args.putSerializable(Extra.EXTRA_MESSGAE, message);
        PreViewItemFragment fragment = new PreviewImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        img.setSingleTapListener(() -> getActivity().onBackPressed());
    }
}
