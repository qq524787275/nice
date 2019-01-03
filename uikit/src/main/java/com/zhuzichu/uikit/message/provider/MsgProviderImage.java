package com.zhuzichu.uikit.message.provider;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.activity.WatchPicAndVideoActivity;
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

    @Override
    protected void onItemClick(IMMessage msg, MessageMultipItemAdapter.DataBindingViewHolder holder) {
        WatchPicAndVideoActivity.startActivity(mContext, msg);
    }
}
