package com.zhuzichu.uikit.message.provider;


import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
    protected IMMessage message;
    protected View view;
    protected ItemMessageBinding bind;
    protected MessageMultipItemAdapter.DataBindingViewHolder helper;

    abstract int getContentResId();

    abstract void inflateContentView();

    protected void onItemClick(IMMessage msg) {
    }

    abstract void refreshView();


    @Override
    public int layout() {
        return R.layout.item_message;
    }


    @Override
    public void convert(MessageMultipItemAdapter.DataBindingViewHolder helper, IMMessage item, int position) {
        this.helper = helper;
        this.bind = helper.getBinding();
        this.message = item;
        this.view = helper.itemView;
        bind.setVariable(BR.color, ColorManager.getInstance().color);
        //加载头像
        ImageAdapter.loadAvatar(bind.msgAvatar, message.getFromAccount());
        //刷新时间
        refreshMsgTime();
        //判断消息是否是自己的
        if (item.getFromAccount().equals(Nice.getAccount())) {
            //刷新自己的消息
            refreshMineMsg();
        } else {
            //刷新他人的消息
            refreshOtherMsg();
        }
        //添加点击事件
        helper.addOnClickListener(R.id.msg_fail);

        if (bind.msgContent.getChildCount() == 0) {
            View.inflate(mContext, getContentResId(), bind.msgContent);
        }
        inflateContentView();
        refreshView();
        setListener(item);
    }

    private void setListener(IMMessage item) {
        bind.msgContent.setOnClickListener(view->onItemClick(item));
    }


    /**
     * 刷新别人发的消息
     */
    private void refreshOtherMsg() {
        bind.msgLoading.setVisibility(View.GONE);
        bind.msgFail.setVisibility(View.GONE);
        bind.msgRoot.setLayoutDirection(RelativeLayout.LAYOUT_DIRECTION_LTR);
        bind.setIsMy(false);
    }

    /**
     * 刷新自己发的的消息
     */
    private void refreshMineMsg() {
        bind.msgRoot.setLayoutDirection(RelativeLayout.LAYOUT_DIRECTION_RTL);
        bind.setIsMy(true);
        //设置 消息的发送状态
        MsgStatusEnum status = message.getStatus();
        switch (status) {
            case fail:
                bind.msgLoading.setVisibility(View.GONE);
                bind.msgFail.setVisibility(View.VISIBLE);
                break;
            case sending:
                bind.msgLoading.setVisibility(View.VISIBLE);
                bind.msgFail.setVisibility(View.GONE);
                break;
            default:
                bind.msgLoading.setVisibility(View.GONE);
                bind.msgFail.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 刷新时间
     */
    private void refreshMsgTime() {
        bind.msgTime.setText(TimeUtils.getTimeShowString(message.getTime(), false));
        if (helper.getAdapterPosition() - 1 >= 0) {
            IMMessage lastMsg = mData.get(helper.getAdapterPosition() - 1);
            if (message.getTime() - lastMsg.getTime() > 60000) {
                //相差60秒 显示时间
                bind.msgTime.setVisibility(View.VISIBLE);
            } else {
                bind.msgTime.setVisibility(View.GONE);
            }
        }
    }

    // 设置控件的长宽
    protected void setLayoutParams(int width, int height, View... views) {
        for (View view : views) {
            ViewGroup.LayoutParams maskParams = view.getLayoutParams();
            maskParams.width = width;
            maskParams.height = height;
            view.setLayoutParams(maskParams);
        }
    }
}
