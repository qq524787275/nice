package com.zhuzichu.uikit.preview.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.widget.OnClickListener;

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
        Log.i(TAG, "onEnterAnimationEnd: --------------------");
        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                Toast.makeText(_mActivity, "执行了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
