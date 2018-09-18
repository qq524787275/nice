package com.zhuzichu.nice.contact;

import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.nice.R;

public class ContactFragment extends NiceFragment {
    public static ContactFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void init(ViewDataBinding binding) {

    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_contact;
    }
}
