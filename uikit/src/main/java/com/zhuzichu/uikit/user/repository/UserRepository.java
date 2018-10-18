package com.zhuzichu.uikit.user.repository;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import io.reactivex.Observable;

public interface UserRepository {
    Observable<NimUserInfo> loadUserInfo(String account);
}
