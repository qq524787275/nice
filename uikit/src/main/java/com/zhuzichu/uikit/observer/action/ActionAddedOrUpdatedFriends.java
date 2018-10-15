package com.zhuzichu.uikit.observer.action;

import com.netease.nimlib.sdk.friend.model.Friend;

import java.util.List;

public class ActionAddedOrUpdatedFriends {
    public List<Friend> data;

    public ActionAddedOrUpdatedFriends(List<Friend>  data) {
        this.data = data;
    }
}
