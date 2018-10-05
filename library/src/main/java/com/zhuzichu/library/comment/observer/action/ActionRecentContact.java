package com.zhuzichu.library.comment.observer.action;

import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

public class ActionRecentContact {
    public static String key = ActionRecentContact.class.getSimpleName();
    public List<RecentContact> data;

    public ActionRecentContact(List<RecentContact> data) {
        this.data = data;
    }

    public List<RecentContact> getData() {
        return data;
    }

    public void setData(List<RecentContact> data) {
        this.data = data;
    }
}
