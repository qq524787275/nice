package com.zhuzichu.uikit.contact.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.zhuzichu.library.base.BaseViewModel;
import com.zhuzichu.uikit.contact.bean.FriendBean;
import com.zhuzichu.uikit.contact.repository.ContactRepository;
import com.zhuzichu.uikit.contact.repository.ContactRepositoryImpl;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wb.zhuzichu18 on 2018/10/15.
 */
public class ContactViewModel extends BaseViewModel {
    private ContactRepository contactRepository = new ContactRepositoryImpl();
    private MutableLiveData<List<FriendBean>> mLiveFriends = new MutableLiveData<>();

    public void loadFriendList() {
        Disposable subscribe = contactRepository.loadFriendList().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(friendBeans -> mLiveFriends.setValue(friendBeans));

        addDisposable(subscribe);
    }

    public MutableLiveData<List<FriendBean>> getLiveFriends() {
        return mLiveFriends;
    }
}
