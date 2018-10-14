package com.zhuzichu.uikit.observer.action;

import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

public class ActionReceiveMessage {
    private List<IMMessage> data;

    public ActionReceiveMessage(List<IMMessage> data) {
        this.data = data;
    }

    public List<IMMessage> getData() {
        return data;
    }

    public void setData(List<IMMessage> data) {
        this.data = data;
    }
}
