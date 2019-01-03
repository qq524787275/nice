package com.zhuzichu.uikit.message.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhuzichu.library.ui.matisse.internal.ui.widget.PreviewViewPager;
import com.zhuzichu.library.widget.NiceRequestCallback;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.message.adapter.PicAndVideoAdapter;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;

public class WatchPicAndVideoActivity extends SupportActivity {
    private IMMessage message;
    private PreviewViewPager mViewPager;
    private PicAndVideoAdapter mAdapter;

    public interface Extra {
        String EXTRA_MESSGAE = "extra_messgae";
    }

    public static void startActivity(Context context, IMMessage msg) {
        Intent intent = new Intent();
        intent.putExtra(Extra.EXTRA_MESSGAE, msg);
        intent.setClass(context, WatchPicAndVideoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_pic_and_video);
        parseData();
        QMUIStatusBarHelper.translucent(this);
        QMUIDisplayHelper.setFullScreen(this);
        initView();
        loadImageAndVideoMessage();
    }

    private void parseData() {
        message = (IMMessage) getIntent().getSerializableExtra(Extra.EXTRA_MESSGAE);
    }

    private void initView() {
        mViewPager = findViewById(R.id.vp);
    }

    public void loadImageAndVideoMessage() {
        // 查找图片和视频类型
        List<MsgTypeEnum> types = new ArrayList<>();
        types.add(MsgTypeEnum.image);
        types.add(MsgTypeEnum.video);
        // 查询锚点
        IMMessage anchor = MessageBuilder.createEmptyMessage(message.getSessionId(), message.getSessionType(), 0);
        NIMClient.getService(MsgService.class).queryMessageListByTypes(types, anchor, 0, QueryDirectionEnum.QUERY_OLD, Integer.MAX_VALUE, true)
                .setCallback(new NiceRequestCallback<List<IMMessage>>(this) {
                    @Override
                    public void success(List<IMMessage> data) {
                        bindAdapter(data);
                    }
                });
    }

    private void bindAdapter(List<IMMessage> data) {
        mAdapter = new PicAndVideoAdapter(getSupportFragmentManager(), data);
        mViewPager.setAdapter(mAdapter);
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getUuid().equals(message.getUuid())) {
                mViewPager.setCurrentItem(i, false);
                break;
            }
        }
    }
}
