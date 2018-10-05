package com.zhuzichu.nice.contact;

import android.os.Bundle;

import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentContactBinding;

public class ContactFragment extends NiceFragment<FragmentContactBinding> {
    public static ContactFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void init(FragmentContactBinding binding) {
        binding.setColor(ColorManager.getInstance().color);
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_contact;
    }
}
