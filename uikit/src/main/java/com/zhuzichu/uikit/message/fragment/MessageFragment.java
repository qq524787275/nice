package com.zhuzichu.uikit.message.fragment;

import android.os.Bundle;

import com.zhuzichu.library.base.NiceSwipeFragment;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentMessageBinding;

public class MessageFragment extends NiceSwipeFragment<FragmentMessageBinding> {
    private FragmentMessageBinding mBind;

    public static MessageFragment newInstance() {

        Bundle args = new Bundle();

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(FragmentMessageBinding binding) {
        binding.setColor(ColorManager.getInstance().color);
        mBind = binding;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_message;
    }
}
