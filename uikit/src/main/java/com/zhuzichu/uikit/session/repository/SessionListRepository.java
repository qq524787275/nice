package com.zhuzichu.uikit.session.repository;

import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
public interface SessionListRepository {
    Observable<List<RecentContact>> loadRecentContact();
}
