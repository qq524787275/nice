package com.zhuzichu.library.comment.observer.action;

import com.netease.nimlib.sdk.StatusCode;

public class ActionOnlineStatus {
    public StatusCode data;

    public ActionOnlineStatus(StatusCode data) {
        this.data = data;
    }

    public StatusCode getData() {
        return data;
    }

    public void setData(StatusCode data) {
        this.data = data;
    }
}
