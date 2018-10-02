package com.zhuzichu.uikit.session.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.model.VMRecentContact;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentSessionListBinding;
import com.zhuzichu.uikit.session.adapter.SessionListAdapter;

import java.util.List;

import javax.annotation.Nullable;

public class SessionListFragment extends NiceFragment<FragmentSessionListBinding> {
    private VMRecentContact mVMRecentContact;
    private FragmentSessionListBinding mBinding;
    private SessionListAdapter mAdapter;
    private static final String TAG = "SessionListFragment";

    public static SessionListFragment newInstance() {

        Bundle args = new Bundle();

        SessionListFragment fragment = new SessionListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_session_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _status.hide();
    }

    @Override
    public void init(FragmentSessionListBinding binding) {
        mBinding = binding;
        mVMRecentContact = ViewModelProviders.of(getActivity()).get(VMRecentContact.class);
        initView();
        initObserve();
        mVMRecentContact.loadRecentContact();
    }

    private void initView() {
        mBinding.rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SessionListAdapter();
        mBinding.rvList.setAdapter(mAdapter);
    }

    private void initObserve() {
        mVMRecentContact.getRecentContact().observe(getActivity(), new Observer<List<RecentContact>>() {
            @Override
            public void onChanged(@Nullable List<RecentContact> recentContacts) {
                mBinding.layoutEmpty.hide();
                if (mAdapter.getData().size() == 0) {
                    mAdapter.addData(recentContacts);
                } else {
                    int index;
                    for (RecentContact item : recentContacts) {
                        index = -1;
                        for (int i = 0; i < mAdapter.getData().size(); i++) {
                            if (item.getContactId().equals(mAdapter.getData().get(i).getContactId())
                                    && item.getSessionType() == (mAdapter.getData().get(i).getSessionType())) {
                                index = i;
                                break;
                            }
                        }
                        if (index >= 0) {
                            mAdapter.getData().remove(index);
                        }
                        mAdapter.getData().add(item);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
