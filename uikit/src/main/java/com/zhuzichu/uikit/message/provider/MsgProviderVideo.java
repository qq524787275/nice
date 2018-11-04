package com.zhuzichu.uikit.message.provider;

import android.support.v7.app.AppCompatActivity;

import com.netease.nimlib.sdk.msg.model.IMMessage;

import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;
import com.zhuzichu.uikit.preview.PreViewActivity;

public class MsgProviderVideo extends MsgProviderThumbBase {

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

    @Override
    protected void onItemClick(IMMessage msg, MessageMultipItemAdapter.DataBindingViewHolder holder) {
        AppCompatActivity activity = (AppCompatActivity) holder.itemView.getContext();
        PreViewActivity.startActivity(activity, msg, holder.itemView.findViewById(R.id.msg_thumbnail));
    }
}
