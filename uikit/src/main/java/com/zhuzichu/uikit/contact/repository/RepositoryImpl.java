package com.zhuzichu.uikit.contact.repository;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zhuzichu.uikit.contact.bean.FriendBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by wb.zhuzichu18 on 2018/10/15.
 */
public class RepositoryImpl implements Repository {

    @Override
    public Observable<List<FriendBean>> loadFriendList() {
        return Observable.create(emitter -> {
            List<String> accounts = NIMClient.getService(FriendService.class).getFriendAccounts(); // 获取所有好友帐号
            List<NimUserInfo> users = NIMClient.getService(UserService.class).getUserInfoList(accounts); // 获取所有好友用户资料
            List<FriendBean> list = new ArrayList<>();
            for (NimUserInfo item : users) {
                list.add(new FriendBean(item));
            }
            emitter.onNext(list);
        });
    }
}
