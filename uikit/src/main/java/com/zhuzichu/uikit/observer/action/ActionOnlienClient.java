package com.zhuzichu.uikit.observer.action;

import com.netease.nimlib.sdk.auth.OnlineClient;

import java.util.List;

public class ActionOnlienClient {
    public List<OnlineClient> data;

    public ActionOnlienClient(List<OnlineClient> data) {
        this.data = data;
    }

    public List<OnlineClient> getData() {
        return data;
    }

    public void setData(List<OnlineClient> data) {
        this.data = data;
    }
}
