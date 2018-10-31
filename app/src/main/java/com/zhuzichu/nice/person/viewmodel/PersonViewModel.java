package com.zhuzichu.nice.person.viewmodel;

import android.databinding.ObservableField;
import android.text.TextUtils;

import com.zhuzichu.library.Nice;
import com.zhuzichu.library.base.BaseViewModel;
import com.zhuzichu.uikit.user.repository.UserRepository;
import com.zhuzichu.uikit.user.repository.UserRepositoryImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PersonViewModel extends BaseViewModel {
    private UserRepository mRepository = new UserRepositoryImpl();
    public ObservableField<String> account = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> avatar = new ObservableField<>();

    public void loadPersonInfo() {
        Disposable subscribe = mRepository.loadUserInfo(Nice.getAccount())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(personInfo -> {
                    avatar.set(personInfo.getAvatar());
                    name.set(getString(personInfo.getName()));
                    account.set(getString(personInfo.getAccount()));
                });
        addDisposable(subscribe);
    }

    private String getString(String s) {
        if (TextUtils.isEmpty(s))
            return "未设置";
        return s;
    }
}
