package com.zhuzichu.uikit.message.adapter;

import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.base.BaseDataBindingAdapter;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.comment.glide.GlideAdapter;
import com.zhuzichu.library.utils.TimeUtil;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.ItemMessageBinding;

import java.util.ArrayList;

public class MessageAdapter extends BaseDataBindingAdapter<IMMessage, ItemMessageBinding> {

    public MessageAdapter() {
        super(R.layout.item_message, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemMessageBinding binding, IMMessage item) {
        binding.setColor(ColorManager.getInstance().color);
        GlideAdapter.loadAvatar(binding.msgAvatar, item.getFromAccount());
        //获取上一条消息
        binding.msgTime.setText(TimeUtil.getTimeShowString(item.getTime(), false));

        if (helper.getAdapterPosition() - 1 >= 0) {
            IMMessage lastMsg = getData().get(helper.getAdapterPosition() - 1);
            if (item.getTime() - lastMsg.getTime() > 60000) {
                //相差60秒 显示时间
                binding.msgTime.setVisibility(View.VISIBLE);
            } else {
                binding.msgTime.setVisibility(View.GONE);
            }
        }


        if (item.getFromAccount().equals(Nice.getAccount())) {
            //自己发的消息
            binding.msgRoot.setLayoutDirection(RelativeLayout.LAYOUT_DIRECTION_RTL);
//            binding.msgContent.setBackground(mContext.getResources().getDrawable(R.drawable.chatto_bg));
            binding.msgText.setTextColor(Nice.getColor(R.color.white));
            binding.setIsMy(true);
        } else {
            //他人发的消息
            binding.msgRoot.setLayoutDirection(RelativeLayout.LAYOUT_DIRECTION_LTR);
//            binding.msgContent.setBackground(mContext.getResources().getDrawable(R.drawable.chatfrom_bg));
            binding.msgText.setTextColor(Nice.getColor(R.color.color_grey_333333));
            binding.setIsMy(false);
        }
        switch (item.getMsgType()) {
            case text:
                binding.msgText.setText(item.getContent());
                break;
        }
    }
}
