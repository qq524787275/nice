package com.zhuzichu.uikit.message.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by wb.zhuzichu18 on 2018/10/16.
 */
@Deprecated
public class MessageBean implements MultiItemEntity {
    public static final int MSG_AUDIO = 0;
    public static final int MSG_AVCHAT = 1;
    public static final int MSG_TIP = 2;
    public static final int MSG_FILE = 3;
    public static final int MSG_TEXT = 4;
    public static final int MSG_IMAGE = 5;
    public static final int MSG_ROBOT = 6;
    public static final int MSG_LOCATION = 7;
    public static final int MSG_VIDEO = 8;
    public static final int MSG_NOTIFICATION = 9;
    public static final int MSG_UNDEF = 10;
    public static final int MSG_CUSTOM = 11;

    public IMMessage data;

    public MessageBean(IMMessage data) {
        this.data = data;
    }

    @Override
    public int getItemType() {
        MsgTypeEnum msgType = data.getMsgType();
        int type;
        switch (msgType) {
            case audio:
                type = MSG_AUDIO;
                break;
            case avchat:
                type = MSG_AVCHAT;
                break;
            case tip:
                type = MSG_TIP;
                break;
            case file:
                type = MSG_FILE;
                break;
            case text:
                type = MSG_TEXT;
                break;
            case image:
                type = MSG_IMAGE;
                break;
            case robot:
                type = MSG_ROBOT;
                break;
            case video:
                type = MSG_VIDEO;
                break;
            case custom:
                type = MSG_CUSTOM;
                break;
            case location:
                type = MSG_LOCATION;
                break;
            case notification:
                type = MSG_NOTIFICATION;
                break;
            case undef:
                type = MSG_UNDEF;
                break;
            default:
                type = MSG_UNDEF;
                break;
        }
        return type;
    }
}
