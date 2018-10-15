package com.zhuzichu.uikit.observer;

import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.friend.model.FriendChangedNotify;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.uikit.observer.action.ActionAddedOrUpdatedFriends;
import com.zhuzichu.uikit.observer.action.ActionDeletedFriends;
import com.zhuzichu.uikit.observer.action.ActionMessageStatus;
import com.zhuzichu.uikit.observer.action.ActionOnlineStatus;
import com.zhuzichu.uikit.observer.action.ActionOtherClients;
import com.zhuzichu.uikit.observer.action.ActionReceiveMessage;
import com.zhuzichu.uikit.observer.action.ActionRecentContact;
import com.zhuzichu.uikit.observer.action.ActionUserInfoUpdate;

import java.util.List;

/**
 *
 */
public class ObserverManager {
    private static final String TAG = "ObserverManager";
    /**
     * 联系人监听
     */
    private final static Observer<List<RecentContact>> observerRecentContact = (Observer<List<RecentContact>>) recentContacts -> {
        RxBus.getIntance().post(new ActionRecentContact(recentContacts));
    };

    /**
     * 用户在线监听
     */
    private final static Observer<StatusCode> observeOnlineStatus = (Observer<StatusCode>) statusCode -> {
        RxBus.getIntance().post(new ActionOnlineStatus(statusCode));
    };

    /**
     * 多端登录监听
     */
    private final static Observer<List<OnlineClient>> observeOtherClients = (Observer<List<OnlineClient>>) onlineClients -> {
        RxBus.getIntance().post(new ActionOtherClients(onlineClients));
    };

    /**
     * 消息状态监听
     */
    private final static Observer<IMMessage> observeMessageStatus = (Observer<IMMessage>) imMessage -> {
        Log.i(TAG, "zzc: observeMessageStatus");
        RxBus.getIntance().post(new ActionMessageStatus(imMessage));
    };

    /**
     * 消息接受监听
     */
    private final static Observer<List<IMMessage>> observeReceiveMessage = (Observer<List<IMMessage>>) list -> {
        Log.i(TAG, "zzc : observeReceiveMessage:size=" + list.size());
        RxBus.getIntance().post(new ActionReceiveMessage(list));
    };

    /**
     * 监听用户资料变更  不会实时监听
     * 用户资料除自己之外，不保证其他用户资料实时更新。其他用户数据更新时机为：
     * 1. 调用 fetchUserInfo 方法刷新用户
     * 2. 收到此用户发来消息（如果消息发送者有资料变更，SDK 会负责更新并通知）
     * 3. 程序再次启动，此时会同步好友信息
     */
    private final static Observer<List<NimUserInfo>> observerUserInfoUpdate = (Observer<List<NimUserInfo>>) list -> {
        Log.i(TAG, "zzc : observerUserInfoUpdate");
        RxBus.getIntance().post(new ActionUserInfoUpdate(list));
    };

    /**
     * 第三方 APP 应在 APP 启动后监听好友关系的变化，当主动添加好友成功、被添加为好友、
     * 主动删除好友成功、被对方解好友关系、好友关系更新、登录同步好友关系数据时都会收到通知。
     */
    private final static Observer<FriendChangedNotify> observerFriendChangedNotify = (Observer<FriendChangedNotify>) friendChangedNotify -> {
        Log.i(TAG, "zzc : observerFriendChangedNotify");
        List<Friend> addedOrUpdatedFriends = friendChangedNotify.getAddedOrUpdatedFriends();
        List<String> deletedFriends = friendChangedNotify.getDeletedFriends();
        if (addedOrUpdatedFriends != null && addedOrUpdatedFriends.size() != 0) {
            RxBus.getIntance().post(new ActionAddedOrUpdatedFriends(addedOrUpdatedFriends));
        }
        if (deletedFriends != null && deletedFriends.size() != 0) {
            RxBus.getIntance().post(new ActionDeletedFriends(deletedFriends));
        }
    };

    public static void regist() {
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(observeReceiveMessage, true);
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(observeMessageStatus, true);
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(observerRecentContact, true);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(observeOnlineStatus, true);
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(observeOtherClients, true);
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(observerUserInfoUpdate, true);
        NIMClient.getService(FriendServiceObserve.class).observeFriendChangedNotify(observerFriendChangedNotify, true);
    }

    public static void unRegist() {
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(observeReceiveMessage, false);
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(observeMessageStatus, false);
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(observerRecentContact, false);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(observeOnlineStatus, false);
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(observeOtherClients, false);
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(observerUserInfoUpdate, false);
        NIMClient.getService(FriendServiceObserve.class).observeFriendChangedNotify(observerFriendChangedNotify, false);
    }
}
