package com.zhuzichu.uikit.message.provider;

import android.view.View;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qmuiteam.qmui.widget.textview.QMUILinkTextView;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.view.face.emoji.EmojiconTextView;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public class MsgProviderText extends MsgProviderBase {
    private EmojiconTextView textView;


    @Override
    int getContentResId() {
        return R.layout.item_message_text;
    }

    @Override
    void inflateContentView() {
        textView = itemView.findViewById(R.id.msg_text);
    }

    @Override
    protected void onItemClick(IMMessage msg, MessageMultipItemAdapter.DataBindingViewHolder holder) {
//        Toast.makeText(mContext, "点击了文本", Toast.LENGTH_SHORT).show();
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

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "长点击", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        textView.setOnLinkClickListener(new QMUILinkTextView.OnLinkClickListener() {
            @Override
            public void onTelLinkClick(String phoneNumber) {
                Toast.makeText(mContext, ""+phoneNumber, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMailLinkClick(String mailAddress) {
                Toast.makeText(mContext, ""+mailAddress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWebUrlLinkClick(String url) {
                Toast.makeText(mContext, ""+url, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int viewType() {
        return MessageMultipItemAdapter.MSG_TEXT;
    }
}
