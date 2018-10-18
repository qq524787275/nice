package com.zhuzichu.uikit.user.repository;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import io.reactivex.Observable;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public Observable<NimUserInfo> loadUserInfo(String account) {
        return Observable.create(emitter -> {
            NimUserInfo userInfo = NIMClient.getService(UserService.class).getUserInfo(account);
            emitter.onNext(userInfo);
        });
    }
}
