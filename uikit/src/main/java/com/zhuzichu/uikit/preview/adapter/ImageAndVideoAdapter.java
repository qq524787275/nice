package com.zhuzichu.uikit.preview.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.uikit.preview.fragment.PreViewItemFragment;
import com.zhuzichu.uikit.preview.fragment.PreviewImageFragment;
import com.zhuzichu.uikit.preview.fragment.PreviewVideoFragment;

import java.util.List;


public class ImageAndVideoAdapter extends FragmentPagerAdapter {

    private List<IMMessage> list;

    public ImageAndVideoAdapter(FragmentManager fm, List<IMMessage> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int i) {
        IMMessage message = list.get(i);
        switch (message.getMsgType()) {
            case image:
                return PreviewImageFragment.newInstance(message);
            case video:
                return PreviewVideoFragment.newInstance(message);
            default:
                return PreviewImageFragment.newInstance(message);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
