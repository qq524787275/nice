package com.zhuzichu.uikit.contact;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zhuzichu.library.action.ActionSoftKeyboard;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.contact.adapter.ContactAdapter;
import com.zhuzichu.uikit.contact.bean.FriendBean;
import com.zhuzichu.uikit.contact.viewmodel.ContactViewModel;
import com.zhuzichu.uikit.databinding.FragmentContactListBinding;
import com.zhuzichu.uikit.observer.action.ActionUserInfoUpdate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
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
    private ContactAdapter mAdapter;
    private ContactViewModel mViewModel;
    private List<FriendBean> mData;

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
        mViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        initView();
        initObserver();
    }

    private void initObserver() {
        mViewModel.getLiveFriends().observe(this, friendBeans -> {
            mAdapter.setDatas(friendBeans);
        });

        Disposable dispUserInfoUpdate = RxBus.getIntance()
                .toObservable(ActionUserInfoUpdate.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(action->action.getData())
                .flatMap(data-> Flowable.fromIterable(data))
                .map(item->{
                    for (int i = 0; i < mData.size(); i++) {

                    }
                    item.getAccount(mAdapter.getItems())
                })
                .subscribe();
        RxBus.getIntance().addSubscription(this, dispUserInfoUpdate);
    }


    private void initView() {
        mBind.layoutIndex.setLayoutManager(new LinearLayoutManager(getContext()));
        // 快速排序。  排序规则设置为：只按首字母  （默认全拼音排序）  效率很高，是默认的10倍左右。  按需开启～
        mBind.layoutIndex.setCompareMode(IndexableLayout.MODE_FAST);
        mAdapter = new ContactAdapter(getContext());
        mBind.layoutIndex.setAdapter(mAdapter);
        mData = mAdapter.getItems();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mViewModel.loadFriendList();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getIntance().unSubscribe(this);
    }
}
