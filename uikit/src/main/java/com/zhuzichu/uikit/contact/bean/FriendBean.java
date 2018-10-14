package com.zhuzichu.uikit.contact.bean;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.io.Serializable;

import me.yokeyword.indexablerv.IndexableEntity;

public class FriendBean implements Serializable, IndexableEntity {
    private NimUserInfo userInfo;

    public FriendBean(NimUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public NimUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(NimUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String getFieldIndexBy() {
        return this.userInfo.getName();
    }

    @Override
    public void setFieldIndexBy(String indexField) {

    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {

    }
}
