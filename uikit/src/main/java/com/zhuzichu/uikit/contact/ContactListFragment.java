package com.zhuzichu.uikit.contact;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.zhuzichu.library.action.ActionMainStartFragmnet;
import com.zhuzichu.library.base.NiceFragment;
import com.zhuzichu.library.bean.TempBean;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.library.widget.OnClickListener;
import com.zhuzichu.uikit.R;
import com.zhuzichu.uikit.contact.adapter.ContactAdapter;
import com.zhuzichu.uikit.contact.bean.FriendBean;
import com.zhuzichu.uikit.contact.viewmodel.ContactViewModel;
import com.zhuzichu.uikit.databinding.FragmentContactListBinding;
import com.zhuzichu.uikit.observer.ObserverManager;
import com.zhuzichu.uikit.observer.action.ActionAddedOrUpdatedFriends;
import com.zhuzichu.uikit.observer.action.ActionDeletedFriends;
import com.zhuzichu.uikit.user.fragment.UserCardFragment;
import com.zhuzichu.uikit.utils.UserInfoUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
        initView();
        mViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        mViewModel.getLiveFriends().observe(this, friendBeans -> {
            mData.addAll(friendBeans);
            mAdapter.notifyDataSetChanged();
            initObserver();
            subscribeOnlineEvnent(mData);
        });
    }

    private void subscribeOnlineEvnent(List<FriendBean> mData) {
        Observable.fromIterable(mData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(item -> item.getUserInfo().getAccount())
                .toList()
                .subscribe(list -> {
                    Log.i(TAG, "subscribeOnlineEvnent: "+list.size());
                    ObserverManager.subscribeOnlineStateEvent(list);
                });
    }

    private static final String TAG = "ContactListFragment";

    private void initObserver() {
        /**
         * 监听添加好友关系变换
         */
        Disposable dispAddedOrUpdatedFriends = RxBus.getIntance()
                .toObservable(ActionAddedOrUpdatedFriends.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(action -> action.data)
                .flatMap(data -> Flowable.fromIterable(data))
                .map(item -> {
                    Log.i(TAG, "initObserver: 改变了");
                    for (int i = 0; i < mData.size(); i++) {
                        if (item.getAccount().equals(mData.get(i).getUserInfo().getAccount()))
                            return new TempBean<>(i, UserInfoUtils.getUserInfo(item.getAccount()).get());
                    }
                    return new TempBean<>(-1, UserInfoUtils.getUserInfo(item.getAccount()).get());
                })
                .subscribe(temp -> {
                    if (temp.index >= 0)
                        mData.set(temp.index, new FriendBean(temp.data));
                    else
                        mData.add(new FriendBean(temp.data));
                    mAdapter.notifyDataSetChanged();
                });

        Disposable dispDeleteFriends = RxBus.getIntance()
                .toObservable(ActionDeletedFriends.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(action -> action.data)
                .flatMap(data -> Flowable.fromIterable(data))
                .map(item -> {
                    for (int i = 0; i < mData.size(); i++) {
                        if (item.equals(mData.get(i).getUserInfo().getAccount()))
                            return i;
                    }
                    return -1;
                })
                .subscribe(position -> {
                    if (position >= 0) {
                        mData.remove(position.intValue());
                        mAdapter.notifyDataSetChanged();
                    }
                });

        RxBus.getIntance().addSubscription(this, dispAddedOrUpdatedFriends, dispDeleteFriends);

        /**
         * Todo 监听用户资料随时更新 sdk 限制不能随时更新
         */
//        Disposable dispUserInfoUpdate = RxBus.getIntance()
//                .toObservable(ActionUserInfoUpdate.class)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(action -> action.getData())
//                .flatMap(data -> Flowable.fromIterable(data))
//                .map(item -> {
//                    for (int i = 0; i < mData.size(); i++) {
//                        if (item.getAccount().equals(mData.get(i).getUserInfo().getAccount()))
//                            return new TempBean<>(i, item);
//                    }
//                    return new TempBean<>(-1, item);
//                })
//                .subscribe(temp -> {
//                    if (temp.index >= 0)
//                        mData.set(temp.index, new FriendBean(temp.data));
//                    else
//                        mData.add(new FriendBean(temp.data));
//                    mAdapter.notifyDataSetChanged();
//                });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mViewModel.loadFriendList();
    }

    private void initView() {
        mBind.layoutIndex.setLayoutManager(new LinearLayoutManager(getContext()));
        // 快速排序。  排序规则设置为：只按首字母  （默认全拼音排序）  效率很高，是默认的10倍左右。  按需开启～
        mBind.layoutIndex.setCompareMode(IndexableLayout.MODE_FAST);
        mAdapter = new ContactAdapter(getContext());
        mData = new ArrayList<>();
        mAdapter.setDatas(mData);
        mBind.layoutIndex.setAdapter(mAdapter);

        mAdapter.setOnItemContentClickListener((v, originalPosition, currentPosition, entity) -> OnClickListener.noDoubleClick(() -> {
            RxBus.getIntance().post(new ActionMainStartFragmnet(UserCardFragment.newInstance(entity.getUserInfo().getAccount())));
        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getIntance().unSubscribe(this);
    }
}
