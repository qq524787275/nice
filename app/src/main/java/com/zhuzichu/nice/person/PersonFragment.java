package com.zhuzichu.nice.person;

import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.nice.R;

public class PersonFragment extends NiceFragment {
    public static PersonFragment newInstance() {
        
        Bundle args = new Bundle();
        
        PersonFragment fragment = new PersonFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void init(ViewDataBinding binding) {

    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_person;
    }
}
