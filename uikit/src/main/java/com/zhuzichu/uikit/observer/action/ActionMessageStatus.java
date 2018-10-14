package com.zhuzichu.uikit.observer.action;

import com.netease.nimlib.sdk.msg.model.IMMessage;

public class ActionMessageStatus {
    public IMMessage data;

    public ActionMessageStatus(IMMessage data) {
        this.data = data;
    }

    public IMMessage getData() {
        return data;
    }

    public void setData(IMMessage data) {
        this.data = data;
    }
}
