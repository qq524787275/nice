package com.zhuzichu.uikit.contact.repository;

import com.zhuzichu.uikit.contact.bean.FriendBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by wb.zhuzichu18 on 2018/10/15.
 */
public interface ContactRepository {
    /**
     * 加载联系人列表数据
     *
     * @return
     */
    Observable<List<FriendBean>> loadFriendList();
}
