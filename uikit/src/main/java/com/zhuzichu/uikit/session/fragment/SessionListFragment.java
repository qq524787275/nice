package com.zhuzichu.uikit.session.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.comment.observer.action.ActionRecentContact;
import com.zhuzichu.library.model.SessionViewModel;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentSessionListBinding;
import com.zhuzichu.uikit.session.adapter.SessionListAdapter;

import java.util.List;

import javax.annotation.Nullable;

import io.reactivex.disposables.Disposable;

public class SessionListFragment extends NiceFragment<FragmentSessionListBinding> {
    private SessionViewModel mSessionViewModel;
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
        mSessionViewModel = ViewModelProviders.of(getActivity()).get(SessionViewModel.class);
        initView();
        initListener();
        initObserve();
        mSessionViewModel.loadRecentContact();
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
        mSessionViewModel.getRecentContact().observe(getActivity(), recentContacts -> {
            //Todo 此处第一次加载 与 监听变换的数据写在一起。可考虑分开
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


        Disposable disposable = RxBus.getIntance().doSubscribe(ActionRecentContact.class, action -> {
            List<RecentContact> data = action.data;
            mSessionViewModel.getRecentContact().setValue(data);
        });
        RxBus.getIntance().addSubscription(this, disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getIntance().unSubscribe(this);
    }

    public void setOnSessionItemClickListener(OnSessionItemClickListener listener) {
        this.mOnSessionItemClickListener = listener;
    }

    public interface OnSessionItemClickListener {
        void onSessionItemClick(RecentContact recentContact);

        void onSessionItemLongClick(RecentContact recentContact);
    }
}
