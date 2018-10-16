package com.zhuzichu.uikit.message.provider;

import android.view.View;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.zhuzichu.library.Nice;
import com.zhuzichu.library.view.face.NiceFaceView;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.adapter.MessageMultipItemAdapter;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public class MsgProviderText extends MsgProviderBase {


    @Override
    int getContainer() {
        return R.layout.item_messgae_text;
    }

    @Override
    void refreshView(View view, IMMessage item, int position) {
        NiceFaceView textView = view.findViewById(R.id.msg_text);
        textView.setText(item.getContent());
        //判断消息是否是自己的
        if (item.getFromAccount().equals(Nice.getAccount())) {
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
