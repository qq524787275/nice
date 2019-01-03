package com.zhuzichu.uikit.message.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.uikit.message.fragment.WatchPicFragment;
import com.zhuzichu.uikit.message.fragment.WatchVideFragment;

import java.util.List;


public class PicAndVideoAdapter extends FragmentPagerAdapter {

    private List<IMMessage> list;

    public PicAndVideoAdapter(FragmentManager fm, List<IMMessage> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int i) {
        IMMessage message = list.get(i);
        switch (message.getMsgType()) {
            case image:
                return WatchPicFragment.newInstance(message);
            case video:
                return WatchVideFragment.newInstance(message);
            default:
                return WatchPicFragment.newInstance(message);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
