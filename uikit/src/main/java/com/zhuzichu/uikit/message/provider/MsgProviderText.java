package com.zhuzichu.uikit.message.provider;

import android.widget.Toast;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.view.face.NiceFaceView;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public class MsgProviderText extends MsgProviderBase {
    private NiceFaceView textView;


    @Override
    int getContentResId() {
        return R.layout.item_message_text;
    }

    @Override
    void inflateContentView() {
        textView = itemView.findViewById(R.id.msg_text);
    }

    @Override
    protected void onItemClick(IMMessage msg,MessageMultipItemAdapter.DataBindingViewHolder holder) {
        Toast.makeText(mContext, "点击了文本", Toast.LENGTH_SHORT).show();
    }

    @Override
    void refreshView() {
        textView.setText(message.getContent());
        //判断消息是否是自己的
        if (message.getFromAccount().equals(Nice.getAccount())) {
            //刷新自己的消息
            textView.setTextColor(Nice.getColor(R.color.white));
        } else {
            //刷新他人的消息
            textView.setTextColor(Nice.getColor(R.color.black));
        }
    }

    @Override
    public int viewType() {
        return MessageMultipItemAdapter.MSG_TEXT;
    }
}
