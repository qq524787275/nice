package com.zhuzichu.uikit.message.provider;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;
import com.zhuzichu.uikit.utils.TeamNotificationUtils;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public class MsgProviderNotification extends BaseItemProvider<IMMessage, BaseViewHolder> {
    @Override
    public int viewType() {
        return MessageMultipItemAdapter.MSG_NOTIFICATION;
    }

    @Override
    public int layout() {
        return R.layout.item_message_notification;
    }

    @Override
    public void convert(BaseViewHolder helper, IMMessage data, int position) {
        helper.setText(R.id.item_notification_label, TeamNotificationUtils.getDisplayText(data));
    }
}
