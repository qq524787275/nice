package com.zhuzichu.uikit.observer;

import android.util.Log;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zhuzichu.library.comment.bus.RxBus;
import com.zhuzichu.uikit.observer.action.ActionMessageStatus;
import com.zhuzichu.uikit.observer.action.ActionOnlineStatus;
import com.zhuzichu.uikit.observer.action.ActionOtherClients;
import com.zhuzichu.uikit.observer.action.ActionReceiveMessage;
import com.zhuzichu.uikit.observer.action.ActionRecentContact;
import com.zhuzichu.uikit.observer.action.ActionUserInfoUpdate;

import java.util.List;

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

    private final static Observer<List<NimUserInfo>> observerUserInfoUpdate = (Observer<List<NimUserInfo>>) list -> {
        Log.i(TAG, "zzc : userInfoUpdateObserver");
        RxBus.getIntance().post(new ActionUserInfoUpdate(list));
    };

    public static void regist() {
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(observeReceiveMessage, true);
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(observeMessageStatus, true);
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(observerRecentContact, true);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(observeOnlineStatus, true);
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(observeOtherClients, true);
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(observerUserInfoUpdate, true);
    }

    public static void unRegist() {
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(observeReceiveMessage, false);
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(observeMessageStatus, false);
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(observerRecentContact, false);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(observeOnlineStatus, false);
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(observeOtherClients, false);
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(observerUserInfoUpdate, false);
    }
}
