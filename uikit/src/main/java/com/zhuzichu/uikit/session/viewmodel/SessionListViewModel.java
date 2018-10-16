package com.zhuzichu.uikit.session.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.zhuzichu.library.base.BaseViewModel;
import com.zhuzichu.uikit.session.repository.SessionListRepository;
import com.zhuzichu.uikit.session.repository.SessionListRepositoryImpl;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public class SessionListViewModel extends BaseViewModel {
    private MutableLiveData<List<RecentContact>> mLiveSessionList = new MutableLiveData<>();
    private SessionListRepository mRepository = new SessionListRepositoryImpl();

    public void loadSessionList() {
        Disposable subscribe = mRepository.loadRecentContact()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(data -> {
                    mLiveSessionList.setValue(data);
                });
        addDisposable(subscribe);
    }

    public MutableLiveData<List<RecentContact>> getLiveSessionList() {
        return mLiveSessionList;
    }
}
