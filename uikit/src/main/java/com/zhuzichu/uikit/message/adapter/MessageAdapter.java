package com.zhuzichu.uikit.message.adapter;

import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.base.BaseDataBindingAdapter;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.utils.TimeUtils;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.adapter.ImageAdapter;
import com.zhuzichu.uikit.databinding.ItemMessageBinding;

import java.util.ArrayList;

public class MessageAdapter extends BaseDataBindingAdapter<IMMessage, ItemMessageBinding> {

    public MessageAdapter() {
        super(R.layout.item_message, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemMessageBinding binding, IMMessage item) {
        binding.setColor(ColorManager.getInstance().color);
        //加载头像
        ImageAdapter.loadAvatar(binding.msgAvatar, item.getFromAccount());
        //刷新时间
        refreshMsgTime(helper, binding, item);
        //判断消息是否是自己的
        if (item.getFromAccount().equals(Nice.getAccount())) {
            //刷新自己的消息
            refreshMineMsg(helper, binding, item);
        } else {
            //刷新他人的消息
            refreshOtherMsg(helper, binding, item);
        }
        //添加点击事件
        helper.addOnClickListener(R.id.msg_fail);

        switch (item.getMsgType()) {
            case text:
                binding.msgText.setText(item.getContent());
                break;
        }
    }

    /**
     * 刷新别人发的消息
     *
     * @param helper
     * @param binding
     * @param item
     */
    private void refreshOtherMsg(BaseViewHolder helper, ItemMessageBinding binding, IMMessage item) {
        binding.msgLoading.setVisibility(View.GONE);
        binding.msgFail.setVisibility(View.GONE);
        binding.msgRoot.setLayoutDirection(RelativeLayout.LAYOUT_DIRECTION_LTR);
        binding.msgText.setTextColor(Nice.getColor(R.color.color_grey_333333));
        binding.setIsMy(false);
    }

    /**
     * 刷新自己发的的消息
     *
     * @param helper
     * @param binding
     * @param item
     */
    private void refreshMineMsg(BaseViewHolder helper, ItemMessageBinding binding, IMMessage item) {
        binding.msgRoot.setLayoutDirection(RelativeLayout.LAYOUT_DIRECTION_RTL);
        binding.msgText.setTextColor(Nice.getColor(R.color.white));
        binding.setIsMy(true);
        //设置 消息的发送状态
        MsgStatusEnum status = item.getStatus();
        switch (status) {
            case fail:
                binding.msgLoading.setVisibility(View.GONE);
                binding.msgFail.setVisibility(View.VISIBLE);
                break;
            case sending:
                binding.msgLoading.setVisibility(View.VISIBLE);
                binding.msgFail.setVisibility(View.GONE);
                break;
            default:
                binding.msgLoading.setVisibility(View.GONE);
                binding.msgFail.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 刷新时间
     *
     * @param helper
     * @param binding
     * @param item
     */
    private void refreshMsgTime(BaseViewHolder helper, ItemMessageBinding binding, IMMessage item) {
        binding.msgTime.setText(TimeUtils.getTimeShowString(item.getTime(), false));
        if (helper.getAdapterPosition() - 1 >= 0) {
            IMMessage lastMsg = getData().get(helper.getAdapterPosition() - 1);
            if (item.getTime() - lastMsg.getTime() > 60000) {
                //相差60秒 显示时间
                binding.msgTime.setVisibility(View.VISIBLE);
            } else {
                binding.msgTime.setVisibility(View.GONE);
            }
        }
    }
}
