package com.zhuzichu.nice.work;

import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentWorkBinding;

public class WorkFragment extends NiceFragment<FragmentWorkBinding> {
    public static WorkFragment newInstance() {
        
        Bundle args = new Bundle();
        
        WorkFragment fragment = new WorkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(FragmentWorkBinding binding) {

    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_work;
    }
}
