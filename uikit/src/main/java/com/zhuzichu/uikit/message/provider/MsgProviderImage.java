package com.zhuzichu.uikit.message.provider;

import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;

public class MsgProviderImage extends MsgProviderThumbBase {

    @Override
    protected String thumbFromSourceFile(String path) {
        return path;
    }

    @Override
    int getContentResId() {
        return R.layout.item_message_image;
    }

    @Override
    public int viewType() {
        return MessageMultipItemAdapter.MSG_IMAGE;
    }
}
