package com.zhuzichu.uikit.message.provider;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import com.zhuzichu.library.utils.TimeUtils;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;
import com.zhuzichu.uikit.preview.PreViewActivity;

public class MsgProviderVideo extends MsgProviderThumbBase {
    private TextView mDuration;

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

    @Override
    void inflateContentView() {
        super.inflateContentView();
        mDuration = itemView.findViewById(R.id.duration);
    }

    @Override
    void refreshView() {
        super.refreshView();
        VideoAttachment attachment = (VideoAttachment) message.getAttachment();
        mDuration.setText(TimeUtils.secToTime((int) (attachment.getDuration() / 1000)));
    }
}
