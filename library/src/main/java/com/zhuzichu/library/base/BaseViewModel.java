package com.zhuzichu.library.base;

import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {
    private CompositeDisposable mDisposables = new CompositeDisposable();

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.isDisposed();
    }

    public void addDisposable(Disposable... disposable) {
        mDisposables.addAll(disposable);
    }
}
