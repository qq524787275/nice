package com.zhuzichu.uikit.user.viewmodel;

import android.databinding.ObservableField;
import android.text.TextUtils;

import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.zhuzichu.library.base.BaseViewModel;
import com.zhuzichu.uikit.user.repository.UserRepository;
import com.zhuzichu.uikit.user.repository.UserRepositoryImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserCardViewModel extends BaseViewModel {
    private UserRepository mRepository = new UserRepositoryImpl();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> birthday = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> gender = new ObservableField<>();
    public ObservableField<String> mobile = new ObservableField<>();
    public ObservableField<String> signature = new ObservableField<>();
    public ObservableField<String> account = new ObservableField<>();
    public ObservableField<String> avatar = new ObservableField<>();

    public void loadUserInfo(String accountId) {
        Disposable subscribe = mRepository.loadUserInfo(accountId)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(userInfo -> {
                    birthday.set(getString(userInfo.getBirthday()));
                    email.set(getString(userInfo.getEmail()));
                    GenderEnum genderEnum = userInfo.getGenderEnum();
                    switch (genderEnum) {
                        case MALE:
                            gender.set("男");
                            break;
                        case FEMALE:
                            gender.set("女");
                            break;
                        case UNKNOWN:
                            gender.set("未设置");
                            break;
                    }
                    mobile.set(getString(userInfo.getMobile()));
                    signature.set(getString(userInfo.getSignature()));
                    account.set(getString(userInfo.getAccount()));
                    avatar.set(userInfo.getAvatar());
                    name.set(getString(userInfo.getName()));
                });
        addDisposable(subscribe);
    }

    private String getString(String s) {
        if (TextUtils.isEmpty(s))
            return "未设置";
        return s;
    }
}
