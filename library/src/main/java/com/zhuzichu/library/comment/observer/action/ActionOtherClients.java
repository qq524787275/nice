package com.zhuzichu.library.comment.observer.action;

import com.netease.nimlib.sdk.auth.OnlineClient;

import java.util.List;

public class ActionOtherClients {
    public static String key = ActionOtherClients.class.getSimpleName();
    public List<OnlineClient> data;

    public ActionOtherClients(List<OnlineClient> data) {
        this.data = data;
    }

    public List<OnlineClient> getData() {
        return data;
    }

    public void setData(List<OnlineClient> data) {
        this.data = data;
    }
}
