package com.zhuzichu.uikit.session.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.common.base.Optional;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.comment.color.ColorManager;
import com.zhuzichu.library.widget.NiceRequestCallback;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.databinding.FragmentSessionListBinding;
import com.zhuzichu.uikit.observer.action.ActionRecentContact;
import com.zhuzichu.uikit.session.adapter.SessionListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SessionListFragment extends NiceFragment<FragmentSessionListBinding> {
    // 置顶功能可直接使用，也可作为思路，供开发者充分利用RecentContact的tag字段
    public interface Extras {
        //会话置顶key
        String SESSION_ON_TOP = "session_on_top";
    }

    private FragmentSessionListBinding mBinding;
    private SessionListAdapter mAdapter;
    private OnSessionItemClickListener mOnSessionItemClickListener;
    private static final String TAG = "SessionListFragment";
    private List<RecentContact> mData;

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
        initView();
        initListener();
        initObserve();
    }


    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnSessionItemClickListener != null)
                mOnSessionItemClickListener.onSessionItemClick(mAdapter.getData().get(position));
        });

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            boolean isTop = isOnTop(mData.get(position));
            new MaterialDialog.Builder(getActivity())
                    .items(isTop ? R.array.session_dialog_items_top : R.array.session_dialog_items)
                    .theme(ColorManager.getInstance().getColorConfig().isDark ? Theme.DARK : Theme.LIGHT)
                    .itemsCallback((dialog, itemView, i, text) -> {
                        switch (i) {
                            case 0:
                                //设置 置顶 取消置顶
                                if (isTop) {
                                    //删除置顶
                                    deleteOnTop(mData.get(position));
                                } else {
                                    //设置置顶
                                    setOnTop(mData.get(position));
                                }
                                break;
                            case 1:
                                deleteSession(position);
                                //删除会话
                                break;
                        }
                    })
                    .build().show();
            return true;
        });
    }

    private void deleteOnTop(RecentContact item) {
        Map<String, Object> map = Optional.fromNullable(item.getExtension()).or(new HashMap<>());
        map.remove(Extras.SESSION_ON_TOP);
        item.setExtension(map);
        NIMClient.getService(MsgService.class).updateRecent(item);
        mAdapter.sortRefresh();
    }

    private void setOnTop(RecentContact item) {
        Map<String, Object> map = Optional.fromNullable(item.getExtension()).or(new HashMap<>());
        map.put(Extras.SESSION_ON_TOP, System.currentTimeMillis());
        item.setExtension(map);
        NIMClient.getService(MsgService.class).updateRecent(item);
        mAdapter.sortRefresh();
    }

    /**
     * 删除会话
     *
     * @param position
     */
    private void deleteSession(int position) {
        // 删除会话，删除后，消息历史被一起删除
        RecentContact recent = mData.get(position);
        NIMClient.getService(MsgService.class).deleteRecentContact(recent);
        NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
        mAdapter.remove(position);
    }

    /**
     * 判断当前会话是否是置顶
     *
     * @param item
     * @return
     */
    private boolean isOnTop(RecentContact item) {
        Optional<Map<String, Object>> extension = Optional.fromNullable(item.getExtension());
        if (!extension.isPresent())
            return false;
        Optional<Object> isTop = Optional.fromNullable(extension.get().get(Extras.SESSION_ON_TOP));
        if (!isTop.isPresent()) {
            return false;
        }
        return true;
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        loadRecentContact();
    }

    private void initView() {
        mData = new ArrayList<>();
        mAdapter = new SessionListAdapter(mData);
        mBinding.rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvList.setAdapter(mAdapter);
    }

    private void initObserve() {
        /**
         *消息会话监听
         */
        Disposable dispRecentContact = RxBus.getIntance().toObservable(ActionRecentContact.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(action -> action.getData())
                .flatMap(list -> Flowable.fromIterable(list))
                .map(item -> {
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).getContactId().equals(item.getContactId()))
                            return new TempRecentContact(i, item);
                    }
                    return new TempRecentContact(-1, item);
                }).subscribe(temp -> {
                    if (temp.index >= 0) {
                        mData.remove(temp.index);
                        mData.add(temp.index, temp.contact);
                    } else {
                        mData.add(temp.contact);
                    }
                    mAdapter.sortRefresh();
                });

        RxBus.getIntance().addSubscription(this, dispRecentContact);
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
    }

    /**
     * 加载本地会话列表数据
     */
    public void loadRecentContact() {
        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new NiceRequestCallback<List<RecentContact>>(getActivity()) {
            @Override
            public void success(List<RecentContact> list) {
                mData.addAll(list);
                mAdapter.sortRefresh();
            }
        });
    }
}

class TempRecentContact {
    int index;
    RecentContact contact;

    TempRecentContact(int index, RecentContact contact) {
        this.index = index;
        this.contact = contact;
    }
}