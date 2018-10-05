package com.zhuzichu.uikit.session.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.livedatabus.LiveDataBus;
import com.zhuzichu.library.comment.observer.action.ActionRecentContact;
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
    private OnSessionItemClickListener mOnSessionItemClickListener;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _status.hide();
    }

    @Override
    public void init(FragmentSessionListBinding binding) {
        mBinding = binding;
        mVMRecentContact = ViewModelProviders.of(getActivity()).get(VMRecentContact.class);
        initView();
        initListener();
        initObserve();
        mVMRecentContact.loadRecentContact();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnSessionItemClickListener != null)
                mOnSessionItemClickListener.onSessionItemClick(mAdapter.getData().get(position));
        });

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            if (mOnSessionItemClickListener != null)
                mOnSessionItemClickListener.onSessionItemLongClick(mAdapter.getData().get(position));
            return true;
        });
    }

    private void initView() {
        mBinding.rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SessionListAdapter();
        mBinding.rvList.setAdapter(mAdapter);
    }

    private void initObserve() {
        mVMRecentContact.getRecentContact().observe(getActivity(), recentContacts -> {
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
        });

        LiveDataBus.get().with(ActionRecentContact.key, ActionRecentContact.class).observe(this, actionRecentContact -> {
            List<RecentContact> data = actionRecentContact.data;
            mVMRecentContact.getRecentContact().setValue(data);
        });
    }

    public void setOnSessionItemClickListener(OnSessionItemClickListener listener) {
        this.mOnSessionItemClickListener = listener;
    }

    public interface OnSessionItemClickListener {
        void onSessionItemClick(RecentContact recentContact);

        void onSessionItemLongClick(RecentContact recentContact);
    }
}
