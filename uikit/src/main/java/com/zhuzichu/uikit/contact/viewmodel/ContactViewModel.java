package com.zhuzichu.uikit.contact.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.zhuzichu.library.base.BaseViewModel;
import com.zhuzichu.uikit.contact.bean.FriendBean;
import com.zhuzichu.uikit.contact.repository.Repository;
import com.zhuzichu.uikit.contact.repository.RepositoryImpl;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wb.zhuzichu18 on 2018/10/15.
 */
public class ContactViewModel extends BaseViewModel {
    private CompositeDisposable mDisposables = new CompositeDisposable();
    private Repository repository = new RepositoryImpl();
    private MutableLiveData<List<FriendBean>> mLiveFriends = new MutableLiveData<>();

    public void loadFriendList() {
        Disposable subscribe = repository.loadFriendList().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(friendBeans -> mLiveFriends.setValue(friendBeans));
        mDisposables.add(subscribe);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (!mDisposables.isDisposed())
            mDisposables.dispose();
    }

    public MutableLiveData<List<FriendBean>> getLiveFriends() {
        return mLiveFriends;
    }
}
