package com.zhuzichu.nice.person.fragment;

import android.os.Bundle;

import com.zhuzichu.library.base.NiceSwipeFragment;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentSettingBinding;

public class SettingFragment extends NiceSwipeFragment<FragmentSettingBinding> {
    private FragmentSettingBinding mBind;

    public static SettingFragment newInstance() {

        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public void init(FragmentSettingBinding binding) {
        binding.setColor(ColorManager.getInstance().color);
        mBind = binding;
        initTopBar();
    }

    private void initTopBar() {
        mBind.topbar.setTitle("设置");
    }
}
