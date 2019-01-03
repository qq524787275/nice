package com.zhuzichu.uikit.message.provider;

import android.widget.TextView;

import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.utils.TimeUtils;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.activity.WatchPicAndVideoActivity;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;

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
        WatchPicAndVideoActivity.startActivity(mContext, msg);
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
