package com.zhuzichu.nice.contact;

import android.os.Bundle;
import android.view.Gravity;

import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.nice.R;
import com.zhuzichu.nice.databinding.FragmentContactBinding;
import com.zhuzichu.uikit.contact.ContactListFragment;

public class ContactFragment extends NiceFragment<FragmentContactBinding> {
    private FragmentContactBinding mBind;
    private ContactListFragment mContactListFragment;


    @Override
    public Object setLayout() {
        return R.layout.fragment_contact;
    }

    public static ContactFragment newInstance() {

        Bundle args = new Bundle();

        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(FragmentContactBinding binding) {
        mBind = binding;
        binding.setColor(ColorManager.getInstance().color);
        mContactListFragment = ContactListFragment.newInstance();
        loadRootFragment(R.id.list_contact, mContactListFragment);
        initTopBar();
    }

    private void initTopBar() {
        mBind.topbar.setTitle(R.string.main_contact);
        mBind.topbar.setTitleGravity(Gravity.LEFT);
    }

}
