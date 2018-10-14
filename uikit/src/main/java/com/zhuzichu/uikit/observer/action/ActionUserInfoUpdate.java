package com.zhuzichu.uikit.observer.action;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

public class ActionUserInfoUpdate {
    public List<NimUserInfo> data;

    public ActionUserInfoUpdate(List<NimUserInfo> data) {
        this.data = data;
    }

    public List<NimUserInfo> getData() {
        return data;
    }

    public void setData(List<NimUserInfo> data) {
        this.data = data;
    }
}
