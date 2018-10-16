package com.zhuzichu.uikit.message.provider;


import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.utils.TimeUtils;
import com.zhuzichu.uikit.BR;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.adapter.ImageAdapter;
import com.zhuzichu.uikit.databinding.ItemMessageBinding;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public abstract class MsgProviderBase extends BaseItemProvider<IMMessage, MessageMultipItemAdapter.DataBindingViewHolder> {

    abstract int getContainer();

    abstract void refreshView(View view, IMMessage item, int position);

    @Override
    public int layout() {
        return R.layout.item_message;
    }


    @Override
    public void convert(MessageMultipItemAdapter.DataBindingViewHolder helper, IMMessage item, int position) {
        ItemMessageBinding binding = helper.getBinding();
        binding.setVariable(BR.color, ColorManager.getInstance().color);
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

        View container = LayoutInflater.from(mContext).inflate(getContainer(), binding.msgContent,true);
        refreshView(container, item, position);
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
            IMMessage lastMsg = mData.get(helper.getAdapterPosition() - 1);
            if (item.getTime() - lastMsg.getTime() > 60000) {
                //相差60秒 显示时间
                binding.msgTime.setVisibility(View.VISIBLE);
            } else {
                binding.msgTime.setVisibility(View.GONE);
            }
        }
    }
}
