package com.zhuzichu.uikit.session.repository;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public class SessionListRepositoryImpl implements SessionListRepository {

    /**
     * 查询最近联系人列表数据
     * @return
     */
    @Override
    public Observable<List<RecentContact>> loadRecentContact() {
        return Observable.create(emitter -> {
            List<RecentContact> data = NIMClient.getService(MsgService.class).queryRecentContactsBlock();
            emitter.onNext(data);
        });
    }
}
