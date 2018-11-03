package com.zhuzichu.uikit.message.provider;

import com.zhuzichu.library.base.BaseFragment;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;

public class MsgProviderVideo extends MsgProviderThumbBase {
    public MsgProviderVideo(BaseFragment fragment) {
        super(fragment);
    }

    @Override
    protected String thumbFromSourceFile(String path) {
        return path;
    }

    @Override
    int getContentResId() {
        return R.layout.item_message_video;
    }

    @Override
    public int viewType() {
        return MessageMultipItemAdapter.MSG_VIDEO;
    }
}
