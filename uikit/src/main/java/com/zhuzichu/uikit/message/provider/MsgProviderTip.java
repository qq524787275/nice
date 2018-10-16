package com.zhuzichu.uikit.message.provider;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public class MsgProviderTip extends BaseItemProvider<IMMessage, BaseViewHolder> {
    @Override
    public int viewType() {
        return MessageMultipItemAdapter.MSG_TIP;
    }

    @Override
    public int layout() {
        return R.layout.item_message_tip;
    }

    @Override
    public void convert(BaseViewHolder helper, IMMessage data, int position) {
        helper.setText(R.id.item_tip_label, "tip消息");
    }
}
