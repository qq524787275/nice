package com.zhuzichu.uikit.preview.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhuzichu.uikit.preview.fragment.PreViewItemFragment;

import java.util.List;


public class ImageAndVideoAdapter extends FragmentPagerAdapter {

    private List<IMMessage> list;

    public ImageAndVideoAdapter(FragmentManager fm, List<IMMessage> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int i) {
        return PreViewItemFragment.newInstance(list.get(i));
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
