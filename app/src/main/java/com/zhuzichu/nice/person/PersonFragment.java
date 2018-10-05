package com.zhuzichu.nice.person;

import android.os.Bundle;

import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentPersonBinding;

public class PersonFragment extends NiceFragment<FragmentPersonBinding> {
    public static PersonFragment newInstance() {
        
        Bundle args = new Bundle();
        
        PersonFragment fragment = new PersonFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void init(FragmentPersonBinding binding) {
        binding.setColor(ColorManager.getInstance().color);
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_person;
    }
}
