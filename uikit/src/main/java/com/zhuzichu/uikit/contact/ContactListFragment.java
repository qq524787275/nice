package com.zhuzichu.uikit.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.contact.adapter.ContactAdapter;
import com.zhuzichu.uikit.contact.bean.FriendBean;
import com.zhuzichu.uikit.databinding.FragmentContactListBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * 通讯录
 */
public class ContactListFragment extends NiceFragment<FragmentContactListBinding> {
    private FragmentContactListBinding mBind;
    private CompositeDisposable mDisposables;
    private ContactAdapter mAdapter;

    public static ContactListFragment newInstance() {

        Bundle args = new Bundle();

        ContactListFragment fragment = new ContactListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_contact_list;
    }

    @Override
    public void init(FragmentContactListBinding binding) {
        mBind = binding;
        _status.hide();
        mDisposables = new CompositeDisposable();
        initView();
    }

    private void initView() {
        mBind.layoutIndex.setLayoutManager(new LinearLayoutManager(getContext()));
        // 快速排序。  排序规则设置为：只按首字母  （默认全拼音排序）  效率很高，是默认的10倍左右。  按需开启～
        mBind.layoutIndex.setCompareMode(IndexableLayout.MODE_FAST);
        mAdapter = new ContactAdapter(getContext());
        mBind.layoutIndex.setAdapter(mAdapter);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        loadFriendList();
    }

    /**
     * 加载好友列表
     */
    public void loadFriendList() {
        Disposable subscribe = Observable.create((ObservableOnSubscribe<List<FriendBean>>) emitter -> {
            List<String> accounts = NIMClient.getService(FriendService.class).getFriendAccounts(); // 获取所有好友帐号
            List<NimUserInfo> users = NIMClient.getService(UserService.class).getUserInfoList(accounts); // 获取所有好友用户资料
            List<FriendBean> list = new ArrayList<>();
            for (NimUserInfo item : users) {
                list.add(new FriendBean(item));
            }
            emitter.onNext(list);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    mAdapter.setDatas(list);
                });
        mDisposables.add(subscribe);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mDisposables.isDisposed())
            mDisposables.dispose();
    }
}
